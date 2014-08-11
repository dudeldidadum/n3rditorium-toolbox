package de.n3rditorium.toolbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    protected HashMap<Long, Integer> itemIdTopMap = new HashMap<Long, Integer>();
    protected StableArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        ListView listView = (ListView) findViewById(R.id.listview);

        final List<String> items = new ArrayList<String>();
        for (int i = 0; i < 100; ++i) {
            items.add("Item " + (i + 1));

        }
        adapter = new StableArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                animateRemoval((ListView) adapterView, view);
            }
        });
    }

    private void animateRemoval(final ListView listView, View viewToRemove) {
        listView.setEnabled(false);
        int firstVisiblePositon = listView.getFirstVisiblePosition();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            View child = listView.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePositon + i;
                long itemId = adapter.getItemId(position);
                itemIdTopMap.put(itemId, child.getTop());
            }
        }
        int position = listView.getPositionForView(viewToRemove);
        adapter.remove(adapter.getItem(position));

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;

                int firstVisiblePositon = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i) {
                    View child = listView.getChildAt(i);
                    int position = firstVisiblePositon + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = itemIdTopMap.get(itemId);
                    int top = child.getTop();

                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(300).translationY(0);

                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        if (startTop == null) {
                            int childHeight = child.getHeight() + listView.getDividerHeight();
                            startTop = top + (i > 0 ? childHeight : -childHeight);
                        }
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(300).translationY(0);

                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }

                }
                itemIdTopMap.clear();
                return true;
            }
        });


    }


}
