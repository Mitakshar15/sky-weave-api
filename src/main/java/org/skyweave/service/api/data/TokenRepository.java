package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, String> {
  Token findByUserId(String userId);

}
