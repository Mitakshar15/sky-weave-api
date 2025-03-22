package org.skyweave.service.api.exception;

import java.io.Serial;

public class PurchaseException extends Exception {
  @Serial
  private static final long serialVersionUID = 1L;
  private final String messageKey;

  public PurchaseException(String messageKey, String message, Throwable cause) {
    super(message, cause);
    this.messageKey = messageKey;
  }

  public PurchaseException(String messageKey, String message) {
    super(message);
    this.messageKey = messageKey;
  }
}
