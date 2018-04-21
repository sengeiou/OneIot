package github.nisrulz.recyclerviewhelper;

/**
 * The interface Rvh view holder.
 */
public interface RVHViewHolder {

  /**
   * Called when the ItemTouchHelper first registers an item as being moved or swiped.
   * Implementations should update the item view to indicate it's active state.
   *
   * @param actionstate the actionstate
   */
  void onItemSelected(int actionstate);

  /**
   * Called when the ItemTouchHelper has completed the move or swipe, and the active item
   * state should be cleared.
   */
  void onItemClear();
}
