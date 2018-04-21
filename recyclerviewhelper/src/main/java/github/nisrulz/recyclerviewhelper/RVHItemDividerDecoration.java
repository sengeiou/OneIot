package github.nisrulz.recyclerviewhelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * The type Rvh item divider decoration.
 */
public class RVHItemDividerDecoration extends RecyclerView.ItemDecoration {

  private static final int[] ATTRS = new int[] {
      android.R.attr.listDivider
  };

  /**
   * The constant HORIZONTAL_LIST.
   */
  public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

  /**
   * The constant VERTICAL_LIST.
   */
  public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

  private Drawable mDivider;

  private int mOrientation;

  /**
   * Instantiates a new Rvh item divider decoration.
   *
   * @param context the context
   * @param orientation the orientation
   */
  public RVHItemDividerDecoration(Context context, int orientation) {
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    mDivider = a.getDrawable(0);
    a.recycle();
    setOrientation(orientation);
  }

  /**
   * Sets orientation.
   *
   * @param orientation the orientation
   */
  public void setOrientation(int orientation) {
    if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
      throw new IllegalArgumentException("invalid orientation");
    }
    mOrientation = orientation;
  }

  @Override public void onDraw(Canvas c, RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  /**
   * Draw vertical.
   *
   * @param c the c
   * @param parent the parent
   */
  public void drawVertical(Canvas c, RecyclerView parent) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int top = child.getBottom() + params.bottomMargin +
          Math.round(ViewCompat.getTranslationY(child));
      final int bottom = top + mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  /**
   * Draw horizontal.
   *
   * @param c the c
   * @param parent the parent
   */
  public void drawHorizontal(Canvas c, RecyclerView parent) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getHeight() - parent.getPaddingBottom();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int left = child.getRight() + params.rightMargin +
          Math.round(ViewCompat.getTranslationX(child));
      final int right = left + mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);

    }
  }

  @Override public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
    if (mOrientation == VERTICAL_LIST) {
      outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    } else {
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
  }
}