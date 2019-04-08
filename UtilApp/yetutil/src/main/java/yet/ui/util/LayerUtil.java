package yet.ui.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import yet.util.app.App;

import java.util.ArrayList;

public class LayerUtil {
	private static class TempDrawable {
		public Drawable drawable;
		public int left = 0;
		public int top = 0;
		public int right = 0;
		public int bottom = 0;

		public TempDrawable(Drawable d, int n) {
			drawable = d;
			left = top = right = bottom = n;
		}
	}

	ArrayList<TempDrawable> ls = new ArrayList<TempDrawable>();

	//inset dp
	public void add(Drawable d, int insetLeftDp, int insetTopDp, int insetRightDp, int insetBottomDp) {
		TempDrawable td = new TempDrawable(d, 0);
		td.left = insetLeftDp;
		td.top = insetTopDp;
		td.right = insetRightDp;
		td.bottom = insetBottomDp;
		ls.add(td);
	}

	public void add(Drawable d, int insetDp) {
		TempDrawable t = new TempDrawable(d, insetDp);
		ls.add(t);
	}

	public void add(Drawable d) {
		TempDrawable t = new TempDrawable(d, 0);
		ls.add(t);
	}

	public LayerDrawable get() {
		Drawable[] layers = new Drawable[ls.size()];
		for (int i = 0; i < ls.size(); ++i) {
			layers[i] = ls.get(i).drawable;
		}
		LayerDrawable ld = new LayerDrawable(layers);
		for (int i = 0; i < ls.size(); ++i) {
			TempDrawable t = ls.get(i);
			ld.setLayerInset(i, App.INSTANCE.dp2px(t.left), App.INSTANCE.dp2px(t.top), App.INSTANCE.dp2px(t.right), App.INSTANCE.dp2px(t.bottom));
		}
		return ld;
	}

	public static LayerDrawable build(Drawable d, int inset) {
		LayerDrawable ld = new LayerDrawable(new Drawable[]{d});
		ld.setLayerInset(0, App.INSTANCE.dp2px(inset), App.INSTANCE.dp2px(inset), App.INSTANCE.dp2px(inset), App.INSTANCE.dp2px(inset));
		return ld;
	}
}
