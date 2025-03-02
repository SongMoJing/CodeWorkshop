package com.funcablaze.codeworkshop.Function.Manager;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.NonNull;

import com.funcablaze.codeworkshop.Fragment.Editor.Base;
import com.funcablaze.codeworkshop.Fragment.Editor.Document;
import com.funcablaze.codeworkshop.Fragment.Editor.Zip;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileManager {

	public static File fileCopy;

	public static boolean createNewProject(String parentPath, String fileName) throws IOException {
		File file = new File(parentPath, fileName);
		return file.createNewFile();
	}

	/**
	 * 新建文件
	 *
	 * @param parentPath 父路径
	 * @param fileName   文件名
	 * @return <code>true</code> 创建成功<br /><code>false</code> 文件早已存在
	 * @throws IOException IO异常
	 */
	public static boolean createNewFile(String parentPath, String fileName) throws IOException {
		File file = new File(parentPath, fileName);
		return file.createNewFile();
	}

	/**
	 * 新建文件夹
	 *
	 * @param parentPath 父路径
	 * @param folderName 文件夹名
	 * @return 创建成功
	 */
	public static boolean createNewFolder(String parentPath, String folderName) {
		File folder = new File(parentPath, folderName);
		return folder.mkdir();
	}

	/**
	 * 复制文件
	 *
	 * @param oldPath$Name 原文件路径
	 * @param newPath$Name 新文件路径
	 * @return <code>true</code> 复制成功<br /><code>false</code> 复制失败
	 */
	public static boolean copyFileTo(String oldPath$Name, String newPath$Name) {
		try {
			File oldFile = new File(oldPath$Name);
			if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
				return false;
			}
			FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
			FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
			byte[] buffer = new byte[1024];
			int byteRead;
			while (-1 != (byteRead = fileInputStream.read(buffer))) {
				fileOutputStream.write(buffer, 0, byteRead);
			}
			fileInputStream.close();
			fileOutputStream.flush();
			fileOutputStream.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 复制文件夹
	 *
	 * @param oldPath 原文件夹路径
	 * @param newPath 新文件夹路径
	 * @return
	 */
	public static boolean copyFolderTo(String oldPath, String newPath) {
		try {
			File newFile = new File(newPath);
			if (!newFile.exists()) {
				if (!newFile.mkdirs()) {
					return false;
				}
			}
			File oldFile = new File(oldPath);
			String[] files = oldFile.list();
			File temp;
			if (files != null) {
				for (String file : files) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + file);
					} else {
						temp = new File(oldPath + File.separator + file);
					}
					if (temp.isDirectory()) {
						//如果是子文件夹
						copyFolderTo(oldPath + "/" + file, newPath + "/" + file);
					} else if (temp.exists() && temp.isFile() && temp.canRead()) {
						FileOutputStream fileOutputStream = getFileOutputStream(newPath, temp);
						fileOutputStream.close();
					}
				}
			} else return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static @NonNull FileOutputStream getFileOutputStream(String newPath, File temp) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(temp);
		FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
		byte[] buffer = new byte[1024];
		int byteRead;
		while ((byteRead = fileInputStream.read(buffer)) != -1) {
			fileOutputStream.write(buffer, 0, byteRead);
		}
		fileInputStream.close();
		fileOutputStream.flush();
		return fileOutputStream;
	}

	/**
	 * 删除文件
	 *
	 * @param parentPath 文件路径
	 * @return 成功删除文件
	 */
	public static boolean deleteFile(String parentPath) {
		File file = new File(parentPath);
		if (!file.isDirectory()) {
			file.delete();
		}
		return file.exists() && file.isFile() && file.delete();
	}

	/**
	 * 删除文件夹
	 *
	 * @param folder 文件夹路径
	 * @return 成功删除文件夹
	 */
	public static boolean deleteFolder(String folder) {
		File dir = new File(folder);
		if (!dir.exists() || !dir.isDirectory())
			return false;
		for (File file : Objects.requireNonNull(dir.listFiles())) {
			if (file.isFile()) file.delete(); // 删除所有文件
			else if (file.isDirectory()) deleteFolder(file.getPath()); // 递规的方式删除文件夹
		}
		return dir.delete();// 删除目录本身
	}

	/**
	 * 重命名文件
	 *
	 * @param file    文件
	 * @param newName 新文件名
	 * @return 重命名后的文件
	 */
	public static File renameFile(File file, String newName) {
		file.renameTo(new File(file.getParent() + "/" + newName));
		return file;
	}

	/**
	 * 写入文件
	 *
	 * @param path    路径
	 * @param content 文件内容
	 * @return 成功写入文件
	 */
	public static boolean WriteFile(String path, String content) {
		try {
			OutputStream out = new FileOutputStream(path);
			byte[] dataBytes = content.getBytes();
			out.write(dataBytes);
			out.close();
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
	}

	/**
	 * 读取文件
	 *
	 * @param path 路径
	 * @return 文件内容
	 */
	public static String ReadFile(String path) {

		InputStream inputStream = null;
		Reader reader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(path);
			inputStream = new FileInputStream(file);
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			StringBuilder result = new StringBuilder();
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				result.append(temp).append('\r').append('\n');
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		StringBuilder Res = new StringBuilder();
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				Res.append(myReader.nextLine()).append('\n');
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			Res.append("```\n\n! ERR: FileNotFoundException \n\t>>> ").append(e);
		}
		return Res.toString();
	}

	public static Base find_504B0304_inFile(FileType fileType, String filePath) {
		try (ZipFile zipFile = new ZipFile(filePath)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith("word/") || name.startsWith("xl/") || name.startsWith("ppt/")) {
					return new Document(fileType, new File(filePath));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Zip(FileType.ZIP, new File(filePath));
	}

	/**
	 * 文件类型
	 */
	public enum FileType {
		/**
		 * 图像文件
		 */
		JPEG("FFD8FF"),
		PNG("89504E47"),
		GIF("47494638"),
		TIFF("49492A00"),
		/**
		 * Office 文件
		 */
		DOC("D0CF11E0"),
		PPT("D0CF11E0"),
		XLS("D0CF11E0"),
		DOCX("504B0304"),
		PPTX("504B0304"),
		XLSX("504B0304"),

		PDF("25504446"),
		/**
		 * ZIP/JAR 压缩包
		 */
		ZIP("504B0304"),
		/**
		 * RAR 压缩包
		 */
		RAR("52617221"),
		/**
		 * 音频文件
		 */
		WAV("57415645"),
		MP3("49443303000000002176"),

		/**
		 * 视频文件
		 */
		AVI("41564920"),
		MP4("00000020667479706D70"),
		/**
		 * TTF 字体文件
		 */
		TTF("111"),
		/**
		 * 文本文档
		 */
		TXT("");

		private String value = "";

		FileType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * @param filePath 文件路径
	 * @throws IOException &#064;description  第一步：获取文件输入流
	 */
	private static String getFileContent(String filePath) throws IOException {
		byte[] b = new byte[20];
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
            /*
              int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
              个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
              之所以从输入流中读取20个字节数据，是因为不同格式的文件头魔数长度是不一样的，比如 EML("44656C69766572792D646174653A")和GIF("47494638")
              为了提高识别精度所以获取的字节数相应地长一点
             */
			inputStream.read(b, 0, 20);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		return bytesToHexString(b);
	}

	/**
	 * @param
	 * @return 16进制字符串
	 * @description 第二步：将文件头转换成16进制字符串
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		System.out.println("文件类型16进制字符串是" + stringBuilder.toString());
		return stringBuilder.toString();
	}

	/**
	 * @param filePath 文件路径
	 * @return 文件类型
	 * @description 第三步：根据十六进制字符串判断文件类型格式
	 */
	public static FileType getType(String filePath) {
		try {
			String fileHead = getFileContent(filePath);
			if (fileHead == null || fileHead.length() == 0) {
				return null;
			}
			fileHead = fileHead.toUpperCase();
			FileType[] fileTypes = FileType.values();
			for (FileType type : fileTypes) {
//            startsWith() 方法用于检测字符串是否以指定的前缀开始
				if (fileHead.startsWith(type.getValue())) {
					return type;
				}
			}
		} catch (IOException ignored) {
		}
		return null;
	}

	public static class Observer extends FileObserver {

		public OnFileChangedListener onFileChangedListener;

		public Observer(File file, OnFileChangedListener onFileChangedListener) {
			super(file);
			this.onFileChangedListener = onFileChangedListener;
			startWatching();
		}

		public interface OnFileChangedListener {
			void onEvent(int event, String path);
		}

		@Override
		public void onEvent(int event, String path) {
			if (onFileChangedListener != null) {
				onFileChangedListener.onEvent(event, path);
			}
		}
	}
}
