package com.karumi.securepreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import com.karumi.securepreference.exception.InvalidatedPrivateKeyException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

/**
 *
 */
 class KeyProviderKeystore implements KeyProvider {

    private static final String ALGORITHM = "algorithm";
    private static final String FORMAT = "format";
    private static final String BASE64_ENCODED = "encoded";
    private static final String LOGTAG = "KeyProviderKeystore";
    public static final String SECURE_STORAGE = "secureStorage";
    private static final String MODULUS = "modulus";
    private static final String PUBLIC_EXPONENT = "publicExponent";
    private final SharedPreferences preferences;
    private final Context context;

    public KeyProviderKeystore(SharedPreferences preferences, Context context) {
        this.preferences = preferences;
        this.context = context;
    }

    @Override
    public PublicKey providePublicKey() {
        PublicKey publicKey = obtainStorePublicKey();

        if (publicKey == null) {
            publicKey = generateRsaPairWithGenerator(SECURE_STORAGE);

            if (publicKey == null) {
                throw new IllegalStateException("The public key generation fails, " +
                        "we can sign the data");
            } else {
                storePublicKey((RSAPublicKey) publicKey);
            }
        }
        return publicKey;
    }

    private void storePublicKey(RSAPublicKey publicKey) {
        SharedPreferences.Editor editor = preferences.edit();

        byte[] encoded = publicKey.getEncoded();
        String base64Encoded = Base64.encodeToString(encoded, Base64.DEFAULT);

        editor.putString(ALGORITHM, publicKey.getAlgorithm());
        editor.putString(FORMAT, publicKey.getFormat());
        editor.putString(BASE64_ENCODED, base64Encoded);
        editor.putString(MODULUS, publicKey.getModulus().toString());
        editor.putString(PUBLIC_EXPONENT, publicKey.getPublicExponent().toString());


        editor.apply();
    }

    private PublicKey obtainStorePublicKey() {

        String algorithm = preferences.getString(ALGORITHM, null);
        String format = preferences.getString(FORMAT, null);
        String base64_encoded = preferences.getString(BASE64_ENCODED, null);

        if (algorithm == null || format == null || base64_encoded == null) {
            return null;
        } else {
            BigInteger modulus = new BigInteger(preferences.getString(MODULUS, ""));
            BigInteger publicExponent = new BigInteger(preferences.getString(PUBLIC_EXPONENT, ""));

            byte[] encoded = Base64.decode(base64_encoded, Base64.DEFAULT);

            PublicKey publicKey = new StorePublicKey(algorithm, format, encoded, publicExponent,
                    modulus);
            return publicKey;
        }
    }

    @Override
    public PrivateKey providePrivateKey() throws InvalidatedPrivateKeyException {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("AndroidKeyStore");

            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(SECURE_STORAGE, null);

            if (entry instanceof KeyStore.PrivateKeyEntry) {
                return ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            } else {
              Log.d(LOGTAG, "Not an instance of a PrivateKeyEntry");
              throw new InvalidatedPrivateKeyException();
            }

        } catch (KeyStoreException e) {
            Log.d(LOGTAG, "Error obtaining the private key", e);
        } catch (CertificateException e) {
            Log.d(LOGTAG, "Error obtaining the private key", e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(LOGTAG, "Error obtaining the private key", e);
        } catch (UnrecoverableEntryException e) {
            Log.d(LOGTAG, "Error obtaining the private key", e);
        } catch (IOException e) {
            Log.d(LOGTAG, "Error obtaining the private key", e);
        }

        return null;
    }

    @SuppressLint("NewApi")
    public PublicKey generateRsaPairWithGenerator(String alias) {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date end = cal.getTime();
        PublicKey publicKey = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            kpg.initialize(new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setStartDate(now)
                    .setEndDate(end)
                    .setSerialNumber(BigInteger.valueOf(1))
                    .setSubject(new X500Principal("CN=test1"))
                    .build());

            KeyPair kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
        } catch (InvalidAlgorithmParameterException e) {
            Log.d(LOGTAG, "Error generate keys on keystore", e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(LOGTAG, "Error generate keys on keystore", e);
        } catch (NoSuchProviderException e) {
            Log.d(LOGTAG, "Error generate keys on keystore", e);
        }
        return publicKey;
    }
}
