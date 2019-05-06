package weshare.groupfour.derek.goods;


public interface ItemTouchHelperAdapter {
    // 資料移動
    boolean onItemMove(int fromPosition, int toPosition);

    // 資料刪除
    void onItemDismiss(int position);


}
