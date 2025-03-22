package org.skyweave.service.api.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.skyweave.service.api.utils.enums.PaymentMethod;
import org.skyweave.service.api.utils.enums.PurchaseStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchases {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "purchase_id")
  private String purchaseId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", nullable = false)
  private User buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "digital_work_id", nullable = false)
  private DigitalWork digitalWork;

  @Column(name = "purchase_price", nullable = false)
  private Long purchasePrice;

  @Column(name = "purchase_date", nullable = false)
  @CreationTimestamp
  private LocalDateTime purchaseDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "purchase_status", nullable = false)
  private PurchaseStatus purchaseStatus;

  @Embedded
  private PaymentDetails paymentDetails;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "file_access")
  private boolean fileAccess = false;

  @PrePersist
  public void prePersist() {
    this.purchaseStatus = PurchaseStatus.PENDING;
    this.purchaseDate = LocalDateTime.now();
    this.transactionId = UUID.randomUUID().toString();
  }
}
