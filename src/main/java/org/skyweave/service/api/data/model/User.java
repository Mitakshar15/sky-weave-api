package org.skyweave.service.api.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.skyweave.service.api.utils.enums.Gender;
import org.skyweave.service.api.utils.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_id")
  private String userId;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "profile_picture")
  private String profilePicture;

  @Column(name = "bio")
  private String bio;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "is_email_verified")
  private boolean isEmailVerified;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @ElementCollection
  @CollectionTable(name = "payment_information", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "payment_info")
  private List<PaymentInformation> paymentInformation = new ArrayList<>();

  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  @ElementCollection
  @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "follower_id", unique = true)
  private List<String> followers;

  @ElementCollection
  @CollectionTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "following_id", unique = true)
  private List<String> following;

  @Column(name = "date_of_birth")
  @Temporal(TemporalType.DATE)
  private LocalDate dateOfBirth;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Purchases> purchases = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Address> address;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<SavedWorks> savedWorks = new ArrayList<>(); // equivalent to cart

  @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
  @Builder.Default
  private List<DigitalWork> digitalWorks = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

}
