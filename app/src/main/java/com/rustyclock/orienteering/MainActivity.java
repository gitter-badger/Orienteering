package com.rustyclock.orienteering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rustyclock.orienteering.model.Checkpoint;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.initiateScan();
            }
        });

        findViewById(R.id.btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK)
            return;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result==null)
            return;

        Checkpoint cp = new Checkpoint(result.getContents());
        Realm realm = Realm.getInstance(this);

        try {
            realm.beginTransaction();
            realm.copyToRealm(cp);
            realm.commitTransaction();

            sendCheckpointSMS(cp);

        } catch (Exception e) {
            realm.cancelTransaction();
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendCheckpointSMS(Checkpoint cp) {

        String phoneNo = preferences.getString("phone", "");
        String competitor = preferences.getString("competitorNo", "");
        if(TextUtils.isEmpty(phoneNo) || TextUtils.isEmpty(competitor)) {
            Toast.makeText(this, "Uzupełnij dane w ustawieniach", Toast.LENGTH_LONG).show();
            return;
        }

        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = writeFormat.format(cp.getScanDate());

        String message = competitor;
        message += Checkpoint.SPLITTER + cp.getCheckpointId();
        message += Checkpoint.SPLITTER + cp.getCheckpointCode();
        message += Checkpoint.SPLITTER + date;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS wysłano.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Wysyłka SMS nieudana",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
