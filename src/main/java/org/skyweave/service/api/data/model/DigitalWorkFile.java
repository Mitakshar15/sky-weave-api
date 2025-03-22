package org.skyweave.service.api.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "digital_work_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalWorkFile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "digital_work_id", nullable = false)
  private DigitalWork digitalWork;

  @Column(name = "file_url", nullable = false)
  private String fileUrl;

  @Column(name = "file_type", nullable = false)
  private String fileType;

  @Column(name = "file_size", nullable = false)
  private long fileSize;
}
