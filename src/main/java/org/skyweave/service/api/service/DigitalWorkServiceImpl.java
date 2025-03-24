package org.skyweave.service.api.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.config.RedisService;
import org.skyweave.service.api.data.*;
import org.skyweave.service.api.data.model.*;
import org.skyweave.service.api.data.redis.IRedisCacheRepository;
import org.skyweave.service.api.data.redis.RedisCacheRepositoryImpl;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.api.utils.DigitalWorkUtils;
import org.skyweave.service.api.utils.enums.SaveType;
import org.skyweave.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DigitalWorkServiceImpl implements DigitalWorkService {

  private final DigitalWorkMapper mapper;
  private final UserService userService;
  private final DigitalWorkRepository digitalWorkRepository;
  private final DigitalWorkUtils digitalWorkUtils;
  private final SavedWorkRepository savedWorkRepository;
  private final UserRepository userRepository;
  private final IRedisCacheRepository<PaginatedDigitalWorkDTO> digitalWorkCacheRepository;

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

  @Override
  public PaginatedDigitalWorkDTO getUserFeed(String userId, Integer page, Integer size,
      String categoryId, List<String> tags, String sort) throws ProductException, UserException {

    PaginatedDigitalWorkDTO dto = digitalWorkCacheRepository.findByKey("digital work of " + userId + " "+ page);
    if (dto == null) {

      User user = userRepository.findById(userId).orElseThrow(
          () -> new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.USER_NOT_FOUND_MESSAGE));
      List<String> followingIds = user.getFollowing();
      int pageNum = page != null && page >= 0 ? page : 0;
      int pageSize = size != null && size > 0 && size <= 100 ? size : 20;
      List<String> filteredTags = (tags != null && !tags.isEmpty()) ? tags : null;

      Sort sortOrder = sort != null ? digitalWorkUtils.parseSort(sort)
          : Sort.by(Sort.Direction.DESC, "createdAt");

      Pageable pageable = PageRequest.of(pageNum, pageSize, sortOrder);
      Page<DigitalWork> digitalWorksPage =
          digitalWorkRepository.findFeedPosts(followingIds, 0, categoryId, filteredTags, pageable)
              .orElseThrow(() -> new ProductException(Constants.DATA_NOT_FOUND_KEY,
                  Constants.DIGITAL_WORK_NOT_FOUND_MESSAGE));
      digitalWorkCacheRepository.save("digital work of " + userId + " "+ page, mapper.toPaginatedDTO(digitalWorksPage));

      return mapper.toPaginatedDTO(digitalWorksPage);
    }
    return dto;
  }


}
