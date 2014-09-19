package com.karumi.securepreference;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

/**
 *
 */
class StorePublicKey implements RSAPublicKey {


    private final String algorithm;
    private final String format;
    private final byte[] encoded;
    private final BigInteger publicExponent;
    private final BigInteger modulus;

    StorePublicKey(String algorithm, String format, byte[] encoded, BigInteger publicExponent,
                   BigInteger modulus) {
        this.algorithm = algorithm;
        this.format = format;
        this.encoded = encoded;
        this.publicExponent = publicExponent;
        this.modulus = modulus;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }

    @Override
    public BigInteger getPublicExponent() {
        return publicExponent;
    }

    @Override
    public BigInteger getModulus() {
        return modulus;
    }
}
