package org.skyweave.service.api.builder;


import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.config.apiconfig.BaseApiResponse;
import org.skyweave.service.api.config.apiconfig.Metadata;
import org.skyweave.service.api.config.apiconfig.Status;
import org.skyweave.service.api.config.jwt.JwtProvider;
import org.skyweave.service.api.data.SavedWorkRepository;
import org.skyweave.service.api.data.model.DigitalWork;
import org.skyweave.service.api.data.model.Purchases;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.service.DigitalWorkService;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.dto.AuthResponseDto;
import org.skyweave.service.dto.DigitalWorkDto;
import org.skyweave.service.dto.PaymentData;
import org.skyweave.service.dto.UserProfileDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApiResponseBuilder {

  private final Tracer tracer;
  private final JwtProvider jwtProvider;
  private final DigitalWorkMapper mapper;
  private final SavedWorkRepository savedWorkRepository;

  public BaseApiResponse buildSuccessApiResponse(String statusMessage) {
    BaseApiResponse response = new BaseApiResponse()
        .metadata(new Metadata().timestamp(Instant.now())
            .traceId(null != tracer.currentSpan()
                ? Objects.requireNonNull(tracer.currentSpan()).context().traceId()
                : ""))
        .status(new Status().statusCode(HttpStatus.OK.value()).statusMessage(statusMessage)
            .statusMessageKey(Constants.RESPONSE_MESSAGE_KEY_SUCCESS));
    return response;
  }

  public AuthResponseDto buildAuthResponseDto(Authentication authentication) {
    String token = jwtProvider.generateToken(authentication);
    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setJwt(token);
    return authResponseDto;
  }

  public DigitalWorkDto buildDigitalWorkDto(User user, DigitalWork digitalWork) {
    DigitalWorkDto dto = mapper.toDigitalWorkData(digitalWork);
    dto.setSaved(savedWorkRepository.existsByUserAndDigitalWork(user, digitalWork));
    return dto;
  }

  public PaymentData buildPurchaseData(Purchases purchases) {
    PaymentData paymentData = new PaymentData();
    paymentData.setStatus("SUCCESS");
    paymentData.setTransactionId(purchases.getTransactionId());
    return paymentData;
  }

  public UserProfileDTO buildUserProfileData(User myProfile) {
    UserProfileDTO userProfileDTO = mapper.toUserProfileDto(myProfile);
    userProfileDTO.setFollowersCount(myProfile.getFollowers().size());
    userProfileDTO.setFollowingCount(myProfile.getFollowing().size());
    return userProfileDTO;
  }
}
