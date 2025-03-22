package org.skyweave.service.api.data.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.skyweave.service.api.utils.enums.PaymentMethod;
import org.skyweave.service.api.utils.enums.PaymentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;
  private String razorpayPaymentLinkId;
  private String razorpayPaymentLinkReferenceId;
  private String razorpayPaymentLinkStatus;
  private String razorpayPaymentId;
}
