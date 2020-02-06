package cn.viworks.vgenerator.data;

import cn.viworks.vgenerator.Configure;
import cn.viworks.vgenerator.VgData;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源获取工厂类.
 */
@Slf4j
public class DataSourceFactory {

    private static DataSource ds = null;

	private static DataSourceFactory instance = new DataSourceFactory();

	private JdbcDataConfig cfg;
	private DataSourceFactory() {
	}
	
	public static DataSourceFactory instance() {
		return instance;
	}

    public void init(JdbcDataConfig cfg) {
        if (ds == null) {
        	this.cfg = cfg;
            ds = druidDataSourceInstance();
        }
    }

	public DataSource getDataSource() {
		log.info("init datasource...");
		if (ds == null) {
			ds = druidDataSourceInstance();
		}
		return ds;
	}

	public DataSource druidDataSourceInstance() {
		DruidDataSource druid = new DruidDataSource();
		druid.setDriverClassName(cfg.getDriver());
		druid.setUrl(cfg.getUrl());
		druid.setUsername(cfg.getUsername());
		druid.setPassword(cfg.getPassword());

		// the settings below are optional -- c3p0 can work with defaults
		druid.setMinIdle(5);
		druid.setInitialSize(5);
		druid.setMaxActive(20);

		// 处理oracle获取列字段注释
		Properties properties = new Properties();
		properties.setProperty("remarksReporting", "true");
		druid.setConnectProperties(properties);
		return druid;
	}
}
