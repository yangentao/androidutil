package dev.entao.util;

import android.os.IBinder;

public class ServiceManager {
	private static final String NAME = "android.os.ServiceManager";

	public static IBinder getService(String name) {
		return (IBinder) RefUtil.invokeStatic(NAME, "getService", name);
	}

	public static String[] listServices() {
		return (String[]) RefUtil.invokeStatic(NAME, "listServices");
	}

}
