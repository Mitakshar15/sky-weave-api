package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.DigitalWork;
import org.skyweave.service.api.data.model.SavedWorks;
import org.skyweave.service.api.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedWorkRepository extends JpaRepository<SavedWorks, String> {

  boolean existsByUserAndDigitalWork(User user, DigitalWork digitalWork);
}
