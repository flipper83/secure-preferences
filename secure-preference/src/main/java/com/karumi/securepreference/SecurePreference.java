package com.karumi.securepreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import com.karumi.securepreference.exception.InvalidatedPrivateKeyException;
import com.karumi.securepreference.exception.StoreValueException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SecurePreference {

  private static final String LOGTAG = "SecurePreference";
  private final SharedPreferences sharedPreferences;
  private final ValueCipher cipher;
  private final KeyProvider keyProvider;

  private SecurePreference(SharedPreferences sharedPreferences, ValueCipher cipher,
      KeyProvider keyProvider) {
    this.sharedPreferences = sharedPreferences;
    this.cipher = cipher;
    this.keyProvider = keyProvider;
  }

  public void putString(String key, String value) throws StoreValueException {
    byte[] valueBytes = value.getBytes();
    putBytes(key, valueBytes);
  }

  public void putSetString(String key, Set<String> value) throws StoreValueException {
    Set<String> encryptedSet = new HashSet<String>();
    for (String stringValue : value) {
      byte[] valueBytes = stringValue.getBytes();
      PublicKey publicKey = keyProvider.providePublicKey();
      byte[] encriptedValue = cipher.encrypt(valueBytes, publicKey);
      String base64Encripted = Base64.encodeToString(encriptedValue, Base64.DEFAULT);
      encryptedSet.add(base64Encripted);
    }
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putStringSet(key, encryptedSet);
    editor.apply();
  }

  public void putInt(String key, int value) throws StoreValueException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    try {
      dos.writeInt(value);
    } catch (IOException e) {
      StoreValueException storeValueException = new StoreValueException();
      storeValueException.initCause(e);
    }
    putBytes(key, baos.toByteArray());
  }

  public void putLong(String key, long value) throws StoreValueException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    try {
      dos.writeLong(value);
    } catch (IOException e) {
      StoreValueException storeValueException = new StoreValueException();
      storeValueException.initCause(e);
    }
    putBytes(key, baos.toByteArray());
  }

  public void putFloat(String key, float value) throws StoreValueException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    try {
      dos.writeFloat(value);
    } catch (IOException e) {
      StoreValueException storeValueException = new StoreValueException();
      storeValueException.initCause(e);
    }
    putBytes(key, baos.toByteArray());
  }

  public void putBoolean(String key, boolean value) throws StoreValueException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    try {
      dos.writeBoolean(value);
    } catch (IOException e) {
      StoreValueException storeValueException = new StoreValueException();
      storeValueException.initCause(e);
    }
    putBytes(key, baos.toByteArray());
  }

  private void putBytes(String key, byte[] valueBytes) {
    PublicKey publicKey = keyProvider.providePublicKey();
    byte[] encriptedValue = cipher.encrypt(valueBytes, publicKey);
    storeEncriptedValue(key, encriptedValue);
  }

  public String getString(String key, String defaultValue) throws InvalidatedPrivateKeyException {
    byte[] bytes = getBytes(key);
    if (bytes == null) {
      return defaultValue;
    }
    String value = new String(bytes);
    return value;
  }

  public Set<String> getSetString(String key) throws InvalidatedPrivateKeyException {
    Set<String> valueSet = new HashSet<String>();

    Set<String> encryptedSet = sharedPreferences.getStringSet(key, null);
    if (encryptedSet == null) {
      return null;
    }

    for (String base64Encrypted : encryptedSet) {
      byte[] encriptedValue = Base64.decode(base64Encrypted, Base64.DEFAULT);
      PrivateKey privateKey = keyProvider.providePrivateKey();

      if (privateKey == null) {
        return null;
      }

      byte[] valueBytes = cipher.decrypt(encriptedValue, privateKey);
      String value = new String(valueBytes);
      valueSet.add(value);
    }

    return valueSet;
  }

  public int getInt(String key, int defaultValue) throws InvalidatedPrivateKeyException {
    DataInputStream dis = getDataInputStream(key);
    if (dis == null) {
      return defaultValue;
    }

    int intValue = defaultValue;
    try {
      intValue = dis.readInt();
    } catch (IOException e) {
      Log.d(LOGTAG, "error parsing int", e);
    }

    return intValue;
  }

  public long getLong(String key, long defaultValue) throws InvalidatedPrivateKeyException {
    DataInputStream dis = getDataInputStream(key);
    if (dis == null) {
      return defaultValue;
    }

    long longValue = defaultValue;
    try {
      longValue = dis.readLong();
    } catch (IOException e) {
      Log.d(LOGTAG, "error parsing int", e);
    }

    return longValue;
  }

  public float getFloat(String key, float defaultValue) throws InvalidatedPrivateKeyException {
    DataInputStream dis = getDataInputStream(key);
    if (dis == null) {
      return defaultValue;
    }

    float floatValue = defaultValue;
    try {
      floatValue = dis.readFloat();
    } catch (IOException e) {
      Log.d(LOGTAG, "error parsing int", e);
    }

    return floatValue;
  }

  public boolean getBoolean(String key, boolean defaultValue)
      throws InvalidatedPrivateKeyException {
    DataInputStream dis = getDataInputStream(key);
    if (dis == null) {
      return defaultValue;
    }

    boolean floatValue = defaultValue;
    try {
      floatValue = dis.readBoolean();
    } catch (IOException e) {
      Log.d(LOGTAG, "error parsing int", e);
    }

    return floatValue;
  }

  private DataInputStream getDataInputStream(String key) throws InvalidatedPrivateKeyException {
    byte[] valueBytes = getBytes(key);

    if (valueBytes != null) {
      ByteArrayInputStream bais = new ByteArrayInputStream(valueBytes);
      DataInputStream dis = new DataInputStream(bais);
      return dis;
    }

    return null;
  }

  private byte[] getBytes(String key) throws InvalidatedPrivateKeyException {
    byte[] encriptedValue = obtainEncryptedValue(key);
    if (encriptedValue == null) {
      return null;
    }

    PrivateKey privateKey = keyProvider.providePrivateKey();

    if (privateKey == null) {
      return null;
    }

    byte[] valueBytes = cipher.decrypt(encriptedValue, privateKey);

    return valueBytes;
  }

  private byte[] obtainEncryptedValue(String key) {
    String base64Encrypted = sharedPreferences.getString(key, null);
    if (base64Encrypted != null) {
      return Base64.decode(base64Encrypted, Base64.DEFAULT);
    } else {
      return null;
    }
  }

  private void storeEncriptedValue(String key, byte[] encriptedValue) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    String base64Encripted = Base64.encodeToString(encriptedValue, Base64.DEFAULT);
    editor.putString(key, base64Encripted);
    editor.apply();
  }

  private static SecurePreference singleton;

  public static SecurePreference with(Context context, SharedPreferences sharedPreferences) {
    if (singleton == null) {
      synchronized (SecurePreference.class) {
        singleton = new Builder(context, sharedPreferences).build();
      }
    }
    return singleton;
  }

  private static class Builder {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesForPublicKey;
    private ValueCipher valueCipher;
    private KeyProvider keyProvider;
    private Context context;

    public Builder(Context context, SharedPreferences sharedPreferences) {
      this.sharedPreferences = sharedPreferences;
      this.context = context;
    }

    public Builder valueCipher(ValueCipher valueCipher) {
      this.valueCipher = valueCipher;
      return this;
    }

    public Builder keyProvider(KeyProvider keyProvider) {
      this.keyProvider = keyProvider;
      return this;
    }

    public SecurePreference build() {
      if (valueCipher == null) {
        valueCipher = new RsaPkcs1Cipher();
      }

      if (keyProvider == null) {
        if (sharedPreferencesForPublicKey == null) {
          sharedPreferencesForPublicKey =
              context.getSharedPreferences("Publickey", Context.MODE_PRIVATE);
        }
        keyProvider = new KeyProviderKeystore(sharedPreferences, context);
      }

      return new SecurePreference(sharedPreferences, valueCipher, keyProvider);
    }
  }
}
