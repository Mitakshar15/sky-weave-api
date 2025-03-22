package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchases, String> {

}
