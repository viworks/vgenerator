package cn.viworks.vgenerator.data;

import cn.viworks.vgenerator.VgData;
import cn.viworks.vgenerator.data.pojo.Clazz;
import cn.viworks.vgenerator.data.pojo.Column;
import cn.viworks.vgenerator.data.pojo.Table;
import cn.viworks.vgenerator.data.pojo.Property;
import cn.viworks.vgenerator.utils.StringUtil;
import com.alibaba.druid.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcData extends VgData {

	private String DIALECT = null;

	public JdbcData() {
		super();
	}

	public void init() {
		JdbcDataConfig cfg = (JdbcDataConfig) getConfig();

		DataSourceFactory.instance().init(cfg);
		this.DIALECT = JdbcUtils.getDbType(cfg.getUrl(), null);
	}

	public String ORACLE = "oracle";
	public String MYSQL = "mysql";

	/**
	 * 获取得数据库所有的表信息.
	 */
	public List<Table> findAllTables() {
		List<Table> tables = new ArrayList<Table>();
		Connection connection = null;
		try {
			connection = DataSourceFactory.instance().getDataSource().getConnection();
			DatabaseMetaData dbMetaData = connection.getMetaData();

			ResultSet rs = null;
			if (MYSQL.equals(DIALECT)) {
				rs = dbMetaData.getTables(null, "public", null, null);
			} else {
				rs = dbMetaData.getTables(null, null, null, null);
			}

			while (rs.next()) {
				Table t = buildTable(connection, rs, dbMetaData);
				if (t != null) {
					tables.add(t);
					System.out.println("load table:" + t.getName());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tables;
	}

	/**
	 * 获取单个数据库表的信息.
	 */
	public Table findTable(String tableName) {
		Table table = null;
		Connection connection = null;
		try {
			connection = DataSourceFactory.instance().getDataSource().getConnection();
			DatabaseMetaData dbMetaData = connection.getMetaData();
			ResultSet rs = null;
			if (MYSQL.equals(DIALECT)) {
				rs = dbMetaData.getTables(null, "public", tableName, null);
			} else {
				rs = dbMetaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
			}

			while (rs.next()) {
				table = buildTable(connection, rs, dbMetaData);
//                table.setRemarks(getTableComment(connection, tableName, rs.getString(1)));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return table;
	}

	/**
	 * 构建单个数据库表的信息.
	 */
	public Table buildTable(Connection connection, ResultSet rs, DatabaseMetaData dbMetaData) throws SQLException {
		if (rs == null) {
			return null;
		}
		Table t = null;
		// 设置表的属性
		if ("TABLE".equals(rs.getString(Table.TABLE_TYPE)) || "VIEW".equals(rs.getString(Table.TABLE_TYPE))) {
			t = new Table();

			String name = rs.getString(Table.TABLE_NAME);
			if (name.startsWith("_")) {
				return null;
			}

			// 表名
			t.setName(rs.getString(Table.TABLE_NAME).toLowerCase());
			// 表类型
			t.setType(rs.getString(Table.TABLE_TYPE));
			// 表注释
//            t.setRemarks(rs.getString(Table.REMARKS));
			t.setRemarks(getTableComment(connection, name, rs.getString(1)));
			// 取各列与相应类型
			List<Column> columnList = getTablesColumns(connection, t);
			t.setColumnList(columnList);
			// 取主键及相应类型

			ResultSet pks = null;
			if (MYSQL.equals(DIALECT)) {
				pks = dbMetaData.getPrimaryKeys(null, "public", t.getName());
			} else {
				pks = dbMetaData.getPrimaryKeys(null, null, t.getName().toUpperCase());
			}

			List<Column> primaryKeyList = new ArrayList<Column>();
			while (pks.next()) {
				String columnName = pks.getString(Column.COLUMN_NAME);
				for (Column col : columnList) {
					if (columnName.toLowerCase().equals(col.getName().toLowerCase())) {
						primaryKeyList.add(col);
						break;
					}
				}
			}
			t.setPrimaryKeyList(primaryKeyList);

			// 设置主键只有一个主键的情况(主键为id)
			if (primaryKeyList.size() == 1) {
				t.setPrimaryKey(primaryKeyList.get(0));
			}
		}
		return t;
	}

	/**
	 * 获取得数据库所有的列信息.
	 */
	public List<Column> getTablesColumns(Connection connection, Table table) throws SQLException {
		ResultSet rs = null;
		if (MYSQL.equals(DIALECT)) {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			rs = dbMetaData.getColumns(null, "public", table.getName(), null);
		} else {
//            NewProxyConnection oracleConnection = (NewProxyConnection) connection;
//            T4CXAConnection connection1 = (T4CXAConnection) oracleConnection.getMetaData().getConnection();
//            connection1.setRemarksReporting(true);
			DatabaseMetaData dbMetaData = connection.getMetaData();
			rs = dbMetaData.getColumns(null, null, table.getName().toUpperCase(), null);
		}

		List<Column> columns = new ArrayList<Column>();
		while (rs.next()) {
			Column c = new Column();
			c.setName(rs.getString(Column.COLUMN_NAME).toLowerCase());
			c.setRemarks(rs.getString(Column.REMARKS));
			c.setDataType(rs.getString(Column.DATA_TYPE));
			c.setTypeName(rs.getString("TYPE_NAME"));
			c.setColumnSize(rs.getString(Column.COLUMN_SIZE));
			if (MYSQL.equals(DIALECT)) {
				c.setIsAutoincrement(rs.getString(Column.IS_AUTOINCREMENT));
			} else {
				c.setIsAutoincrement("N");
			}
			columns.add(c);
		}
		return columns;
	}

	/**
	 * 获取表的注释信息.
	 *
	 * @param connection
	 * @param tableName
	 * @param schema
	 * @return
	 */
	private String getTableComment(Connection connection, String tableName, String schema) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String comment = "";
		try {
			if (MYSQL.equals(DIALECT)) {
				String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?";
				ps = connection.prepareStatement(sql);
				ps.setString(1, tableName);
				ps.setString(2, schema);
				rs = ps.executeQuery();
				while (rs.next()) {
					comment = rs.getString(1);
					break;
				}
			} else {
				String sql = "select * from user_tab_comments WHERE TABLE_NAME = ? ";
				ps = connection.prepareStatement(sql);
				ps.setString(1, tableName);
				rs = ps.executeQuery();
				while (rs.next()) {
					comment = rs.getString(3);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comment;
	}

	/**
	 * 表名重新处理
	 * user_config => UserConfig
	 * @param table 表名
	 * @return
	 */
	public Clazz tableToClazz(Table table, String ormName) {
		if (StringUtil.isEmpty(ormName)) {
			ormName = table.getName();
		}

		// 表名
		String upperCaseName = camelNamingWithUpperCase(ormName);
		String lowerCaseName = camelNamingWithLowerCase(ormName);
		// 表注释
		String comment = table.getRemarks();
		// 表列
		List<Property> propertyList = new ArrayList<Property>();
		List<Column> columnList = table.getColumnList();
		for (Column col : columnList) {
			propertyList.add(columnToProperty(col));
		}
		// 主键
		Column primaryKey = table.getPrimaryKey();
		Property primaryProperty = columnToProperty(primaryKey);
		
		// 表转换成类对象
		Clazz clazz = new Clazz();
		clazz.setName(upperCaseName);
		clazz.setUpperCaseName(upperCaseName);
		clazz.setLowerCaseName(lowerCaseName);
		clazz.setComment(comment);
		clazz.setPropertyList(propertyList);
		clazz.setPrimaryProperty(primaryProperty);
		clazz.setTable(table);
		clazz.setOrmName(ormName);
		return clazz;
	}
	
	/**
	 * 列名重新处理
	 * user_id => userId
	 * @param column 表名
	 * @return
	 */
	public Property columnToProperty(Column column) {
		String upperCaseName = camelNamingWithUpperCase(column.getName());
		String lowerCaseName = camelNamingWithLowerCase(column.getName());
		String comment = column.getRemarks();
		String typename = column.getTypeName();
		
		// 类属性
		Property property = new Property();
		// 属性名
		property.setName(lowerCaseName);
		// 属性名(首字母大写)
		property.setUpperCaseName(upperCaseName);
		// 属性名(首字母小写)
		property.setLowerCaseName(lowerCaseName);
		// 属性注释
		property.setComment(comment);
		// 属性类型
		JdbcDataConfig cfg = (JdbcDataConfig) getConfig();
		String typeValue = (cfg.getDbtype().get(typename));
		property.setType(typeValue);
		
		property.setColumn(column);
		return property;
	}
	
	/**
	 * 骆驼命名法（首字母大写）
	 * user_config => UserConfig
	 * @param tableName 表名
	 * @return
	 */
	public String camelNamingWithUpperCase(String tableName) {
		String[] fragments = tableName.split("_");
		String result = "";
		for (String s : fragments) {
			result = result + s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		return result;
	}
	
	/**
	 * 骆驼命名法（首字母小写）
	 * user_config => userConfig
	 * @param colName 表名
	 * @return
	 */
	public String camelNamingWithLowerCase(String colName) {
		String[] fragments = colName.split("_");
		String result = "";
		for (String s : fragments) {
			result = result + s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		result = lowerCaseFirstChar(result);
		return result;
	}
	
	/**
	 * 首字母大写(UserConfig => userConfig)
	 * @param s
	 * @return
	 */
	public String lowerCaseFirstChar(String s) {
		if (isEmpty(s)) { 
			return "";
		}
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
	/**
	 * 判断操作
	 * @param s
	 * @return
	 */
	public boolean isEmpty(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}
}
