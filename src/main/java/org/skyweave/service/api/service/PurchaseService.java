package org.skyweave.service.api.service;

import org.skyweave.service.api.data.model.Purchases;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.PurchaseException;
import org.skyweave.service.dto.PurchaseProductData;
import org.skyweave.service.dto.PurchaseRequest;

public interface PurchaseService {

  PurchaseProductData purchase(User user, PurchaseRequest request) throws ProductException;

  Purchases confirmPurchase(User user, String purchaseId)
      throws ProductException, PurchaseException;


}
