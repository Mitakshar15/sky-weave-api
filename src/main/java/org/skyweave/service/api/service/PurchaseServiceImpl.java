package org.skyweave.service.api.service;

import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.builder.ApiResponseBuilder;
import org.skyweave.service.api.data.DigitalWorkRepository;
import org.skyweave.service.api.data.PurchaseRepository;
import org.skyweave.service.api.data.UserRepository;
import org.skyweave.service.api.data.model.DigitalWork;
import org.skyweave.service.api.data.model.PaymentDetails;
import org.skyweave.service.api.data.model.Purchases;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.PurchaseException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.api.utils.DigitalWorkUtils;
import org.skyweave.service.api.utils.enums.PaymentStatus;
import org.skyweave.service.api.utils.enums.PurchaseStatus;
import org.skyweave.service.dto.PurchaseProductData;
import org.skyweave.service.dto.PurchaseRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final DigitalWorkMapper mapper;
  private final ApiResponseBuilder builder;
  private final DigitalWorkRepository digitalWorkRepository;
  private final UserRepository userRepository;
  private final DigitalWorkUtils digitalWorkUtils;

  @Override
  public PurchaseProductData purchase(User buyer, PurchaseRequest request) throws ProductException {
    Purchases purchases = new Purchases();
    DigitalWork digitalWork = digitalWorkRepository.findById(request.getDigitalWorkId())
        .orElseThrow(() -> new ProductException(Constants.DATA_NOT_FOUND_KEY,
            Constants.DIGITAL_WORK_NOT_FOUND_MESSAGE));
    purchases.setBuyer(buyer);
    purchases.setDigitalWork(digitalWork);
    purchases.setPurchasePrice(digitalWork.getPrice());
    purchaseRepository.save(purchases);
    buyer.getPurchases().add(purchases);
    userRepository.save(buyer);
    return mapper.toPurchaseProductData(purchases);
  }

  @Override
  public Purchases confirmPurchase(User user, String purchaseId)
      throws ProductException, PurchaseException {

    Purchases purchases = purchaseRepository.findById(purchaseId)
        .orElseThrow(() -> new ProductException(Constants.DATA_NOT_FOUND_KEY,
            Constants.PURCHASE_DETAILS_NOT_FOUND_MESSAGE));
    PaymentDetails details = digitalWorkUtils.handlePayment(purchases);
    boolean isPaid = true;
    if (!isPaid) {
      purchases.setPurchaseStatus(PurchaseStatus.FAILED);
      purchaseRepository.save(purchases);
      throw new PurchaseException(Constants.SERVICE_ERROR_KEY, Constants.SERVICE_ERROR_MESSAGE);
    }
    purchases.setPaymentDetails(details);
    if (details.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
      purchases.setFileAccess(true);
    }
    purchases.setPurchaseStatus(PurchaseStatus.COMPLETED);
    return purchaseRepository.save(purchases);
  }


}
