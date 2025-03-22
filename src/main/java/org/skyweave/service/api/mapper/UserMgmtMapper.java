package org.skyweave.service.api.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.skyweave.service.api.config.apiconfig.BaseApiResponse;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.dto.*;

@Mapper(componentModel = "spring")
public interface UserMgmtMapper {

  @Mapping(target = "gender", ignore = true)
  User toUserEntity(SignUpRequest signUpRequest);

  AuthResponse toAuthResponse(BaseApiResponse baseApiResponse);

  ActivationResponse toActivationResponse(BaseApiResponse baseApiResponse);

  UserMgmtBaseApiResponse toUserMgmtBaseApiResponse(BaseApiResponse baseApiResponse);

  UserProfileResponse toUserProfileResponse(BaseApiResponse baseApiResponse);
}
