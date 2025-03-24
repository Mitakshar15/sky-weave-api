package org.skyweave.service.api.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.skyweave.service.api.utils.enums.WorkStatus;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "digital_works")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalWork implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "title", unique = true, nullable = false)
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", nullable = false)
  private User creator;

  @OneToMany(mappedBy = "digitalWork", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<DigitalWorkFile> files = new ArrayList<>();

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @Column(name = "price")
  private Long price;

  @Column(name = "is_free", nullable = false)
  private Boolean isFree;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "digital_work_tags", joinColumns = @JoinColumn(name = "digital_work_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private WorkStatus status;

  @Column(name = "downloads", nullable = false)
  private int downloads = 0;

  @Column(name = "likes", nullable = false)
  private int likes = 0;

  @Column(name = "views", nullable = false)
  private int views = 0;

  @ElementCollection
  @CollectionTable(name = "digital_work_preview_images",
      joinColumns = @JoinColumn(name = "digital_work_id"))
  @Column(name = "preview_image_url")
  @Builder.Default
  private List<String> previewImages = new ArrayList<>();

  @Column(name = "license")
  private String license;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Version
  @Column(name = "version")
  private Long version;


}
