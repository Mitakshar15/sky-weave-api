package org.skyweave.service.api.v1;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.AuthMgmtV1Api;
import org.skyweave.service.api.builder.ApiResponseBuilder;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.UserMgmtMapper;
import org.skyweave.service.api.service.AuthService;
import org.skyweave.service.api.service.UserService;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthMgmtV1Api {
  private final AuthService authService;
  private final ApiResponseBuilder builder;
  private final UserMgmtMapper mapper;
  private final UserService userService;

  @Override
  public ResponseEntity<AuthResponse> signUp(@Valid SignUpRequest signUpRequest)
      throws MessagingException, UserException {
    User user = authService.signUp(signUpRequest);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    AuthResponse response =
        mapper.toAuthResponse(builder.buildSuccessApiResponse(Constants.SIGN_UP_SUCCESS_MESSAGE));
    response.data(builder.buildAuthResponseDto(authentication));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public ResponseEntity<AuthResponse> signIn(@Valid SignInRequest signInRequest)
      throws UserException {
    AuthResponse response =
        mapper.toAuthResponse(builder.buildSuccessApiResponse(Constants.LOGIN_SUCCESS_MESSAGE));
    response.data(authService.signIn(signInRequest));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  public ResponseEntity<ActivationResponse> activateUser(
      @NotNull @RequestHeader(value = Constants.AUTHORIZATION_HEADER_NAME) String authorization,
      @Valid @RequestBody ActivationRequest activationRequest) throws UserException {
    User user = userService.findUserByToken(authorization);
    authService.activateUser(user, activationRequest.getActivationCode());
    ActivationResponse response = mapper.toActivationResponse(
        builder.buildSuccessApiResponse(Constants.USER_ACTIVATION_SUCCESS_MESSAGE));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserMgmtBaseApiResponse> regenerateActivationCode(String authorization)
      throws Exception {
    UserMgmtBaseApiResponse response = mapper.toUserMgmtBaseApiResponse(
        builder.buildSuccessApiResponse(Constants.REGENERATE_ACTIVATION_CODE_SUCCESS_MESSAGE));
    User user = userService.findUserByToken(authorization);
    authService.generateActivationToken(user);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserMgmtBaseApiResponse> validateUser(String authorization)
      throws Exception {
    UserMgmtBaseApiResponse response = mapper
        .toUserMgmtBaseApiResponse(builder.buildSuccessApiResponse(Constants.VALID_USER_MESSAGE));
    userService.validateUser(authorization);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
