package cn.viworks.vgenerator.data.pojo;

import java.util.List;

/**
 * 生成类信息.
 */
public class Clazz {
	
	/**
	 * 类名(首字母大写)
	 */
	private String name;

	/**
	 * 类名(首字母大写)
	 */
	private String upperCaseName;

	/**
	 * 类名(首字母小写)
	 */
	private String lowerCaseName;

	/**
	 * 类说明
	 */
	private String comment;

	/**
	 * 类的所有属性
	 */
	private List<Property> propertyList;
	
	/**
	 * 类的所有属性
	 */
	private Property primaryProperty;
	
	/**
	 * 类的对的表信息
	 */
	private Table table;

	/**
	 * 类的映射的名称
	 */
	private String ormName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}

	public Property getPrimaryProperty() {
		return primaryProperty;
	}

	public void setPrimaryProperty(Property primaryProperty) {
		this.primaryProperty = primaryProperty;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
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

	public String getOrmName() {
		return ormName;
	}

	public void setOrmName(String ormName) {
		this.ormName = ormName;
	}
}
