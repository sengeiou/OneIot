package github.nisrulz.recyclerviewhelper;

/**
 * Interface to notify a RecyclerView.Adapter of moving and dismissal event
 */
public interface RVHAdapter {

  /**
   * Called when an item has been dragged far enough to trigger a move. This is called every time
   * an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
   * <br/>
   * Implementations should call RecyclerView.Adapter#notifyItemMoved(int, int) after
   * adjusting the underlying data to reflect this move.
   *
   * @param fromPosition The start position of the moved item.
   * @param toPosition Then resolved position of the moved item.
   * @return True if the item was moved to the new adapter position.
   */
  boolean onItemMove(int fromPosition, int toPosition);

  /**
   * Called when an item has been dismissed by a swipe.<br/>
   * <br/>
   * Implementations should call RecyclerView.Adapter#notifyItemRemoved(int) after
   * adjusting the underlying data to reflect this removal.
   *
   * @param position The position of the item dismissed.
   * @param direction the direction
   */
  void onItemDismiss(int position, int direction);
}
