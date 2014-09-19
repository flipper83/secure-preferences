package com.karumi.securepreferences.demo;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.karumi.securepreference.SecurePreference;
import com.karumi.securepreference.exception.InvalidatedPrivateKeyException;
import com.karumi.securepreference.exception.StoreValueException;

public class MainActivity extends ActionBarActivity {

  private static final String LOGTAG = "MainActivity";
  private EditText etStore;
  private Button btStore;
  private Button btLoad;
  private TextView tvStore;
  private SecurePreference uuiDataSourceKeyStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mapUi();
    hookListeners();
  }

  private void hookListeners() {
    btStore.setOnClickListener(btStoreClickListener);
    btLoad.setOnClickListener(btLoadClickListener);
  }

  private void mapUi() {
    etStore = ((EditText) findViewById(R.id.et_store));
    btStore = ((Button) findViewById(R.id.bt_store));
    btLoad = ((Button) findViewById(R.id.bt_load));
    tvStore = ((TextView) findViewById(R.id.tv_store));
  }

  private View.OnClickListener btStoreClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      String value = etStore.getText().toString();
      if ((value != null) && (!value.equals(""))) {
        StoreAsync storeAsync = new StoreAsync();
        storeAsync.execute(value);
      }
    }
  };

  private View.OnClickListener btLoadClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (uuiDataSourceKeyStore == null) {
        inituuiDataSource();
      }

      LoadAsync loadAsync = new LoadAsync();
      loadAsync.execute();
    }
  };

  private void inituuiDataSource() {
    SharedPreferences preferencesValue = getSharedPreferences("ValueTest", MODE_PRIVATE);
    uuiDataSourceKeyStore = SecurePreference.with(this, preferencesValue);
  }

  class StoreAsync extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
      if (uuiDataSourceKeyStore == null) {
        inituuiDataSource();
      }

      try {
        uuiDataSourceKeyStore.putString("TestValue", params[0]);
      } catch (StoreValueException e) {
        Log.d(LOGTAG, "error storing values", e);
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      Toast.makeText(MainActivity.this, getString(R.string.data_store), Toast.LENGTH_SHORT).show();
    }
  };

  class LoadAsync extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
      if (uuiDataSourceKeyStore == null) {
        inituuiDataSource();
      }

      String value = null;
      try {
        value = uuiDataSourceKeyStore.getString("TestValue", getString(R.string.emptyValue));
      } catch (InvalidatedPrivateKeyException e) {
        Log.d(LOGTAG, "error loading private key", e);
        value = "ERROR INVALIDATED PRIVATE KEY";
      }

      return value;
    }

    @Override
    protected void onPostExecute(String value) {
      tvStore.setText(value);
    }
  };
}
