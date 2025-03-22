package org.skyweave.service.api.v1;

import lombok.RequiredArgsConstructor;
import org.skyweave.service.api.DigitalProductMgmtV1Api;
import org.skyweave.service.api.builder.ApiResponseBuilder;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.api.mapper.DigitalWorkMapper;
import org.skyweave.service.api.service.DigitalWorkService;
import org.skyweave.service.api.service.UserService;
import org.skyweave.service.api.utils.Constants;
import org.skyweave.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DigitalWorkController implements DigitalProductMgmtV1Api {
  private final DigitalWorkService digitalWorkService;
  private final DigitalWorkMapper mapper;
  private final ApiResponseBuilder builder;
  private final UserService userService;


  @Override
  public ResponseEntity<CreateProductResponse> createDigitalWork(String authorization,
      CreateProductRequest createProductRequest) throws Exception {
    User user = userService.findUserByToken(authorization);
    digitalWorkService.createDigitalWork(user, createProductRequest);
    CreateProductResponse response = mapper.toCreateProductResponse(
        builder.buildSuccessApiResponse(Constants.CREATE_DIGITAL_WORK_SUCCESS_MESSAGE));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<DigitalWorkResponse> getDigitalWorkById(String authorization,
      String productId) throws Exception {
    // Todo : implement auth logic for each request
    User user = userService.findUserByToken(authorization);
    DigitalWorkResponse response = mapper.toDigitalWorkResponse(
        builder.buildSuccessApiResponse(Constants.GET_DIGITAL_WORK_SUCCESS_MESSAGE));
    response.data(builder.buildDigitalWorkDto(user, digitalWorkService.getDigitalWork(productId)));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<DigitalWorkServiceBaseApiResponse> saveDigitalWork(String authorization,
      SaveDigitalWorkRequest saveDigitalWorkRequest) throws UserException, ProductException {
    DigitalWorkServiceBaseApiResponse response = mapper.toDigitalWorkServiceBaseApiResponse(
        builder.buildSuccessApiResponse(Constants.SAVE_DIGITAL_WORK_SUCCESS_MESSAGE));
    digitalWorkService.saveDigitalWork(saveDigitalWorkRequest, authorization);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
