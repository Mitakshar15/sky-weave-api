package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  User findByEmail(String email);

  boolean existsByEmail(String email);
}
