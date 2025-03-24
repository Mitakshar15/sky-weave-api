package org.skyweave.service.api.data;

import org.skyweave.service.api.data.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
