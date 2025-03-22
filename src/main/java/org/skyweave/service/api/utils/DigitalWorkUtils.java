package org.skyweave.service.api.utils;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.data.CategoryRepository;
import org.skyweave.service.api.data.TagRepository;
import org.skyweave.service.api.data.model.*;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.utils.enums.PaymentMethod;
import org.skyweave.service.api.utils.enums.PaymentStatus;
import org.skyweave.service.dto.CreateProductRequestCategory;
import org.skyweave.service.dto.DigitalWorkFileDto;
import org.skyweave.service.dto.TagDto;
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


  public PaymentDetails handlePayment(Purchases purchases) {
    PaymentDetails details = new PaymentDetails();
    details.setPaymentMethod(PaymentMethod.UPI);
    details.setRazorpayPaymentId(UUID.randomUUID().toString());
    details.setRazorpayPaymentLinkStatus("APPROVED");
    details.setRazorpayPaymentLinkStatus("APPROVED");
    details.setPaymentStatus(PaymentStatus.COMPLETED);
    return details;
  }
}
