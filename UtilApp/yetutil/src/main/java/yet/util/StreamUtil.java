package yet.util;

import java.io.*;

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
public class StreamUtil {
	/**
	 * 会close inputstream
	 *
	 * @param is
	 * @return
	 */
	public static byte[] readBytes(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
		StreamUtil.copyStream(is, os);
		return os.toByteArray();
	}

	/**
	 * 会关闭输入流
	 *
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readString(InputStream is, String encoding) throws IOException {
		byte[] data = readBytes(is);
		return data == null ? null : new String(data, encoding);
	}
	/**
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		copyStream(is, true, os, true);
	}

	public static void copyStream(InputStream is, OutputStream os, boolean close) throws IOException {
		copyStream(is, close, os, close);
	}

	public static void copyStream(InputStream is, boolean closeIn, OutputStream os, boolean closeOut) throws IOException {
		try {
			byte[] buffer = new byte[4096];
			int n = -1;
			while ((n = is.read(buffer)) != -1) {
				os.write(buffer, 0, n);
			}
			os.flush();
		} finally {
			if (closeIn) {
				close(is);
			}
			if (closeOut) {
				close(os);
			}
		}
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception ex) {
//				ex.printStackTrace();
			}
		}
	}

}
