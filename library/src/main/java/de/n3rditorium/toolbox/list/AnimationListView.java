package de.n3rditorium.toolbox.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Kyp on 18.08.14.
 */
public class AnimationListView extends ListView {

   private ItemRemover mRemover;

   public AnimationListView(Context context) {
      super(context);
   }

   public AnimationListView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public AnimationListView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
   }

   @Override
   public void removeView(View viewToRemove) {
      if (mRemover == null) {
         mRemover = new ItemRemover(this);
      }
      mRemover.performRemove(viewToRemove);
   }
}
