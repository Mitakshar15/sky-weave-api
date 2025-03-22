package org.skyweave.service.api.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {

  @Column(name = "cardholder_name")
  private String cardHolderName;

  @Column(name = "card_number")
  private String cardNumber;

  @Column(name = "expiration_date")
  private String expirationDate;

  @Column(name = "cvv")
  private String cvv;

}
