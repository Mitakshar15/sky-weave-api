package org.skyweave.service.api.service;

import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;

public interface UserService {
  User findUserByToken(String token) throws UserException;

  void validateUser(String token) throws UserException;

  boolean followUser(String creatorId, String followerId) throws UserException;

  User getMyProfile(String token) throws UserException;

  User getUserProfile(String userId) throws UserException;

}
