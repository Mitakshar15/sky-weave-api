package org.skyweave.service.api.v1;


import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.PurchaseMgmtV1Api;
import org.skyweave.service.api.builder.ApiResponseBuilder;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.PurchaseException;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.service.PurchaseService;
import org.skyweave.service.api.service.UserService;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PurchaseController implements PurchaseMgmtV1Api {

  private final PurchaseService purchaseService;
  private final UserService userService;
  private final DigitalWorkMapper mapper;
  private final ApiResponseBuilder builder;

  @Override
  public ResponseEntity<PurchaseProductResponse> purchaseDigitalWork(String authorization,
      PurchaseRequest purchaseRequest) throws Exception {
    User user = userService.findUserByToken(authorization);
    PurchaseProductResponse response = mapper.toPurchaseProductResponse(
        builder.buildSuccessApiResponse(Constants.PURCHASE_SUCCESS_MESSAGE));
    response.data(purchaseService.purchase(user, purchaseRequest));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PaymentResponse> confirmPurchase(String authorization,
      PaymentRequest paymentRequest) throws UserException, PurchaseException, ProductException {
    User user = userService.findUserByToken(authorization);
    PaymentResponse response = mapper
        .toPaymentResponse(builder.buildSuccessApiResponse(Constants.PAYMENT_SUCCESFULL_MESSAGE));
    response.data(builder
        .buildPurchaseData(purchaseService.confirmPurchase(user, paymentRequest.getPurchaseId())));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
