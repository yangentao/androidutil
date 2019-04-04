package yet.util.app;

import yet.util.RefUtil;

public class SystemProperties {
	private static final String CLS = "android.os.SystemProperties";

	public static String get(String key) {
		return (String) RefUtil.invokeStatic(CLS, "get", key);
	}

	public static String get(String key, String defVal) {
		return (String) RefUtil.invokeStatic(CLS, "get", new Class<?>[]{String.class , String.class}, key, defVal);
	}
}
