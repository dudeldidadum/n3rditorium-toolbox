package de.n3rditorium.toolbox.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import de.n3rditorium.toolbox.list.AnimationListView;
import de.n3rditorium.toolbox.list.StableArrayAdapter;

public class SampleActivity extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sample);

      final List<String> items = new ArrayList<String>();
      for (int i = 0; i < 100; ++i) {
         items.add("Item " + (i + 1));
      }

      final AnimationListView listView = (AnimationListView) findViewById(R.id.listview);
      listView.setAdapter(
            new StableArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            listView.removeView(view);
         }
      });
   }
}
