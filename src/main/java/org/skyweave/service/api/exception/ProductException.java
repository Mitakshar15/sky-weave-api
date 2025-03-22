package org.skyweave.service.api.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ProductException extends Exception {
  @Serial
  private static final long serialVersionUID = 1L;
  private final String messageKey;

  public ProductException(String messageKey, String message, Throwable cause) {
    super(message, cause);
    this.messageKey = messageKey;
  }

  public ProductException(String messageKey, String message) {
    super(message);
    this.messageKey = messageKey;
  }
}
