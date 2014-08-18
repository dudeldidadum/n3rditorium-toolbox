package de.n3rditorium.toolbox.list;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fetze_000 on 11.08.2014.
 */
public class StableArrayAdapter<T> extends ArrayAdapter<T> {

   protected HashMap<T, Integer> idMap = new HashMap<T, Integer>();

   public StableArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
         idMap.put(objects.get(i), i);
      }
   }

   @Override
   public boolean hasStableIds() {
      return true;
   }

   @Override
   public long getItemId(int position) {
      T item = getItem(position);
      return idMap.get(item);
   }
}
