package com.rustyclock.orienteering.model;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-27.
 */
public class Checkpoint extends RealmObject {

    public static final String URL_SCHEME = "checkpoint://";
    public static final String SPLITTER = ":";

    @PrimaryKey
    private String checkpointId;

    private String checkpointCode;
    private String scannedData;

    private Date scanDate;

    private boolean local;

    public Checkpoint() {
    }

    public Checkpoint(String data) {
        this.scannedData = data.replace(URL_SCHEME, "");

        this.scanDate = new Date();
        this.local = true;

        if(TextUtils.isEmpty(data))
            return;

        String[] pointParams = scannedData.split(SPLITTER);
        checkpointId = "01" + SPLITTER + pointParams[0];
        checkpointCode = pointParams[1];
    }

    public String getCheckpointId() {
        return "PK" + checkpointId.split(SPLITTER)[1];
    }

    public String getCheckpointCode() {
        return checkpointCode;
    }

    public String getScannedData() {
        return scannedData;
    }

    public void setScannedData(String scannedData) {
        this.scannedData = scannedData;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public void setCheckpointId(String checkpointId) {
        this.checkpointId = checkpointId;
    }

    public void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }
}
