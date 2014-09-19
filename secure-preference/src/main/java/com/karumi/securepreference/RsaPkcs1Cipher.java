package com.karumi.securepreference;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 */
 class RsaPkcs1Cipher implements ValueCipher {
    private static final String LOGTAG = "RsaPkcs1Cipher";

    public RsaPkcs1Cipher(){ }

    @Override
    public byte[] encrypt(byte[] bytesToEncrypt, PublicKey publicKey) {
        Cipher cipherC = null;
        try {
            cipherC = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            cipherC.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipherC.doFinal(bytesToEncrypt);

        } catch (NoSuchAlgorithmException e) {
            Log.d(LOGTAG, "error encrypting", e);
        } catch (NoSuchProviderException e) {
            Log.d(LOGTAG, "error encrypting", e);
        } catch (NoSuchPaddingException e) {
            Log.d(LOGTAG, "error encrypting", e);
        } catch (BadPaddingException e) {
            Log.d(LOGTAG, "error encrypting", e);
        } catch (IllegalBlockSizeException e) {
            Log.d(LOGTAG, "error encrypting", e);
        } catch (InvalidKeyException e) {
            Log.d(LOGTAG, "error encrypting", e);
        }

        throw new IllegalStateException("An error happens trying to encrypt the data.");
    }

    @Override
    public byte[] decrypt(byte[] bytesToDecrypt, PrivateKey privateKey) {
        // decrypts the message
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(bytesToDecrypt);

        } catch (NoSuchAlgorithmException e) {
            Log.d(LOGTAG, "error decrypting", e);
        } catch (NoSuchProviderException e) {
            Log.d(LOGTAG, "error decrypting", e);
        } catch (NoSuchPaddingException e) {
            Log.d(LOGTAG, "error decrypting", e);
        } catch (IllegalBlockSizeException e) {
            Log.d(LOGTAG, "error decrypting", e);
        } catch (BadPaddingException e) {
            Log.d(LOGTAG, "error decrypting", e);
        } catch (InvalidKeyException e) {
            Log.d(LOGTAG, "error decrypting", e);
        }

        throw new IllegalStateException("An error happens trying to decrypt the data.");
        //((KeyStore.PrivateKeyEntry) entry).getPrivateKey()
        //return Base64.encodeToString(dectyptedText, Base64.DEFAULT);

    }
}
