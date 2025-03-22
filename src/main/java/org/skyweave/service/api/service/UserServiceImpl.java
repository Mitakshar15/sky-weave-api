package org.skyweave.service.api.service;


import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.config.jwt.JwtProvider;
import org.skyweave.service.api.data.UserRepository;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.utils.Constants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  @Cacheable(value = "users", key = "#id")
  public User findUserByToken(String token) throws UserException {
    String email = jwtProvider.getEmailFromJwtToken(token);
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new RuntimeException("USER NOT FOUND");
    }
    return user;
  }

  @Override
  @Cacheable(value = "users", key = "#id")
  public void validateUser(String token) throws UserException {
    try {
      User user = findUserByToken(token);
    } catch (Exception e) {
      throw new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.INVALID_USER_MESSAGE);
    }
    // TODO: implement other validation logic here later
  }

  @Override
  public User findUserByCreator(String creator) throws UserException {
    return userRepository.findById(creator).orElseThrow(
        () -> new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.USER_NOT_FOUND_MESSAGE));
  }

  @Override
  public boolean followUser(String creatorId, String followerId) throws UserException {
    User creator = userRepository.findById(creatorId).orElseThrow(
        () -> new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.USER_NOT_FOUND_MESSAGE));
    User follower = userRepository.findById(followerId).orElseThrow(
        () -> new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.USER_NOT_FOUND_MESSAGE));
    try {
      creator.getFollowers().add(followerId);
      follower.getFollowing().add(creatorId);
      userRepository.save(creator);
      userRepository.save(follower);
      return true;
    } catch (Exception e) {
      throw new UserException(Constants.SERVICE_ERROR_KEY,
          Constants.FAILED_TO_FOLLOW_MESSAGE + e.getMessage());
    }
  }

  @Override
  public User getMyProfile(String token) throws UserException {
    User user = findUserByToken(token);
    if (user == null) {
      throw new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.USER_NOT_FOUND_MESSAGE);
    }
    return user;
  }


}
