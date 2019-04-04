package yet.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import yet.ui.res.Asset;

public class ZipUtil {
	/**
	 * 只解压第一个文件
	 * 
	 * @param zis
	 * @param toDir
	 * @throws IOException
	 */
	public static void unzipOneToDir(ZipInputStream zis, File toDir) throws IOException {
		OutputStream os = null;
		try {
			ZipEntry e = zis.getNextEntry();
			if (e != null) {
				os = new FileOutputStream(new File(toDir, e.getName()));
				StreamUtil.copyStream(zis, false, os, true);
				zis.closeEntry();
			}

		} finally {
			close(os);
			close(zis);
		}
	}
	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
		}
	}
	public static void unzipOneToFile(ZipInputStream zis, File toFile) throws IOException {
		OutputStream os = null;
		try {
			ZipEntry e = zis.getNextEntry();
			if (e != null) {
				os = new FileOutputStream(toFile);
				StreamUtil.copyStream(zis, false, os, true);
				zis.closeEntry();
			}

		} finally {
			close(os);
			close(zis);
		}
	}

	public static void unzipAssetOneToDir(String assetFile, File toDir) throws IOException {
		ZipInputStream zis = Asset.INSTANCE.streamZip(assetFile);
		unzipOneToDir(zis, toDir);
	}

	public static void unzipAssetOneToFile(String assetFile, File toFile) throws IOException {
		ZipInputStream zis = Asset.INSTANCE.streamZip(assetFile);
		unzipOneToFile(zis, toFile);
	}

	public static void zip(byte[] data, String filename, File zipFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		zos.putNextEntry(new ZipEntry(filename));
		try {
			zos.write(data);
			zos.closeEntry();
			zos.flush();
			fos.flush();
		} finally {
			close(zos);
			close(fos);
			zos = null;
			fos = null;
		}
	}

	public static void zip(File from, File zipFile) throws IOException {
		FileInputStream fis = new FileInputStream(from);
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		zos.putNextEntry(new ZipEntry(from.getName()));
		try {
			StreamUtil.copyStream(fis, zos, false);
			zos.closeEntry();
			zos.flush();
			fos.flush();
		} finally {
			close(zos);
			close(fos);
			close(fis);
			zos = null;
			fos = null;
			fis = null;
		}
	}

	public static void gzip(byte[] data, File zipFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		GZIPOutputStream zos = new GZIPOutputStream(fos);
		try {
			zos.write(data);
			zos.finish();
			zos.flush();
			fos.flush();
		} finally {
			close(zos);
			close(fos);
			zos = null;
			fos = null;
		}
	}

	public static void gzip(File from, File zipFile) throws IOException {
		FileInputStream fis = new FileInputStream(from);
		FileOutputStream fos = new FileOutputStream(zipFile);
		GZIPOutputStream zos = new GZIPOutputStream(fos);
		try {
			StreamUtil.copyStream(fis, zos, false);
			zos.finish();
			zos.flush();
			fos.flush();
		} finally {
			close(zos);
			close(fos);
			close(fis);
			zos = null;
			fos = null;
			fis = null;
		}
	}

	public static String readGzipUtf8(File gzipFile) throws Exception {
		byte[] buffer = readGzip(gzipFile);
		return buffer == null ? null : new String(buffer, "UTF-8");
	}

	public static byte[] readGzip(File gzipFile) throws IOException {
		FileInputStream fis = new FileInputStream(gzipFile);
		GZIPInputStream zis = new GZIPInputStream(fis);
		return StreamUtil.readBytes(zis);
	}

	public static void unGzip(File gzipFile, File toFile) throws IOException {
		FileInputStream fis = new FileInputStream(gzipFile);
		GZIPInputStream zis = new GZIPInputStream(fis);
		FileOutputStream fos = new FileOutputStream(toFile);
		StreamUtil.copyStream(zis, fos, true);
	}

}
