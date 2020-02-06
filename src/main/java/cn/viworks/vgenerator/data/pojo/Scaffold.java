package cn.viworks.vgenerator.data.pojo;

/**
 * 结构对象类
 */
public class Scaffold {

	/**
	 * 命令行
	 */
	private String cmd;

	/**
	 * 类相关信息
	 */
	private Clazz clazz;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}
}
