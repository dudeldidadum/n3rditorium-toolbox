package de.n3rditorium.toolbox.list;

import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

import de.n3rditorium.toolbox.utils.SimpleAnimatorListener;

/**
 * Created by Kyp on 18.08.14.
 */
public class ItemRemover {

   protected HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
   protected ListView mListView;
   protected ArrayAdapter mAdapter;

   public ItemRemover(ListView listView) {
      this.mListView = listView;
      this.mAdapter = (ArrayAdapter) listView.getAdapter();
   }

   public void performRemove(View viewToRemove) {
      performRemoveWithAnimation(viewToRemove, null);
   }

   public void performRemoveWithAnimation(View viewToRemove, ViewPropertyAnimator animator) {
      checkFields();
      final int position = getPositionForView(viewToRemove);
      if (animator == null) {
         remove(position);
         return;
      }
      animator.setListener(new SimpleAnimatorListener() {
         @Override
         public void onAnimationEnd(Animator animation) {
            remove(position);
         }
      });
      animator.start();
   }

   private void remove(int position) {
      mAdapter.remove(mAdapter.getItem(position));
      final ViewTreeObserver observer = mListView.getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

         @Override
         public boolean onPreDraw() {
            observer.removeOnPreDrawListener(this);
            boolean firstAnimation = true;
            int firstVisiblePositon = mListView.getFirstVisiblePosition();
            for (int i = 0; i < mListView.getChildCount(); ++i) {
               prepareChildren(firstAnimation, firstVisiblePositon, i);
            }
            mItemIdTopMap.clear();
            return true;
         }
      });
   }

   private boolean prepareChildren(boolean firstAnimation, int firstVisiblePositon, int i) {
      View child = mListView.getChildAt(i);
      int position = firstVisiblePositon + i;
      long itemId = mAdapter.getItemId(position);
      Integer startTop = mItemIdTopMap.get(itemId);
      int top = child.getTop();

      if (startTop != null) {
         if (startTop != top) {
            int delta = startTop - top;
            child.setTranslationY(delta);
            child.animate().setDuration(300).translationY(0);

            if (firstAnimation) {
               firstAnimation = handleFirstAnimation(child);
            }
         }
      } else {
         int childHeight = child.getHeight() + mListView.getDividerHeight();
         startTop = top + (i > 0 ? childHeight : -childHeight);
         int delta = startTop - top;
         child.setTranslationY(delta);
         child.animate().setDuration(300).translationY(0);

         if (firstAnimation) {
            firstAnimation = handleFirstAnimation(child);
         }
      }
      return firstAnimation;
   }

   private boolean handleFirstAnimation(View child) {
      child.animate().setListener(new SimpleAnimatorListener() {
         @Override
         public void onAnimationEnd(Animator animation) {
            mListView.setEnabled(true);
         }
      });
      return false;
   }

   private int getPositionForView(View viewToRemove) {
      mListView.setEnabled(false);
      int firstVisiblePositon = mListView.getFirstVisiblePosition();
      for (int i = 0; i < mListView.getChildCount(); ++i) {
         View child = mListView.getChildAt(i);
         if (child != viewToRemove) {
            int position = firstVisiblePositon + i;
            long itemId = mAdapter.getItemId(position);
            mItemIdTopMap.put(itemId, child.getTop());
         }
      }
      return mListView.getPositionForView(viewToRemove);
   }

   public void performRemoveWithAnimation(View viewToRemove) {
      ViewPropertyAnimator defaultAnimator =
            viewToRemove.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f);
      performRemoveWithAnimation(viewToRemove, defaultAnimator);
   }

   protected void checkFields() {
      if (mListView == null) {
         throw new IllegalArgumentException("ListView not bound");
      }
      if (mAdapter == null) {
         throw new IllegalArgumentException("ListView does not have an an Adapter");
      }
   }
}
