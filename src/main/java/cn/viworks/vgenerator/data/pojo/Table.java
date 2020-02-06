package cn.viworks.vgenerator.data.pojo;

import java.util.List;

/**
 * 表结构对象类.
 */
public class Table {
	
	/**
	 * 表名
	 */
	private String name;

	/**
	 * 表注释
	 */
	private String remarks;

	/**
	 * 表类型
	 */
	private String type;

	/**
	 * 列项目
	 */
	private List<Column> columnList;

	/**
	 * 主键项目
	 */
	private List<Column> primaryKeyList;

	/**
	 * 主键项目(id)
	 */
	private Column primaryKey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		if (remarks == null || "".equals(remarks)) {
			return name;
		}
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public List<Column> getPrimaryKeyList() {
		return primaryKeyList;
	}

	public void setPrimaryKeyList(List<Column> primaryKeyList) {
		this.primaryKeyList = primaryKeyList;
	}

	public Column getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey;
	}

	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String TABLE_TYPE = "TABLE_TYPE";
	public static final String REMARKS = "REMARKS";
	public static final String PK = "";
	public static final String PK_TYPE = "";
}
