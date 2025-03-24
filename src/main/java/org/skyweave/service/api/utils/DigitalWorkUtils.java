package org.skyweave.service.api.utils;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.data.AddressRepository;
import org.skyweave.service.api.data.CategoryRepository;
import org.skyweave.service.api.data.TagRepository;
import org.skyweave.service.api.data.UserRepository;
import org.skyweave.service.api.data.model.*;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.utils.enums.PaymentMethod;
import org.skyweave.service.api.utils.enums.PaymentStatus;
import org.skyweave.service.dto.AddressDto;
import org.skyweave.service.dto.CreateProductRequestCategory;
import org.skyweave.service.dto.DigitalWorkFileDto;
import org.skyweave.service.dto.TagDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DigitalWorkUtils {

  private final CategoryRepository categoryRepository;
  private final DigitalWorkMapper mapper;
  private final TagRepository tagRepository;
  private final AddressRepository addressRepository;
  private final UserRepository userRepository;


  public List<Tag> getOrCreateTags(@Valid List<@Valid TagDto> tags, DigitalWork digitalWork) {
    List<Tag> tagList = new ArrayList<>();
    for (TagDto tagDto : tags) {
      Tag tag = tagRepository.findByName(tagDto.getName()).orElseGet(() -> {
        Tag newTag = new Tag();
        newTag.setName(tagDto.getName());
        return newTag;
      });

      if (!tag.getDigitalWorks().contains(digitalWork)) {
        tag.getDigitalWorks().add(digitalWork);
      }
      tagList.add(tag);
    }
    return tagRepository.saveAll(tagList);
  }

  public List<DigitalWorkFile> processDigitalWorkFiles(@Valid List<@Valid DigitalWorkFileDto> files,
      DigitalWork digitalWork) {
    return files.stream().map(file -> {
      DigitalWorkFile newFile = mapper.toDigitalWorkFile(file);
      newFile.setDigitalWork(digitalWork);
      return newFile;
    }).collect(Collectors.toList());
  }

  public Category getOrCreateCategory(@Valid CreateProductRequestCategory categoryDto,
      DigitalWork digitalWork) throws ProductException {
    // First, check if the category already exists
    Optional<Category> existingCategory = categoryRepository.findByName(categoryDto.getName());
    if (existingCategory.isPresent()) {
      return existingCategory.get();
    }
    // Create new category
    Category category = new Category();
    category.setName(categoryDto.getName());
    category.setDescription(categoryDto.getDescription());
    // Handle parent category
    if (categoryDto.getParentCategory() != null) {
      Category parentCategory = categoryRepository.findByName(categoryDto.getParentCategory())
          .orElseThrow(() -> new ProductException("Parent category not found", "DD"));
      category.setParent(parentCategory);
      parentCategory.getChildren().add(category);
    }

    return categoryRepository.save(category);
  }


  public PaymentDetails handlePayment(Purchases purchases, AddressDto addressDto, User user) {
    PaymentDetails details = new PaymentDetails();
    Address address = mapper.toAddressEntity(addressDto);
    address.setUser(user);
    address = addressRepository.save(address);
    user.getAddress().add(address);
    userRepository.save(user);
    details.setPaymentMethod(PaymentMethod.UPI);
    details.setRazorpayPaymentId(UUID.randomUUID().toString());
    details.setRazorpayPaymentLinkStatus("APPROVED");
    details.setRazorpayPaymentLinkStatus("APPROVED");
    details.setPaymentStatus(PaymentStatus.COMPLETED);
    return details;
  }

  public Sort parseSort(String sort) {
    try {
      String[] parts = sort.split(",");
      String field = parts[0].trim();
      Sort.Direction direction =
          parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim()) ? Sort.Direction.ASC
              : Sort.Direction.DESC;
      if ("createdAt".equals(field) || "price".equals(field) || "likes".equals(field)
          || "views".equals(field)) {
        return Sort.by(direction, field);
      }
    } catch (Exception ignored) {
    }
    return Sort.by(Sort.Direction.DESC, "createdAt"); // Default
  }
}
