package com.karumi.securepreference;

import com.karumi.securepreference.exception.InvalidatedPrivateKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 */
public interface KeyProvider {
    PublicKey providePublicKey();
    PrivateKey providePrivateKey() throws InvalidatedPrivateKeyException;
}
