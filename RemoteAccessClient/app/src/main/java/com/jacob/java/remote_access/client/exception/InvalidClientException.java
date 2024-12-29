package com.jacob.java.remote_access.client.exception;

public class InvalidClientException extends RuntimeException {
  public InvalidClientException(String message) {
    super(message);
  }

  public InvalidClientException(String message, Throwable e) {
    super(message, e);
  }

  public InvalidClientException(Throwable e) {
    super(e);
  }
}
