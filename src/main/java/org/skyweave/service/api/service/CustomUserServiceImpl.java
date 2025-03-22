package org.skyweave.service.api.service;

import lombok.AllArgsConstructor;
import org.skyweave.service.api.data.UserRepository;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.utils.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(), authorities);
  }

  public Authentication authenticate(String username, String password)
      throws AuthenticationException, UserException {
    UserDetails userDetails = loadUserByUsername(username);
    if (userDetails == null) {
      throw new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.INVALID_USERNAME_MESSAGE);
    }
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new UserException(Constants.DATA_NOT_FOUND_KEY, Constants.INVALID_PASSWORD_MESSAGE);
    }
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
