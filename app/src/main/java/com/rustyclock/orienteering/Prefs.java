package com.rustyclock.orienteering;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Mateusz Jablonski
 * on 2015-06-10.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Prefs {

    @DefaultString(value = "", keyRes = R.string.pref_phone_no)
    String phoneNo();

}
