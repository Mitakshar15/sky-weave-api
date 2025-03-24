package org.skyweave.service.api.service;

import jakarta.validation.Valid;
import org.skyweave.service.api.data.model.DigitalWork;
import org.skyweave.service.api.data.model.SavedWorks;
import org.skyweave.service.api.data.model.User;
import org.skyweave.service.api.exception.ProductException;
import org.skyweave.service.api.exception.UserException;
import org.skyweave.service.dto.CreateProductRequest;
import org.skyweave.service.dto.PaginatedDigitalWorkDTO;
import org.skyweave.service.dto.SaveDigitalWorkRequest;

import java.util.List;

public interface DigitalWorkService {

  void createDigitalWork(User user, CreateProductRequest request) throws ProductException;

  DigitalWork getDigitalWork(String digitalWorkId) throws ProductException;

  SavedWorks saveDigitalWork(@Valid SaveDigitalWorkRequest request, String authorization)
      throws ProductException, UserException;

  public PaginatedDigitalWorkDTO getUserFeed(String userId, Integer page, Integer size,
      String categoryId, List<String> tagIds, String sort) throws ProductException, UserException;

}
