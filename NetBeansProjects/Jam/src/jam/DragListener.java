package jam;

public interface DragListener {
    void onDragged();
    void onMoved(int dragX, int dragY);
    void onDropped(int dragX, int dragY);
}