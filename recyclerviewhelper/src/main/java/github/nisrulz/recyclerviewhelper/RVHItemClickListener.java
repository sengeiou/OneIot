package github.nisrulz.recyclerviewhelper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * The type Rvh item click listener.
 */
public class RVHItemClickListener implements RecyclerView.OnItemTouchListener {
  private OnItemClickListener mListener;

  /**
   * The interface On item click listener.
   */
  public interface OnItemClickListener {
    /**
     * On item click.
     *
     * @param view the view
     * @param position the position
     */
    void onItemClick(View view, int position);
  }

  /**
   * The M gesture detector.
   */
  GestureDetector mGestureDetector;

  /**
   * Instantiates a new Rvh item click listener.
   *
   * @param context the context
   * @param listener the listener
   */
  public RVHItemClickListener(Context context, OnItemClickListener listener) {
    mListener = listener;
    mGestureDetector = new GestureDetector(context,
            new GestureDetector.SimpleOnGestureListener() {
      @Override public boolean onSingleTapUp(MotionEvent e) {
        return true;
      }
    });
  }

  @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
      mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
      return true;
    }
    return false;
  }

  @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    // Do nothing
  }

  @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    // Do nothings
  }
}
