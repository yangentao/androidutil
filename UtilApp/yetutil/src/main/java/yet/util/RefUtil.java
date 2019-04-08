package yet.util;

import dev.entao.yapp.App;
import dev.entao.yog.Yog;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;

public class RefUtil {

	//field HashSet<String> children =...
	//fieldType(childrenField, HashSet.class, String.class) => true
	public static boolean isGenericFieldType(Field field, Class<?> fieldClass, Class<?> genericParamClass) {
		try {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				if (pt.getRawType() == fieldClass) {
					Type[] arr = pt.getActualTypeArguments();
					if (arr.length > 0) {
						return arr[0] == genericParamClass;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static boolean isGenericFieldType2(Field field, Class<?> genericParamClass1, Class<?> genericParamClass2) {
		try {
			Type t = field.getGenericType();
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				Type[] arr = pt.getActualTypeArguments();
				if (arr.length >= 2) {
					return arr[0] == genericParamClass1 && arr[1] == genericParamClass2;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}


	public static void dumpObject(Object obj) {
		if (obj == null) {
			Yog.INSTANCE.d("NULL");
			return;
		}
		dumpClass(obj.getClass());
	}

	public static void dumpService(String name) {
		Yog.INSTANCE.d("Dump Service ", name);
		Object obj = App.INSTANCE.getContext().getSystemService(name);
		dumpObject(obj);
	}

	public static void dumpClass(Class<?> cls) {
		Yog.INSTANCE.d("Dump Class Methods: ", cls.getName());
		StringBuilder sb = new StringBuilder(128);
		Field[] fs = cls.getDeclaredFields();
		Arrays.sort(fs, new Comparator<Field>() {

			@Override
			public int compare(Field lhs, Field rhs) {
				boolean s1 = Modifier.isStatic(lhs.getModifiers());
				boolean s2 = Modifier.isStatic(rhs.getModifiers());
				if (s1 && !s2) {
					return -1;
				}
				if (!s1 && s2) {
					return 1;
				}
				return lhs.getName().compareTo(rhs.getName());

			}
		});
		Method[] ms = cls.getDeclaredMethods();
		Arrays.sort(ms, new Comparator<Method>() {

			@Override
			public int compare(Method lhs, Method rhs) {
				boolean s1 = Modifier.isStatic(lhs.getModifiers());
				boolean s2 = Modifier.isStatic(rhs.getModifiers());
				if (s1 && !s2) {
					return -1;
				}
				if (!s1 && s2) {
					return 1;
				}

				return lhs.getName().compareTo(rhs.getName());
			}
		});
		for (Field f : fs) {
			sb.setLength(0);
			int mod = f.getModifiers();
			if (Modifier.isPublic(mod)) {
				sb.append("public ");
			} else if (Modifier.isPrivate(mod)) {
				sb.append("private ");
			} else if (Modifier.isProtected(mod)) {
				sb.append("protected ");
			}
			boolean isStatic = Modifier.isStatic(f.getModifiers());
			if (isStatic) {
				sb.append("static ");
			}
			sb.append(f.getType().getSimpleName()).append( " ");
			sb.append(f.getName());
			if (isStatic) {
				sb.append(" = ").append(getStatic(cls, f.getName()));
			}
			Yog.INSTANCE.d(sb.toString());
		}

		for (Method m : ms) {
			sb.setLength(0);
			if (Modifier.isPublic(m.getModifiers())) {
				sb.append("public ");
			}
			if (Modifier.isStatic(m.getModifiers())) {
				sb.append("static ");
			}
			sb.append(m.getReturnType().getSimpleName()).append( " ");
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
			sb.append(");");
			Yog.INSTANCE.d(sb.toString());
		}
	}

	public static void dumpClass(String cls) {
		try {
			dumpClass(Class.forName(cls));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasClass(String cls) {
		try {
			Class.forName(cls);
			return true;
		} catch (Throwable t) {
		}
		return false;
	}

	public static Field field(Class<?> cls, String name) {
		try {
			return fieldE(cls, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field fieldE(Class<?> cls, String name) throws Exception {
		Field f = cls.getDeclaredField(name);
		f.setAccessible(true);
		return f;
	}

	public static Object get(Object obj, String name, Object defVal) {
		try {
			return getE(obj, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defVal;
	}

	public static Object getE(Object obj, String name) throws Exception {
		Field f = fieldE(obj.getClass(), name);
		return f.get(obj);
	}

	public static Object getStatic(String cls, String name) {
		return getStatic(cls, name, null);
	}

	public static Object getStatic(String cls, String name, Object defVal) {
		try {
			Class<?> c = Class.forName(cls);
			return getStatic(c, name, defVal);
		} catch (Throwable t) {
		}
		return null;
	}

	public static Object getStatic(Class<?> cls, String name) {
		return getStatic(cls, name, null);
	}

	public static Object getStatic(Class<?> cls, String name, Object defVal) {
		try {
			return getStaticE(cls, name);
		} catch (Exception e) {
		}
		return defVal;
	}

	public static Object getStaticE(Class<?> cls, String name) throws Exception {
		Field f = fieldE(cls, name);
		if (!isStatic(f)) {
			throw new NoSuchFieldException("NOT a static field!");
		}
		return f.get(null);
	}

	/**
	 * 如果参数args中有Integer,Long,Boolean, Float, Double,Byte,Short类型
	 * 会转换成对应的int,long, boolean, float, double, byte, short
	 *
	 * @param args
	 * @return
	 */
	public static Class<?>[] getTypes(Object... args) {
		Class<?>[] arr = new Class<?>[args.length];
		int i = 0;
		for (Object obj : args) {
			if (obj == null) {
				arr[i] = String.class;
			} else {
				Class<?> cls = obj.getClass();
				if (cls == Integer.class) {
					arr[i] = int.class;
				} else if (cls == Long.class) {
					arr[i] = long.class;
				} else if (cls == Boolean.class) {
					arr[i] = boolean.class;
				} else if (cls == Float.class) {
					arr[i] = float.class;
				} else if (cls == Double.class) {
					arr[i] = double.class;
				} else if (cls == Byte.class) {
					arr[i] = byte.class;
				} else if (cls == Short.class) {
					arr[i] = short.class;
				} else {
					arr[i] = cls;
				}
			}
			++i;
		}
		return arr;
	}

	public static boolean hasConstructor(Class<?> cls, Class<?>... argTypes) {
		try {
			Constructor<?> c = cls.getConstructor(argTypes);
			return true;
		} catch (NoSuchMethodException e) {
		}
		return false;
	}

	public static boolean hasField(Class<?> cls, String name) {
		return null != field(cls, name);
	}

	public static boolean hasMethodObject(Object obj, String name, Class<?>... argTypes) {
		try {
			return hasMethod(obj.getClass(), name, argTypes);
		} catch (Throwable t) {
		}
		return false;
	}

	public static boolean hasMethod(Class<?> cls, String name, Class<?>... argTypes) {
		return null != method(cls, name, argTypes);
	}

	public static boolean hasMethod(String cls, String name, Class<?>... argTypes) {
		try {
			return hasMethod(Class.forName(cls), name, argTypes);
		} catch (Throwable t) {
		}
		return false;
	}

	public static Object invokeObject(Object obj, String name, Class<?>[] argTypes, Object... args) {
		try {
			if (obj != null) {
				return invokeObjectE(obj, name, argTypes, args);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 如果参数args中有Integer,Long,Boolean, Float, Double,Byte,Short类型
	 * 会转换成对应的int,long, boolean, float, double, byte, short
	 *
	 * @param obj
	 * @param name
	 * @param args
	 * @return
	 */
	public static Object invokeObject(Object obj, String name, Object... args) {
		return invokeObject(obj, name, getTypes(args), args);
	}

	public static Object invokeObjectE(Object obj, String name, Class<?>[] argTypes, Object... args) throws Exception {
		Method m = methodE(obj.getClass(), name, argTypes);
		return m.invoke(obj, args);
	}

	public static Object invokeObjectE(Object obj, String name, Object... args) throws Exception {
		return invokeObjectE(obj, name, getTypes(args), args);
	}

	public static Object invokeServiceE(String service, String name, Object... args) throws Exception {
		return invokeServiceE(service, name, getTypes(args), args);
	}

	public static Object invokeServiceE(String service, String name, Class<?>[] argTypes, Object... args) throws Exception {
		Object obj = App.INSTANCE.getContext().getSystemService(service);
		return invokeObjectE(obj, name, argTypes, args);
	}

	public static Object invokeService1(String service, String name, Class<?> argType, Object arg) {
		return invokeService(service, name, new Class<?>[]{argType}, arg);
	}

	public static Object invokeService(String service, String name, Class<?>[] argTypes, Object... args) {
		Object obj = App.INSTANCE.getContext().getSystemService(service);
		if (obj != null) {
			return invokeObject(obj, name, argTypes, args);
		}
		return null;
	}

	public static Object invokeService(String service, String name, Object... args) {
		return invokeService(service, name, getTypes(args), args);
	}

	public static Object invokeStatic1(Class<?> cls, String name, Class<?> argType, Object arg) {
		return invokeStatic(cls, name, new Class<?>[]{argType}, arg);
	}

	public static Object invokeStatic(Class<?> cls, String name, Class<?>[] argTypes, Object... args) {
		try {
			return invokeStaticE(cls, name, argTypes, args);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 如果参数args中有Integer,Long,Boolean, Float, Double,Byte,Short类型
	 * 会转换成对应的int,long, boolean, float, double, byte, short
	 *
	 * @param cls
	 * @param name
	 * @param args
	 * @return
	 */
	public static Object invokeStatic(Class<?> cls, String name, Object... args) {
		return invokeStatic(cls, name, getTypes(args), args);
	}

	public static Object invokeStatic1(String cls, String name, Class<?> argType, Object arg) {
		return invokeStatic(cls, name, new Class<?>[]{argType}, arg);
	}

	public static Object invokeStatic(String cls, String name, Class<?>[] argTypes, Object... args) {
		try {
			return invokeStaticE(Class.forName(cls), name, argTypes, args);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object invokeStatic(String cls, String name, Object... args) {
		return invokeStatic(cls, name, getTypes(args), args);
	}

	public static Object invokeStaticE(Class<?> cls, String name, Class<?>[] argTypes, Object... args) throws Exception {
		Method m = methodE(cls, name, argTypes);
		if (!isStatic(m)) {
			throw new NoSuchMethodException("NOT a static method!");
		}
		return m.invoke(null, args);
	}

	public static Object invokeStaticE(Class<?> cls, String name, Object... args) throws Exception {
		return invokeStaticE(cls, name, getTypes(args), args);
	}

	public static boolean isStatic(Field f) {
		return Modifier.isStatic(f.getModifiers());
	}

	public static boolean isStatic(Method m) {
		return Modifier.isStatic(m.getModifiers());
	}

	public static Method method(Class<?> cls, String name, Class<?>... argTypes) {
		try {
			return methodE(cls, name, argTypes);
		} catch (Exception e) {
		}
		return null;
	}

	public static Method methodE(Class<?> cls, String name, Class<?>... argTypes) throws Exception {
		Method m = cls.getMethod(name, argTypes);
		m.setAccessible(true);
		return m;
	}

	public static Object newInstance1(String clsName, Class<?> argType, Object arg) {
		try {
			Class<?> cls = Class.forName(clsName);
			Constructor<?> c = cls.getConstructor(argType);
			return c.newInstance(arg);
		} catch (Throwable e) {
		}
		return null;
	}

	public static Object newInstance(String clsName, Class<?>[] argTypes, Object... args) {
		try {
			Class<?> cls = Class.forName(clsName);
			Constructor<?> c = cls.getConstructor(argTypes);
			return c.newInstance(args);
		} catch (Throwable e) {
		}
		return null;
	}

	public static Object newInstance(String clsName, Object... args) {
		try {
			Class<?> cls = Class.forName(clsName);
			Constructor<?> c = cls.getConstructor(getTypes(args));
			return c.newInstance(args);
		} catch (Throwable e) {
		}
		return null;
	}

	public static Object newInstance1(Class<?> cls, Class<?> argType, Object arg) {
		try {
			Constructor<?> c = cls.getConstructor(argType);
			return c.newInstance(arg);
		} catch (Exception e) {
		}
		return null;
	}

	public static Object newInstance(Class<?> cls, Class<?>[] argTypes, Object... args) {
		try {
			Constructor<?> c = cls.getConstructor(argTypes);
			return c.newInstance(args);
		} catch (Exception e) {
		}
		return null;
	}

	public static Object newInstance(Class<?> cls, Object... args) {
		try {
			Constructor<?> c = cls.getConstructor(getTypes(args));
			return c.newInstance(args);
		} catch (Exception e) {
		}
		return null;
	}

	public static boolean set(Object obj, String name, Object value) {
		try {
			setE(obj, name, value);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static void setE(Object obj, String name, Object value) throws Exception {
		Field f = fieldE(obj.getClass(), name);
		f.set(obj, value);
	}

	public static boolean setStatic(Class<?> cls, String name, Object value) {
		try {
			setStaticE(cls, name, value);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static void setStaticE(Class<?> cls, String name, Object value) throws Exception {
		Field f = fieldE(cls, name);
		if (!isStatic(f)) {
			throw new NoSuchFieldException("NOT a static field!");
		}
		f.set(null, value);
	}



}
