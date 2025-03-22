package org.skyweave.service.api.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "icon")
  private String icon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Category> children = new ArrayList<>();

  @OneToMany(mappedBy = "category")
  @Builder.Default
  private List<DigitalWork> digitalWorks = new ArrayList<>();

  public void addChild(Category child) {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }
    this.children.add(child);
    child.setParent(this); // Maintain bidirectional relationship
  }

}
