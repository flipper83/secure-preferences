package com.karumi.securepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.AndroidTestCase;
import com.karumi.securepreference.SecurePreference;
import com.karumi.securepreference.exception.InvalidatedPrivateKeyException;
import com.karumi.securepreference.exception.StoreValueException;
import java.util.HashSet;
import java.util.Set;

public class SecurePreferenceIntegrationTest extends AndroidTestCase {

  public static final String STORAGE_PREFERENCE = "ValueTest";
  public static final String INT_KEY = "intValue";
  public static final int INT_VALUE = 1;
  private static final String LONG_KEY = "longValue";
  private static final long LONG_VALUE = Long.MAX_VALUE;
  private static final String FLOAT_KEY = "floatValue";
  private static final float FLOAT_VALUE = 1.5f;
  private static final String STRING_KEY = "StringValue";
  private static final String STRING_VALUE = "value";
  private static final String STRING_VALUE_2 = "value2";
  private static final String STRING_SET_KEY = "StringSetValue";
  private static final String BOOL_KEY = "booleanValue";
  private static final boolean BOOL_VALUE = true;

  public void testStoreAndGetBoolean() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      securePreference.putBoolean(BOOL_KEY, BOOL_VALUE);
    } catch (StoreValueException e) {
      assertTrue(false);
    }
    boolean boolValue = false;
    try {
      boolValue = securePreference.getBoolean(BOOL_KEY, false);
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertEquals(BOOL_VALUE, boolValue);
  }

  public void testStoreAndGetInt() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      securePreference.putInt(INT_KEY, INT_VALUE);
    } catch (StoreValueException e) {
      assertTrue(false);
    }
    int intValue = 0;
    try {
      intValue = securePreference.getInt(INT_KEY, 0);
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertEquals(INT_VALUE, intValue);
  }

  public void testStoreAndGetLong() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      securePreference.putLong(LONG_KEY, LONG_VALUE);
    } catch (StoreValueException e) {
      assertTrue(false);
    }
    long longValue = 0;
    try {
      longValue = securePreference.getLong(LONG_KEY, 0);
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertEquals(LONG_VALUE, longValue);
  }

  public void testStoreAndGetFloat() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      securePreference.putFloat(FLOAT_KEY, FLOAT_VALUE);
    } catch (StoreValueException e) {
      assertTrue(false);
    }

    float floatValue = 0;
    try {
      floatValue = securePreference.getFloat(FLOAT_KEY, 0);
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertEquals(FLOAT_VALUE, floatValue);
  }

  public void testStoreAndGetString() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      securePreference.putString(STRING_KEY, STRING_VALUE);
    } catch (StoreValueException e) {
      assertTrue(false);
    }

    String stringValue = null;
    try {
      stringValue = securePreference.getString(STRING_KEY, "");
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertEquals(STRING_VALUE, stringValue);
  }

  public void testStoreAndGetSetString() {
    SharedPreferences preferencesValue = getContext().getSharedPreferences(STORAGE_PREFERENCE,
        Context.MODE_PRIVATE);
    SecurePreference securePreference = SecurePreference.with(getContext(), preferencesValue);
    try {
      Set<String> anySetString = new HashSet<String>();
      anySetString.add(STRING_VALUE);
      anySetString.add(STRING_VALUE_2);

      securePreference.putSetString(STRING_SET_KEY, anySetString);
    } catch (StoreValueException e) {
      assertTrue(false);
    }

    Set<String> setString = null;
    try {
      setString = securePreference.getSetString(STRING_SET_KEY);
    } catch (InvalidatedPrivateKeyException e) {
      assertTrue(false);
    }
    assertTrue(setString.contains(STRING_VALUE));
    assertTrue(setString.contains(STRING_VALUE_2));
  }
}