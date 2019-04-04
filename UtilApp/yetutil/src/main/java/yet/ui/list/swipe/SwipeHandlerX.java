package yet.ui.list.swipe;

import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

public class SwipeHandlerX implements OnTouchListener {
    private ListView listView = null;
    private boolean enable = true;
    private GestureDetector gd;
    private XSwipeItemView curView = null;

    public SwipeHandlerX(final ListView listView) {
        gd = new GestureDetector(listView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return onScrollItem(e1, e2, distanceX);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return onFlingItem(e1, e2);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                XSwipeItemView view = findSwipeItemView(e);
                if (view != curView) {
                    resetCurrent();
                }
                return false;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                resetCurrent();
                return false;
            }
        });
        this.listView = listView;
        listView.setOnTouchListener(this);
        listView.setLongClickable(true);
    }

    public void resetCurrent() {
        if (null != curView) {
            moveToX(curView, 0);
            curView = null;
        }
    }

    private static void moveToX(View view, int endX) {
        if (view == null) {
            return;
        }
        int n = view.getScrollX();
        if (n != endX) {
            Message msg = new MoveHandler(view, endX).obtainMessage();
            msg.sendToTarget();
        }
    }

    private XSwipeItemView findSwipeItemView(MotionEvent e) {
        return findSwipeItemView(e.getX(), e.getY());
    }

    private XSwipeItemView findSwipeItemView(float x, float y) {
        int selectPos = listView.pointToPosition((int) x, (int) y);
        if (selectPos >= 0) {
            View view = listView.getChildAt(selectPos - listView.getFirstVisiblePosition());
            if (view instanceof XSwipeItemView) {
                return (XSwipeItemView) view;
            }
        }
        return null;
    }

    private boolean onFlingItem(MotionEvent e1, MotionEvent e2) {
        if (curView != null) {
            XSwipeItemView itemView = curView;
            float dx = e2.getX() - e1.getX();
            int scrollX = itemView.getScrollX();
            if (scrollX > 0) {//<--
                if (dx < 0) {//<--
                    int rightWidth = itemView.getRightViewWidth();
                    if (scrollX > rightWidth / 4) {
                        moveToX(itemView, rightWidth);
                    } else {
                        moveToX(itemView, 0);
                    }
                } else {//-->
                    int rightWidth = itemView.getRightViewWidth();
//                    if (scrollX < rightWidth * 3 / 4) {
                    moveToX(itemView, 0);
//                    }
                }
            } else {//-->
                if (dx > 0) {//-->
                    int leftWidth = itemView.getLeftViewWidth();
                    if (-scrollX > leftWidth / 4) {
                        moveToX(itemView, -leftWidth);
                    } else {
                        moveToX(itemView, 0);
                    }
                } else {//<--
                    int leftWidth = itemView.getLeftViewWidth();
//                    if (-scrollX < leftWidth * 3 / 4) {
                    moveToX(itemView, 0);
//                    }
                }

            }
        }
        return false;
    }

    private boolean onScrollItem(MotionEvent e1, MotionEvent e2, float distanceX) {
        float dx = e2.getX() - e1.getX();
        float dy = e2.getY() - e1.getY();
        boolean horScroll = Math.abs(dx) > Math.abs(dy * 2);
        if (horScroll) {
            MotionEvent cancelEvent = MotionEvent.obtain(e2);
            cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
            listView.onTouchEvent(cancelEvent);
            XSwipeItemView itemView = findSwipeItemView(e1);
            if (itemView != null) {
                if (itemView != curView) {
                    moveToX(curView, 0);
                    curView = itemView;
                }
                float newScrollX = distanceX + itemView.getScrollX();
                if (newScrollX > 0) {//<--
                    int rightWidth = itemView.getRightViewWidth();
                    if (newScrollX > rightWidth) {
                        itemView.scrollTo(rightWidth, 0);
                    } else {
                        itemView.scrollBy((int) distanceX, 0);
                    }
                } else {//-->
                    int leftWidth = itemView.getLeftViewWidth();
                    if (-newScrollX > leftWidth) {
                        itemView.scrollTo(-leftWidth, 0);
                    } else {
                        itemView.scrollBy((int) distanceX, 0);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void enable(boolean enable) {
        this.enable = enable;
    }

    public boolean enable() {
        return this.enable;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        return gd.onTouchEvent(ev);
    }

}
