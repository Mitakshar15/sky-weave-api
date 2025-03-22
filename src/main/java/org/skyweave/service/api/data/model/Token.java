package org.skyweave.service.api.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skyweave.service.api.utils.enums.TokenType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  private String id;

  @Column(name = "activation_code")
  private String activationCode;

  @Column(name = "user_id")
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "token_type")
  private TokenType tokenType;

  @Column(name = "expiry_date")
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime expiryDate;

  @Column(name = "is_used")
  private boolean isUsed;
}
