package com.rustyclock.orienteering.custom;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rustyclock.orienteering.R;

/**
 * Created by Mateusz Jablonski
 * on 2015-06-10.
 */
public class ToolbarActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected void setupToolbar(boolean navigationBack) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(navigationBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setToolbarIcon(int res) {
        toolbar.setNavigationIcon(res);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
