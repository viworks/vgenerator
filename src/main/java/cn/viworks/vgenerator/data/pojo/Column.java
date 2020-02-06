package cn.viworks.vgenerator.data.pojo;

/**
 * 列结构对象类.
 */
public class Column {

	/**
	 * 列名
	 */
	private String name;
	
	/**
	 * 列注释
	 */
	private String remarks;
	
	/**
	 * 列数据类型
	 */
	private String dataType;
	
	/**
	 * 列数据类型名称
	 */
	private String typeName;
	
	/**
	 * 列大小
	 */
	private String columnSize;

	/**
	 * 主键是否自增
	 */
	private String isAutoincrement;


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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}

	public String getIsAutoincrement() {
		return isAutoincrement;
	}

	public void setIsAutoincrement(String isAutoincrement) {
		this.isAutoincrement = isAutoincrement;
	}

	public static final String COLUMN_NAME = "COLUMN_NAME";
	public static final String REMARKS = "REMARKS";
	public static final String DATA_TYPE = "DATA_TYPE";
	public static final String TYPE_NAME = "TYPE_NAME";
	public static final String COLUMN_SIZE = "COLUMN_SIZE";
	public static final String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";
}
