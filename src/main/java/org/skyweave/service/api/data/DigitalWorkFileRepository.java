package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.DigitalWorkFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalWorkFileRepository extends JpaRepository<DigitalWorkFile, Long> {
}
