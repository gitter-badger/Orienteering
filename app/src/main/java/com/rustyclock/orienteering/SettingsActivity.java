package com.rustyclock.orienteering;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.rustyclock.orienteering.custom.ToolbarActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-28.
 */

@EActivity(R.layout.activity_settings)
@OptionsMenu(R.menu.settings)
public class SettingsActivity extends ToolbarActivity {

    @ViewById(R.id.et_phone) EditText etPhone;

    @Pref Prefs_ prefs;

    @AfterViews
    void afterViews() {
        setupToolbar(true);
        etPhone.setText(prefs.phoneNo().getOr(""));
    }

    @OptionsItem(R.id.action_save)
    void save() {
        prefs.edit().phoneNo().put(etPhone.getText().toString()).apply();
        finish();
    }
}
