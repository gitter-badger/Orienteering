package com.rustyclock.orienteering;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.rustyclock.orienteering.db.DbHelper;
import com.rustyclock.orienteering.model.Checkpoint;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.ReceiverAction;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;

@EReceiver
public class SmsStatusReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsStatusReceiver";

    public static final String SENT_INTENT = "com.rustyclock.orienteering.SMS_SENT";
    public static final String DELIVERED_INTENT = "com.rustyclock.orienteering.DELIVERED_SENT";

    public static final String EXTRA_ID_SENT = "id_sent";
    public static final String EXTRA_ID_DELIVER = "id_deliver";

    @OrmLiteDao(helper = DbHelper.class)
    Dao<Checkpoint, Integer> checkpointsDao;

    public SmsStatusReceiver() {
    }

    @ReceiverAction(actions = SENT_INTENT)
    void sentAction(@ReceiverAction.Extra(EXTRA_ID_SENT) int id, Context context) {

        int checkpointStatus = -1;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                checkpointStatus = Checkpoint.STATUS_SENT;
                Toast.makeText(context, R.string.sms_status_sent, Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                checkpointStatus = Checkpoint.STATUS_FAILED;
                Toast.makeText(context, R.string.sms_status_failure, Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                checkpointStatus = Checkpoint.STATUS_FAILED;
                Toast.makeText(context, R.string.sms_status_no_service, Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                checkpointStatus = Checkpoint.STATUS_FAILED;
                Toast.makeText(context, R.string.sms_status_pdu, Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                checkpointStatus = Checkpoint.STATUS_FAILED;
                Toast.makeText(context, R.string.sms_status_radio_off, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        updateCheckpointStatus(id, checkpointStatus);
    }

    @ReceiverAction(actions = DELIVERED_INTENT)
    void deliveryAction(@ReceiverAction.Extra(EXTRA_ID_DELIVER) int id, Context context) {

        int checkpointStatus = -1;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                checkpointStatus = Checkpoint.STATUS_DELIVERED;
                Toast.makeText(context, R.string.sms_status_delivered, Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, R.string.sms_status_no_delivery, Toast.LENGTH_SHORT).show();
                break;
        }

        updateCheckpointStatus(id, checkpointStatus);
    }

    private void updateCheckpointStatus(int id, int status) {
        if(status<0)
            return;

        try {
            Checkpoint checkpoint = checkpointsDao.queryForId(id);
            checkpoint.setStatus(status);
            checkpointsDao.update(checkpoint);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
