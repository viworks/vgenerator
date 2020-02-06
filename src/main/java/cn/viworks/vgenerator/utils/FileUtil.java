package cn.viworks.vgenerator.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * 私有化构造函数
     */
    private FileUtil() {
    }

	/**
	 * 判断文件或者文件夹是否存在
	 * @param folderName 文件或者文件夹的绝对路径
	 * @return boolean 存在/不存在
	 */
	public static boolean exist(String folderName) {
		File file = new File(folderName);
		boolean returnBoolean = file.exists();
		return returnBoolean;
	}
    
	/**
	 * 这里的路径格式必须是：c:\tmp\tmp\ 或者是c:\tmp\tmp
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolders(String path) {
		return (new File(path)).mkdirs();
	}
	
	/**
	 * 递归创建文件
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolds(String path) {
		boolean ret = false;
		String child = path;
		if (!exist(path)) {
			int i = path.lastIndexOf(File.separator);
			String pathTmp = path.substring(0, i);
			child = pathTmp;
			createFolds(pathTmp);
			ret = createFolder(child);
		} else {
			return true;
		}
		return ret;
	}
	
	/**
	 * 在已经存在的路径下创建文件夹
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolder(String path) {
		File file = new File(path);
		boolean returnBoolean = file.mkdir();
		return returnBoolean;
	}

	/**
	 * 创建文件或者文件夹
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return boolean
	 */
	public static boolean createFile(String path, String fileName) {
		String fPath = path + File.separator + fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 创建文件
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(String fileName) {
		String fPath = fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
    /**
     * 读取路径下指定的文件,取得InputStream对象
     * 指定文件为空的情况,返回null
     * @param path 指定路径
     * @return InputStream对象
     */
    public static InputStream getFileAsStream(String path) {
        ClassLoader loader = FileUtil.class.getClassLoader();
        return loader.getResourceAsStream(path);
    }

	/**
	 * 将原文件拷贝到目标文件
	 * @param sourceFile 原文件
	 * @param targetFile 目标文件
	 * @return boolean 成功/失败
	 */
	public static boolean copyFile(String sourceFile, String targetFile) {

		String path = sourceFile; //原文件路径
		String target = targetFile; //目标文件路径

		boolean flag = true;

		try {

			File file = new File(path);
			FileInputStream stream = new FileInputStream(file); //把文件读入
			OutputStream bos = new FileOutputStream(target); //建立一个上传文件的输出流

			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead); //将文件写入服务器
			}

			bos.close();
			stream.close();

		} catch (Exception e) {
			flag = false;
		}

		return flag;

	}

	/**
	 * 文件夹下所有文件夹和文件的拷贝
	 * @param sourceFile 原文件
	 * @param targetFile 目标文件
	 */
	public static boolean copyDirectiory(String source, String target) {
		
		String file1 = target; //原文件路径
		String file2 = source; //目标文件路径

		boolean flag = true;
		
		//文件分隔符，Windows环境下为"\"，Unix环境下为"/"
		String separator = System.getProperty("file.separator");
		
		try {
			
			(new File(file1)).mkdirs();
			
			File[] file = (new File(file2)).listFiles();
			
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					FileInputStream input = new FileInputStream(file[i]);
					FileOutputStream output = new FileOutputStream(file1 + separator + file[i].getName());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (file[i].isDirectory()) {
					copyDirectiory(file2 + separator + file[i].getName(), file1 + separator + file[i].getName());
				}

			}
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}
	
	/**
	 * 返回的文件路径list
	 */
	private static List<String> fileWithPath = new ArrayList<String>();

	/**
	 * 根据文件路径取得该文件路径下的所有文件
	 * @param target 文件路径
	 * @return List<String> 返回的文件路径list
	 */
	public static List<String> getAllFileWithPath(String target) {
		getFileWithPath(target);
		return fileWithPath;
	}

	/**
	 * 根据文件路径取得该文件路径下的所有文件(内部子方法)
	 * @param target 文件路径
	 */
	private static void getFileWithPath(String target) {
		String file2 = target;
		try {
			File[] file = (new File(file2)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					fileWithPath.add(file2 + File.separator + file[i].getName());
				}
				if (file[i].isDirectory()) {
					getAllFileWithPath(file2 + File.separator + file[i].getName());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除文件夹，如果该文件夹下有子文件或者文件夹，则全部删除
	 * @param path 要删除的文件夹
	 * @return boolean 成功/失败
	 */
	public static boolean delFoldsWithChilds(String path) {
		int files = 0;
		File file = new File(path);
		File[] tmp = file.listFiles();
		if (tmp == null) {
			files = 0;
		} else {
			files = tmp.length;
		}
		for (int i = 0; i < files; i++) {
			delFoldsWithChilds(tmp[i].getAbsolutePath());
		}
		boolean ret = deleteFolder(path);
		return ret;

	}
	
	/**
	 * 取得文件名称（带后缀）
	 * @param filePath 文件路径（包括文件名称）
	 * @return String 文件名称
	 */
	public static String getFileName(String filePath) {
		String fileName = "";
		String tmpFilePath = filePath;
		int winIndex = tmpFilePath.indexOf("\\");
		int linuxIndex = tmpFilePath.indexOf("/");
		if (winIndex != -1)
			fileName = tmpFilePath.substring(winIndex + 1, tmpFilePath.length()).trim();
		if (linuxIndex != -1)
			fileName = tmpFilePath.substring(linuxIndex + 1, tmpFilePath.length()).trim();
		return fileName;
	}
	
	/**
	 * 得到文件的后缀
	 * @param fileName 文件名称
	 * @return String 后缀名称
	 */
	public static String getSuffix(String fileName) {
		String returnSuffix = "";
		String tmp = "";
		try {
			int index = fileName.lastIndexOf(".");
			if (index == -1) {
				tmp = "";
			} else
				tmp = fileName.substring(index + 1, fileName.length());
		} catch (Exception e) {
			tmp = "";
		}
		returnSuffix = tmp;
		return returnSuffix;
	}
	

	/**
	 * 将文件的路径格式转换为标准的文件路径格式
	 * @param inputPath 原文件路径
	 * @return String 转换后的文件路径
	 */
	public static String toStanderds(String inputPath) {

		String path = inputPath;
		char pathChar = '/';
		char pathCharLin = '/';
		char pathCharWin = '\\';

		char[] mychar = path.toCharArray();
		String rtp = "";

		if (String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
			pathChar = pathCharWin;
		}
		if (String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
			pathChar = pathCharLin;
		}

		for (int i = 0; i < mychar.length; i++) {
			if (mychar[i] == pathCharWin || mychar[i] == pathCharLin) {
				mychar[i] = pathChar;
			}

			if (mychar[i] != pathCharLin && mychar[i] != pathCharWin) {
				rtp += String.valueOf(mychar[i]);
			}

			if (i < mychar.length - 1) {
				if (mychar[i] == pathChar && mychar[i + 1] != pathChar && mychar[i + 1] != pathCharWin && mychar[i + 1] != pathCharLin) {
					rtp += String.valueOf(mychar[i]);
				}
			}
		}

		return rtp;
	}
	
	/**
	 * 将路径转换为linux路径－也可使用为将http的相对路径进行转换
	 * @param inputPath
	 * @return String
	 */
	public static String toLinuxStanderds(String inputPath) {

		String path = inputPath;
		char pathChar = '/';
		char pathCharLin = '/';
		char pathCharWin = '\\';

		char[] mychar = path.toCharArray();
		String rtp = "";

		if (String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
			pathChar = pathCharWin;

		}
		if (String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
			pathChar = pathCharLin;
		}
		pathChar = '/';
		for (int i = 0; i < mychar.length; i++) {
			if (mychar[i] == pathCharWin || mychar[i] == pathCharLin) {
				mychar[i] = pathChar;
			}

			if (mychar[i] != pathCharLin && mychar[i] != pathCharWin)
				rtp += String.valueOf(mychar[i]);

			if (i < mychar.length - 1) {
				if (mychar[i] == pathChar && mychar[i + 1] != pathChar && mychar[i + 1] != pathCharWin && mychar[i + 1] != pathCharLin) {
					rtp += String.valueOf(mychar[i]);
				}
			}

		}

		return rtp;
	}
	

	
	/**
	 * 删除文件夹，当该文件夹下有文件或者文件夹的时候不能删除该文件夹
	 * @param path
	 * @return boolean
	 */
	public static boolean deleteFolder(String path) {
		File file = new File(path);
		boolean returnBoolean = file.delete();
		return returnBoolean;
	}
	
	

    /**
     * 连接路径字符串
     * @param baseFilePath 基础路径
     * @param fileName 文件名
     * @return 路径字符串
     */
    public static String combinePath(String baseFilePath, String fileName) {
        String rtnString = "";
        if ("\\".equals(baseFilePath.substring(baseFilePath.length() - 1)) || "/".equals(baseFilePath.substring(baseFilePath.length() - 1))) {
            if ("\\".equals(fileName.substring(0, 1)) || "/".equals(fileName.substring(0, 1))) {
                rtnString = baseFilePath + fileName.substring(1);
            } else {
                rtnString = baseFilePath + fileName;
            }
        } else {
            if ("\\".equals(fileName.substring(0, 1)) || "/".equals(fileName.substring(0, 1))) {
                rtnString = baseFilePath + fileName;
            } else {
                rtnString = baseFilePath + File.separator + fileName;
            }
        }
        return rtnString;
    }
    
    public static void main(String[] s) {
        System.out.println(getSuffix("aa.txt"));
    }
}
