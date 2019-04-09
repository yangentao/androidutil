package dev.entao.ui.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
public abstract class TouchMonitor {
	public abstract void onDown(View view);

	public abstract void onUp(View view);

	public abstract void onCancel(View view);

	public void onMove(View view, boolean inside) {
	}

	public void monitor(View view) {
		if (view == this.view) {
			return;
		}

		if (this.view != null) {
			this.view.setOnTouchListener(null);
		}
		this.view = view;
		if (this.view != null) {
			this.view.setOnTouchListener(listener);
		}
	}

	private View view;
	private View.OnTouchListener listener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getActionMasked();
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					onDown(v);
					break;
				case MotionEvent.ACTION_UP:
					if (isInside(v, event)) {
						onUp(v);
					} else {
						onCancel(v);
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					onCancel(v);
					break;
				case MotionEvent.ACTION_OUTSIDE:
					onCancel(v);
					break;
				case MotionEvent.ACTION_MOVE:
					onMove(v, isInside(v, event));
					break;
			}
			return false;
		}
	};

	private boolean isInside(View v, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (x >= 0 && y >= 0 && x < v.getWidth() && y < v.getHeight()) {
			return true;
		} else {
			return false;
		}
	}
}
