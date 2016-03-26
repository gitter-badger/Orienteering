package com.rustyclock.orienteering;

import com.rustyclock.orienteering.custom.ToolbarActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-28.
 */

@EActivity(R.layout.activity_settings)
@OptionsMenu(R.menu.settings)
public class SettingsActivity extends ToolbarActivity {

    @AfterViews
    void afterViews() {
        setupToolbar(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, SettingsFragment_.builder().build())
                .commit();
    }

    @OptionsItem(R.id.action_save)
    void save() {
        finish();
    }
}
