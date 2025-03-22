package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.DigitalWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DigitalWorkRepository extends JpaRepository<DigitalWork, Long> {

  Optional<DigitalWork> findById(String id);
}
