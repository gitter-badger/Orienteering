package com.rustyclock.orienteering;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.j256.ormlite.dao.Dao;
import com.rustyclock.orienteering.custom.ToolbarActivity;
import com.rustyclock.orienteering.db.DbHelper;
import com.rustyclock.orienteering.model.Checkpoint;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.ormlite.annotations.OrmLiteDao;


@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends ToolbarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    Prefs_ prefs;

    @OrmLiteDao(helper = DbHelper.class)
    Dao<Checkpoint, Integer> checkpointsDao;

    @AfterViews
    void afterViews() {
        setupToolbar(false);
        setToolbarIcon(R.mipmap.ic_launcher);
    }

    @Click(R.id.btn_scan)
    void scan() {
        String phoneNo = prefs.phoneNo().get();
        if(TextUtils.isEmpty(phoneNo)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.fill_up_settings, Snackbar.LENGTH_LONG).show();
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.initiateScan();
    }

    @Click(R.id.btn_history)
    void history() {
        HistoryActivity_.intent(this).start();
    }

    @OptionsItem(R.id.action_settings)
    void settings() {
        SettingsActivity_.intent(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = new Prefs_(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK)
            return;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result==null)
            return;

        Checkpoint cp = new Checkpoint(result.getContents());

        try {
            checkpointsDao.create(cp);
            sendCheckpointSMS(cp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendCheckpointSMS(Checkpoint cp) {

        Intent sentIntent = new Intent(SmsStatusReceiver.SENT_INTENT);
        sentIntent.putExtra(SmsStatusReceiver.EXTRA_ID_SENT, cp.getDbId());

        Intent deliveryIntent = new Intent(SmsStatusReceiver.DELIVERED_INTENT);
        deliveryIntent.putExtra(SmsStatusReceiver.EXTRA_ID_DELIVER, cp.getDbId());

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            cp.setStatus(Checkpoint.STATUS_SENDING);
            checkpointsDao.update(cp);

            Log.d(TAG, "Sending SMS of checkpoint" + cp.getDbId());

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(prefs.phoneNo().get(), null, cp.getSmsMesssage(), sentPI, deliveredPI);
        } catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), R.string.sms_sending_failed, Snackbar.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
}
