secure-preferences
==================

Secure preferences is a small wrapper around AndroidShared preferences. The difference is
secure preferences store the data encrypted, with a RsaPkcs1 cipher, instead of plain text. This wrapper obtain the public and private key for the internal Android keystore and encrypt with them.

These encrypted preferences are slower than normal sharedpreferences and also consume more storage size on the shared preferences. I strongly recommend you to use it only to store sensible information.
 
Limitations
-----------
This first version is only compatible with **api-18** and higher.
Methods getAll and listeners are not migrated now. But it will be soon.

Bugs & know Issues
--------------

In some handsets I founded a bug when the user change the phone PIN, because the keystore is signed
with the pin and it cannot recover the private key. If you want to test it please follow the following 
steps:

1. Run the demo app and write some info on the box to store.
2. Click on store button, a toast must appear to notify that the data has been saved.
3. Click load button and check that the info is correct.
4. Close the app from App manager, clicking on Force close to invalidate sharedpreferences caches.
5. Go to setting/security/Screen Lock and change it to Pin, and change to new a Pin.
6. Go back to the app and click on load button. it's the info correct or empty storage appear(buggy case).

I cannot find a fix or work around for this problem, Secure preferences throw an InvalidatePrivateKeyException, and the user must manage the invalidate private key, to store again the information.

Other solution for this bug, it's set the app inside the system domain, you need add to your
 manifest the property android:sharedUserId="android.uid.system"

How to use
----------
Init the securePreferences:

```java
  SharedPreferences preferencesValue = getSharedPreferences("ValueTest", MODE_PRIVATE);
  SecurePreference uuiDataSourceKeyStore = SecurePreference.with(this, preferencesValue);
```
Storing data:

```java
  try {
    uuiDataSourceKeyStore.putString("TestValue", params[0]);
  } catch (StoreValueException e) {
    Log.d(LOGTAG, "error storing values", e);
  }
```

Loading data:

```java
   String value = null;
   try {
     value = uuiDataSourceKeyStore.getString("TestValue", getString(R.string.emptyValue));
   } catch (InvalidatedPrivateKeyException e) {
     Log.d(LOGTAG, "error loading private key", e);
   }
```

Download
--------

License
-------
