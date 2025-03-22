package org.skyweave.service.api.data.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.skyweave.service.api.utils.enums.SaveType;
import org.skyweave.service.api.utils.enums.SavedWorkStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_works",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "digital_work_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedWorks {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "digital_work_id", nullable = false)
  private DigitalWork digitalWork;

  @Enumerated(EnumType.STRING)
  @Column(name = "save_type", nullable = false)
  private SaveType saveType;

  @Column(name = "saved_at", nullable = false)
  private LocalDateTime savedAt;

  @Column(name = "notes")
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SavedWorkStatus status;

  @PrePersist
  public void prePersist() {
    this.savedAt = LocalDateTime.now();
    this.status = SavedWorkStatus.ACTIVE;
  }
}


