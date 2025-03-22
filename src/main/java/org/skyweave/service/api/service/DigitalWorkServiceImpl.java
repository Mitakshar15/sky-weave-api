package org.skyweave.service.api.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.data.*;
import org.skyweave.service.api.data.model.*;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.api.utils.DigitalWorkUtils;
import org.skyweave.service.api.utils.enums.SaveType;
import org.skyweave.service.api.utils.enums.SavedWorkStatus;
import org.skyweave.service.dto.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DigitalWorkServiceImpl implements DigitalWorkService {

  private final DigitalWorkRepository repository;
  private final DigitalWorkMapper mapper;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;
  private final UserService userService;
  private final DigitalWorkRepository digitalWorkRepository;
  private final DigitalWorkFileRepository digitalWorkFileRepository;
  private final DigitalWorkUtils digitalWorkUtils;
  private final SavedWorkRepository savedWorkRepository;
  private final UserRepository userRepository;


  @Override
  @Transactional
  public void createDigitalWork(User user, @Valid CreateProductRequest request)
      throws ProductException {
    // Create and save the digital work first
    DigitalWork digitalWork = mapper.toDigitalWorkEntity(request);
    digitalWork.setCreator(user);

    // Process and set files first
    List<DigitalWorkFile> digitalWorkFiles =
        digitalWorkUtils.processDigitalWorkFiles(request.getFiles(), digitalWork);
    digitalWork.setFiles(digitalWorkFiles);

    Category category = digitalWorkUtils.getOrCreateCategory(request.getCategory(), digitalWork);
    digitalWork.setCategory(category);

    List<Tag> processedTags = digitalWorkUtils.getOrCreateTags(request.getTags(), digitalWork);
    digitalWork.setTags(processedTags);

    digitalWorkRepository.save(digitalWork);
  }

  @Override
  @Cacheable(value = "digitalWorks", key = "#digitalWorkId", unless = "#result == null")
  public DigitalWork getDigitalWork(String digitalWorkId) throws ProductException {
    Optional<DigitalWork> opt = digitalWorkRepository.findById(digitalWorkId);
    if (opt.isPresent()) {
      return opt.get();
    } else {
      throw new ProductException(Constants.DATA_NOT_FOUND_KEY,
          Constants.DIGITAL_WORK_NOT_FOUND_MESSAGE);
    }
  }

  @Override
  public SavedWorks saveDigitalWork(@Valid SaveDigitalWorkRequest request, String authorization)
      throws ProductException, UserException {
    User user = userService.findUserByToken(authorization);
    DigitalWork digitalWork = digitalWorkRepository.findById(request.getDigitalWorkId())
        .orElseThrow(() -> new ProductException(Constants.DATA_NOT_FOUND_KEY,
            Constants.DIGITAL_WORK_NOT_FOUND_MESSAGE));
    SavedWorks savedWorks = SavedWorks.builder().digitalWork(digitalWork).user(user).build();
    switch (request.getSaveType()) {
      case BOOKMARK:
        savedWorks.setSaveType(SaveType.BOOKMARK);
      case INSPIRATION:
        savedWorks.setSaveType(SaveType.INSPIRATION);
      default:
        savedWorks.setSaveType(SaveType.WISHLIST);
    }
    savedWorks = savedWorkRepository.save(savedWorks);
    user.getSavedWorks().add(savedWorks);
    userRepository.save(user);
    return savedWorks;
  }


}
