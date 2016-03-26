package com.rustyclock.orienteering;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;

/**
 * Created by Mateusz
 * 24.03.2016.
 */

@PreferenceScreen(R.xml.settings)
@EFragment
public class SettingsFragment extends PreferenceFragmentCompat {

    @PreferenceChange(R.string.pref_phone_no)
    void phoneChanged(EditTextPreference preference, String value) {
        preference.setSummary(value);
    }

    @PreferenceByKey(R.string.pref_phone_no)
    EditTextPreference phoneNo;

    @AfterPreferences
    void after() {
        String phone = phoneNo.getText();
        phoneNo.setSummary(TextUtils.isEmpty(phone) ? getString(R.string.judge_phone_hint): phoneNo.getText());
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        this.getPreferenceManager().setSharedPreferencesName("Prefs");
    }
}
