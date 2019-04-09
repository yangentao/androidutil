package dev.entao.ui.list.swipe;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by yet on 2015/10/26.
 */
public class MoveHandler extends Handler {
    private final static int DURATION = 100;
    private final static int DURATION_STEP = 10;
    int stepX = 0;
    int fromX = 0;
    int toX = 0;
    View view;

    public MoveHandler(View view, int to) {
        this.view = view;
        this.fromX = view.getScrollX();
        this.toX = to;
        stepX = (int) ((toX - fromX) * DURATION_STEP * 1.0 / DURATION);
    }

    @Override
    public void handleMessage(Message msg) {
        if (Math.abs(toX - fromX) <= Math.max(Math.abs(stepX), 10)) {
            view.scrollTo(toX, 0);
        } else {
            fromX += stepX;
            view.scrollTo(fromX, 0);
            this.sendEmptyMessageDelayed(0, DURATION_STEP);
        }
    }
}