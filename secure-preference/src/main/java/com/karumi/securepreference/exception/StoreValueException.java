package com.karumi.securepreference.exception;

/**
 *
 */
public class StoreValueException extends Exception {
  public StoreValueException(){
    super("An error happends storing encrypted data");
  }
}
