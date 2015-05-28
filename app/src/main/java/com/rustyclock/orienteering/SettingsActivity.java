package com.rustyclock.orienteering;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-28.
 */
public class SettingsActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etNumber;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etNumber = (EditText) findViewById(R.id.et_competitor);

        etPhone.setText(preferences.getString("phone", ""));
        etNumber.setText(preferences.getString("competitorNo", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            preferences.edit()
                    .putString("phone", etPhone.getText().toString())
                    .putString("competitorNo", etNumber.getText().toString())
                    .apply();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
