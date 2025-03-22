package org.skyweave.service.api.v1;

import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.UserMgmtV1Api;
import org.skyweave.service.api.builder.ApiResponseBuilder;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.mapper.UserMgmtMapper;
import org.skyweave.service.api.service.UserService;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.dto.UserMgmtBaseApiResponse;
import org.skyweave.service.dto.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserMgmtV1Api {


  private final UserService userService;
  private final UserMgmtMapper mapper;
  private final ApiResponseBuilder builder;

  @Override
  public ResponseEntity<UserMgmtBaseApiResponse> followUser(String authorization, String creatorId)
      throws Exception {
    User user = userService.findUserByToken(authorization);
    userService.followUser(user.getUserId(), creatorId);
    UserMgmtBaseApiResponse response = mapper.toUserMgmtBaseApiResponse(
        builder.buildSuccessApiResponse(Constants.FOLLOW_USER_SUCCESS_MESSAGE));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserProfileResponse> getMyProfile(String authorization) throws Exception {
    UserProfileResponse response = mapper.toUserProfileResponse(
        builder.buildSuccessApiResponse(Constants.USER_PROFILE_FETCH_SUCCESS_MESSAGE));
    response.data(builder.buildUserProfileData(userService.getMyProfile(authorization)));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
