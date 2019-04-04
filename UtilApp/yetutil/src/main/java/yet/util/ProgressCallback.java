package yet.util;

/**
 * Created by yet on 2015/10/7.
 */
public interface ProgressCallback {
    void onFinish(boolean success);
    void onStart(long total);
    void onProgress(long total, long current, int percent);
}
