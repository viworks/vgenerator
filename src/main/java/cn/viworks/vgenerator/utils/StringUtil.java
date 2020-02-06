package cn.viworks.vgenerator.utils;

public final class StringUtil {

	private StringUtil() {}
	
	/**
	 * 判断操作
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}
}
