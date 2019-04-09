package dev.entao.ui.widget;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by yet on 2015/10/25.
 */
public interface LinearAdapter<T> {
    View onCreateItemView(Context context, T item);

    List<T> onRequestItems();
}
