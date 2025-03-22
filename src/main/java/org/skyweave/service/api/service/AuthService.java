package org.skyweave.service.api.service;

import jakarta.mail.MessagingException;
import org.skyweave.service.api.data.model.Token;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.dto.AuthResponseDto;
import org.skyweave.service.dto.SignInRequest;
import org.skyweave.service.dto.SignUpRequest;

public interface AuthService {
  User signUp(SignUpRequest signUpRequest) throws MessagingException, UserException;

  AuthResponseDto signIn(SignInRequest signInRequest) throws UserException;

  void activateUser(User user, String activationCode) throws UserException;

  Token generateActivationToken(User user) throws MessagingException;
}
