package com.karumi.securepreference.exception;

/**
 *
 */
public class InvalidatedPrivateKeyException extends Throwable {
  public InvalidatedPrivateKeyException(){
    super("The private key has been invalidated for the keystore or has been removed.");
  }
}
