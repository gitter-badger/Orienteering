package com.rustyclock.orienteering;

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
import org.androidannotations.annotations.OrmLiteDao;


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

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(prefs.phoneNo().get(), null, cp.getSmsMesssage(), null, null);
            Snackbar.make(findViewById(android.R.id.content), R.string.sms_sent, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), R.string.sms_sending_failed, Snackbar.LENGTH_LONG).show();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
}
