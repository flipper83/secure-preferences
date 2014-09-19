package com.karumi.securepreference;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 */
public interface ValueCipher {

    byte[] encrypt(byte[] bytesToEncrypt, PublicKey publicKey);
    byte[] decrypt(byte[] bytesToDecrypt, PrivateKey privateKey);
}
