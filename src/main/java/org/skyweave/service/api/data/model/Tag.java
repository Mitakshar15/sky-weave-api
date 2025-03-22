package org.skyweave.service.api.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @ManyToMany(mappedBy = "tags")
  @Builder.Default
  private List<DigitalWork> digitalWorks = new ArrayList<>();

  public void addDigitalWork(DigitalWork child) {
    if (this.digitalWorks == null) {
      this.digitalWorks = new ArrayList<>();
    }
    this.digitalWorks.add(child);
  }
}
