package yet.util.app;

import android.content.res.Resources;
import yet.util.RefUtil;

/**
 * Created by entaoyang@163.com on 2016-10-05.
 */

public class SysRes {
	public static boolean bool(String name, boolean defVal) {
		Integer n = idBool(name);
		if (n != null) {
			return Resources.getSystem().getBoolean(n);
		}
		return defVal;
	}

	public static String string(String name, String defVal) {
		Integer n = idString(name);
		if (n != null) {
			return Resources.getSystem().getString(n);
		}
		return defVal;
	}

	public static Integer idBool(String name) {
		return (Integer) RefUtil.getStatic("com.android.internal.R$bool", name);
	}

	public static int idBool(String name, int defVal) {
		Integer n = (Integer) RefUtil.getStatic("com.android.internal.R$bool", name);
		return n == null ? defVal : n;
	}

	public static Integer idString(String name) {
		return (Integer) RefUtil.getStatic("com.android.internal.R$string", name);
	}

	public static int idString(String name, int defVal) {
		Integer n = (Integer) RefUtil.getStatic("com.android.internal.R$string", name);
		return n == null ? defVal : n;
	}
}
