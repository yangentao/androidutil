package yet.ui.util;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import yet.log.Yog;
import yet.util.MyDate;

public class Dumps {
	/**
	 * 打印一个类的所有方法
	 *
	 * @param cls
	 */
	public static void classMethod(Class<?> cls) {
		ArrayList<String> all = new ArrayList<String>(64);
		for (Method m : cls.getMethods()) {
			StringBuilder sb = new StringBuilder(128);
			sb.append(m.getName());
			sb.append("(");
			boolean first = true;
			for (Class<?> p : m.getParameterTypes()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(p.getSimpleName());
			}
			sb.append(") -->　");
			sb.append(m.getReturnType().getSimpleName());

			if (Modifier.isStatic(m.getModifiers())) {
				sb.append("  [static]");
			}

			all.add(sb.toString());

		}
		Collections.sort(all);
		for (String m : all) {
			Yog.INSTANCE.d(m);
		}
	}

	public static void bundle(Bundle b) {
		dumpBundle("", b);
	}

	private static void dumpBundle(String prefix, Bundle b) {
		if (b != null) {
			for (String key : b.keySet()) {
				Object value = b.get(key);
				if (value instanceof Bundle) {
					Yog.INSTANCE.d(prefix, key);
					dumpBundle(prefix + "        ", (Bundle) value);
				} else {
					Yog.INSTANCE.d(prefix, key, value, value == null ? " null " : value.getClass().getSimpleName());
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void build() {
		Yog.INSTANCE.d("BOARD", Build.BOARD);
		Yog.INSTANCE.d("BOOTLOADER", Build.BOOTLOADER);
		Yog.INSTANCE.d("BRAND", Build.BRAND);
		Yog.INSTANCE.d("CPU_ABI", Build.CPU_ABI);
		Yog.INSTANCE.d("CPU_ABI2", Build.CPU_ABI2);
		Yog.INSTANCE.d("DEVICE", Build.DEVICE);
		Yog.INSTANCE.d("DISPLAY", Build.DISPLAY);
		Yog.INSTANCE.d("FINGERPRINT", Build.FINGERPRINT);
		Yog.INSTANCE.d("HARDWARE", Build.HARDWARE);
		Yog.INSTANCE.d("HOST", Build.HOST);
		Yog.INSTANCE.d("ID", Build.ID);
		Yog.INSTANCE.d("MANUFACTURER", Build.MANUFACTURER);
		Yog.INSTANCE.d("MODEL", Build.MODEL);
		Yog.INSTANCE.d("PRODUCT", Build.PRODUCT);
		Yog.INSTANCE.d("RADIO", Build.RADIO);
		Yog.INSTANCE.d("SERIAL", Build.SERIAL);
		Yog.INSTANCE.d("TAGS", Build.TAGS);
		Yog.INSTANCE.d("TIME", new MyDate(Build.TIME, Locale.getDefault()).formatDate());
		Yog.INSTANCE.d("TYPE", Build.TYPE);
		Yog.INSTANCE.d("UNKNOWN", Build.UNKNOWN);
		Yog.INSTANCE.d("USER", Build.USER);
		Yog.INSTANCE.d("getRadioVersion", Build.getRadioVersion());
	}

	public static void telMgr(TelephonyManager tm) {
		Yog.INSTANCE.d("Dump TelephonyManager");
		Yog.INSTANCE.d("line 1 number:", tm.getLine1Number());
		Yog.INSTANCE.d("country iso: ", tm.getNetworkCountryIso());
		Yog.INSTANCE.d("getNetworkOperator", tm.getNetworkOperator());
		Yog.INSTANCE.d("getNetworkOperatorName", tm.getNetworkOperatorName());
		Yog.INSTANCE.d("getNetworkType", tm.getNetworkType());
		Yog.INSTANCE.d("getPhoneType", tm.getPhoneType());
		Yog.INSTANCE.d("getSimCountryIso", tm.getSimCountryIso());
		Yog.INSTANCE.d("getSimOperator", tm.getSimOperator());
		Yog.INSTANCE.d("getSimOperatorName ", tm.getSimOperatorName());
		Yog.INSTANCE.d("getSimSerialNumber ", tm.getSimSerialNumber());
		Yog.INSTANCE.d("getSimState ", tm.getSimState());
		Yog.INSTANCE.d("getSubscriberId ", tm.getSubscriberId());
		Yog.INSTANCE.d("getDeviceId ", tm.getDeviceId());
		Yog.INSTANCE.d("getDeviceSoftwareVersion ", tm.getDeviceSoftwareVersion());
	}
}
