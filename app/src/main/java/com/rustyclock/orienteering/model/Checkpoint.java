package com.rustyclock.orienteering.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.field.DatabaseField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-27.
 */


public class Checkpoint implements Comparable<Checkpoint> {

    public static final String SPLITTER = ":";

    @DatabaseField(generatedId = true)
    int dbId;

    @DatabaseField
    private String scannedData;

    @DatabaseField
    private Date scanDate;

    @DatabaseField
    private boolean local;

    public Checkpoint() {
    }

    public Checkpoint(String data) {
        this.scannedData = data;

        this.scanDate = new Date();
        this.local = true;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public String getFormattedQr() {
        String s = scannedData;
        if(scannedData.contains(SPLITTER))
            s = scannedData.replace(SPLITTER, " - ");

        return s;
    }

    public String getFormattedDate() {
        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd", new Locale("pl_PL"));
        return writeFormat.format(scanDate);
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getScanHour() {
        DateFormat writeFormat = new SimpleDateFormat("HH:mm", new Locale("pl_PL"));
        return writeFormat.format(scanDate);
    }

    public void displayChekpointData(TextView checkpoint, TextView code) {
        if(scannedData.contains(SPLITTER)) {
            String[] splits = scannedData.split(SPLITTER);
            checkpoint.setText(splits[0]);
            code.setText(splits[1]);
        } else {
            checkpoint.setText(scannedData);
            code.setVisibility(View.GONE);
        }
    }

    public void displayDate(TextView date, TextView hour) {
        String[] splits = getFormattedDate().split(" ");
        hour.setText(splits[0]);
        date.setText(splits[1]);
    }

    public String getSmsMesssage() {

        String checkPointCode = scannedData;

        if(scannedData.contains(SPLITTER)) {
            String[] splits = scannedData.split(SPLITTER);
            if(splits.length>1)
                checkPointCode = splits[1];
        }

        return checkPointCode + " " + getScanHour();
    }

    @Override
    public int compareTo(@NonNull Checkpoint ch) {
        return ch.getScanDate().compareTo(scanDate);
    }
}
