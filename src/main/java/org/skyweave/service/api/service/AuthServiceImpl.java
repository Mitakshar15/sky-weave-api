package org.skyweave.service.api.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.skyweave.service.api.config.jwt.JwtProvider;
import org.skyweave.service.api.data.TokenRepository;
import org.skyweave.service.api.data.UserRepository;
import org.skyweave.service.api.data.model.Token;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.UserMgmtMapper;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.api.utils.UserUtils;
import org.skyweave.service.api.utils.enums.Gender;
import org.skyweave.service.api.utils.enums.TokenType;
import org.skyweave.service.dto.AuthResponseDto;
import org.skyweave.service.dto.SignInRequest;
import org.skyweave.service.dto.SignUpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


  private final UserRepository userRepository;
  private final CustomUserServiceImpl customUserServiceImpl;
  private final JwtProvider jwtProvider;
  private final UserMgmtMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;
  private final UserUtils userUtils;

  @Override
  public User signUp(SignUpRequest signUpRequest) throws MessagingException, UserException {
    if (userRepository.existsByEmail((signUpRequest.getEmail()))) {
      throw new UserException(Constants.REQUEST_ERROR_KEY, Constants.USER_ALREADY_EXISTS_MESSAGE);
    }
    User user = mapper.toUserEntity(signUpRequest);
    switch (signUpRequest.getGender()) {
      case "M":
        user.setGender(Gender.MALE);
        break;
      case "F":
        user.setGender(Gender.FEMALE);
        break;
      case "O":
        user.setGender(Gender.UNKNOWN);
        break;
    }
    user.setDateOfBirth(LocalDate.now());
    user.setActive(false);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    User savedUser = userRepository.save(user);
    generateActivationToken(savedUser);
    return savedUser;
  }

  @Override
  public AuthResponseDto signIn(SignInRequest signInRequest) throws UserException {
    Authentication authentication =
        customUserServiceImpl.authenticate(signInRequest.getEmail(), signInRequest.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtProvider.generateToken(authentication);
    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setJwt(token);
    return authResponseDto;
  }

  @Override
  public void activateUser(User user, String activationCode) throws UserException {
    if (!user.isActive() && !user.isEmailVerified()) {
      Token verificationToken = tokenRepository.findByUserId((user.getUserId()));
      if (verificationToken.getActivationCode().equals(activationCode)) {
        if (!verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
          user.setActive(true);
          user.setEmailVerified(true);
          verificationToken.setUsed(Boolean.TRUE);
          tokenRepository.save(verificationToken);
          userRepository.save(user);
        } else {
          throw new UserException(Constants.CODE_INVALID_KEY,
              Constants.EMAIL_ACTIVATION_CODE_EXPIRED_MESSAGE);
        }
      } else {
        throw new UserException(Constants.CODE_INVALID_KEY,
            Constants.INVALID_EMAIL_ACTIVATION_CODE_MESSAGE);
      }
    } else {
      throw new UserException(Constants.CODE_INVALID_KEY,
          Constants.ALREADY_USED_EMAIL_ACTIVATION_CODE_MESSAGE);
    }
  }

  @Override
  public Token generateActivationToken(User user) throws MessagingException {
    Token token = new Token();
    token.setUserId(user.getUserId());
    token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
    token.setTokenType(TokenType.EMAIL_VERIFICATION_TOKEN);
    token.setUsed(Boolean.FALSE);
    // TODO: Implement Proper Activation code generating algorithm
    token.setActivationCode(RandomStringUtils.randomAlphanumeric(6));
    System.out.println("TOKEN CODE :: " + token.getActivationCode());
    boolean isMailSent = userUtils.sendActivationEmail(token.getActivationCode(), user);
    if (!isMailSent) {
      throw new RuntimeException(Constants.EMAIL_SENDING_FAILURE_MSG);
    }
    return tokenRepository.save(token);
  }

}
