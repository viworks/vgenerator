package cn.viworks.vgenerator.data.pojo;

/**
 * 类属性信息.
 */
public class Property {
	
	/**
	 * 属性名(首字母小写)
	 */
	private String name;
	
	/**
	 * 属性名(首字母大写)
	 */
	private String upperCaseName;
	
	/**
	 * 属性名(首字母小写)
	 */
	private String lowerCaseName;
	
	/**
	 * 属性说明
	 */
	private String comment;
	
	/**
	 * 属性类型
	 */
	private String type;
	
	/**
	 * 属性所对应的列
	 */
	private Column column;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpperCaseName() {
		return upperCaseName;
	}
	public void setUpperCaseName(String upperCaseName) {
		this.upperCaseName = upperCaseName;
	}
	public String getLowerCaseName() {
		return lowerCaseName;
	}
	public void setLowerCaseName(String lowerCaseName) {
		this.lowerCaseName = lowerCaseName;
	}
	public String getComment() {
		if (comment == null || "".equals(comment)) {
			return name;
		}
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
}
