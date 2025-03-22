package org.skyweave.service.api.mapper;


import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;
import org.skyweave.service.api.config.apiconfig.BaseApiResponse;
import org.skyweave.service.api.data.model.*;
import org.skyweave.service.dto.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DigitalWorkMapper {

  // Add a custom mapping method for User to String
  default String mapUserToId(User user) {
    return user != null ? user.getFirstName() : null;
  }

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "creator", ignore = true)
  DigitalWork toDigitalWorkEntity(CreateProductRequest request);

  CreateProductResponse toCreateProductResponse(BaseApiResponse baseApiResponse);

  DigitalWorkFile toDigitalWorkFile(DigitalWorkFileDto file);

  DigitalWorkResponse toDigitalWorkResponse(BaseApiResponse baseApiResponse);

  @Mapping(target = "creator",
      expression = "java(digitalWork.getCreator() != null ? digitalWork.getCreator().getUserId() : null)")
  @Mapping(target = "tags",
      expression = "java(digitalWork.getTags().stream().map(Tag::getName).collect(java.util.stream.Collectors.toList()))")
  @Mapping(target = "category", qualifiedByName = "mapCategoryToDto")
  DigitalWorkDto toDigitalWorkData(DigitalWork digitalWork);

  @Named("mapCategoryToDto")
  default CategoryDto mapCategoryToDto(Category category) {
    return null;
  }

  DigitalWorkServiceBaseApiResponse toDigitalWorkServiceBaseApiResponse(
      BaseApiResponse baseApiResponse);

  @Mapping(target = "buyer", expression = "java(purchases.getBuyer().getUserId())")
  @Mapping(target = "digitalProductId", expression = "java(purchases.getDigitalWork().getId())")
  PurchaseProductData toPurchaseProductData(Purchases purchases);

  PurchaseProductResponse toPurchaseProductResponse(BaseApiResponse baseApiResponse);

  PaymentResponse toPaymentResponse(BaseApiResponse baseApiResponse);


  UserProfileDTO toUserProfileDto(User myProfile);
}
