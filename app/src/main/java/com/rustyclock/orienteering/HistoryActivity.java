package com.rustyclock.orienteering;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rustyclock.orienteering.model.Checkpoint;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Mateusz Jablonski
 * on 2015-05-27.
 */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Realm realm = Realm.getInstance(this);
        RealmQuery<Checkpoint> query = realm.where(Checkpoint.class);
        RealmResults<Checkpoint> result = query.findAll();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new HistoryAdapter(result));
    }

    private class HistoryAdapter extends ArrayAdapter<Checkpoint> {

        public HistoryAdapter(List<Checkpoint> checkpoints) {
            super(HistoryActivity.this, android.R.layout.simple_list_item_2, checkpoints);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) view.findViewById(android.R.id.text2);

            Checkpoint cp = getItem(position);

            tv1.setText(cp.getCheckpointId() + " - " + cp.getCheckpointCode());
            tv2.setText(cp.getScanDate().toString());

            return view;
        }
    }

}
