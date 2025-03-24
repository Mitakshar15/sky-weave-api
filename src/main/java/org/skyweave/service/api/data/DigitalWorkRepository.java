package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.DigitalWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DigitalWorkRepository extends JpaRepository<DigitalWork, Long> {

  Optional<DigitalWork> findById(String id);

  // Todo: implement efficient feed logic
  @Query("SELECT dw FROM DigitalWork dw " + "WHERE dw.status = 'PUBLISHED' "
      + "AND (dw.views >= :minViews) "
      + "AND (:categoryId IS NULL OR dw.category.id = :categoryId)")
  Optional<Page<DigitalWork>> findFeedPosts(@Param("followingIds") List<String> followingIds,
      @Param("minViews") int minViews, @Param("categoryId") String categoryId,
      @Param("tags") List<String> tags, Pageable pageable);
}
