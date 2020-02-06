package cn.viworks.vgenerator;

import cn.viworks.vgenerator.generator.GeneratorConfig;
import cn.viworks.vgenerator.utils.YamlUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Configure {

	private final Logger logger = Logger.getLogger(Configure.class);

	public final static String DATA = "data";
	public final static String GENERTOR = "generator";
	public final static String PROJECT_HOME = "outputhome";
	public final static String TEMPLATE_HOME = "templatehome";

//	private Map<String, Map<String, Object>> dataCfgMap = new LinkedHashMap<String, Map<String, Object>>();
//	private Map<String, VgData> dataObjMap = new LinkedHashMap<String, VgData>();
//	private Map<String, GeneratorCfg> genCfgMap = new LinkedHashMap<String, GeneratorCfg>();
//	private Map<String, Generator> genCmdMap = new LinkedHashMap<String, Generator>();

	private static Map<String, Object> propeties = new LinkedHashMap<String, Object>();

	public static void load(String applicationXml) throws VgException {
		Map map = null;
		try {
			map = YamlUtil.load(applicationXml, Map.class);
		} catch (IOException e) {
			throw new VgException(e);
		}

		Map dataMap = (Map) map.get(DATA);
		Map<String, Config> dataCfgMap = new LinkedHashMap<String, Config>();
		propeties.put("data_cfg", dataCfgMap);

		Map<String, VgData> dataObjMap = new LinkedHashMap<String, VgData>();
		propeties.put("data_obj", dataObjMap);

		Map<String, GeneratorConfig> genCfgMap = new LinkedHashMap<String, GeneratorConfig>();
		propeties.put("gen_cfg", genCfgMap);

		Map<String, Generator> genCmdMap = new LinkedHashMap<String, Generator>();
		propeties.put("gen_cmd", genCmdMap);

		for (Object item : dataMap.keySet()) {
			try {
				Class dataCfgClass = Class.forName(((String) item + "Config"));
				Config dataCfg = JSONObject.parseObject(JSONObject.toJSONString(dataMap.get(item)), (Type) dataCfgClass);

				dataCfgMap.put(dataCfg.getAlias(), dataCfg);

				Class dataClass = Class.forName((String) item);
				VgData vgData = (VgData) dataClass.newInstance();
				vgData.setConfig(dataCfg);
				vgData.setSource(dataCfg.getAlias());
				vgData.init();
				dataObjMap.put(dataCfg.getAlias(), vgData);
			} catch (ClassNotFoundException e) {
				throw new VgException(e);
			} catch (IllegalAccessException e) {
				throw new VgException(e);
			} catch (InstantiationException e) {
				throw new VgException(e);
			}
		}

		Map genMap = (Map) map.get(GENERTOR);
		for (Object item : genMap.keySet()) {
			String clazz = (String) item;
			try {
				GeneratorConfig cfg = JSONObject.parseObject(JSONObject.toJSONString(genMap.get(item)), GeneratorConfig.class);
				String source = cfg.getSource();
				genCfgMap.put(item.toString(), cfg);

				Class generatorClass = Class.forName(clazz);
				Generator generator = (Generator) generatorClass.newInstance();
				generator.setGenCfg(cfg);
				generator.setSource(source);
				generator.setVgData(dataObjMap.get(source));

				if (cfg.getFile() != null) {
					for (String cmd : cfg.getFile().keySet()) {
						genCmdMap.put(cmd, generator);
					}
				}

				if (cfg.getFiles() != null) {
					for (String cmds : cfg.getFiles().keySet()) {
						genCmdMap.put(cmds, generator);
					}
				}
			} catch (ClassNotFoundException e) {
				throw new VgException(e);
			} catch (IllegalAccessException e) {
				throw new VgException(e);
			} catch (InstantiationException e) {
				throw new VgException(e);
			}
		}

		for (Object key : map.keySet()) {
			set((String) key, map.get(key));
		}
	}

	public static Object get(String key) {
		return propeties.get(key);
	}

	public static void set(String key, Object value) {
		propeties.put(key, value);
	}

	public static Map<String, Map<String, Object>> getDataCfgMap() {
		return (Map<String, Map<String, Object>>)get("data_cfg");
	}

	public static Map<String, GeneratorConfig> getGenCfgMap() {
		return (Map<String, GeneratorConfig>)get("gen_cfg");
	}

	public static Map<String, Generator> getGenCmdMap() {
		return (Map<String, Generator>)get("gen_cmd");
	}

	//	public String getJdbcDriver() {
//		Map<String, Object> map = (Map<String, Object>) propeties.get(JDBC);
//		return (String) map.get("driver");
//	}
//
//	public String getJdbcUrl() {
//		Map<String, Object> map = (Map<String, Object>) propeties.get(JDBC);
//		return (String) map.get("url");
//	}
//
//	public String getJdbcUsername() {
//		Map<String, Object> map = (Map<String, Object>) propeties.get(JDBC);
//		return (String) map.get("username");
//	}
//
//	public String getJdbcPassword() {
//		Map<String, Object> map = (Map<String, Object>) propeties.get(JDBC);
//		return (String) map.get("password");
//	}
//
//	public String getDbtypeValue(String item) {
//		Map<String, Object> map = (Map<String, Object>) propeties.get(DBTYPE);
//		return (String) map.get(item);
//	}
//
//	public boolean isSingleCmd(String cmd) {
//		Map<String, Object> map = (Map<String, Object>)((Map<String, Object>) propeties.get(CMD)).get("file");
//		return map.get(cmd) != null;
//	}
//
//	public boolean isGroupCmd(String cmd) {
//		Map<String, Object> map = (Map<String, Object>)((Map<String, Object>) propeties.get(CMD)).get("files");
//		return map.get(cmd) != null;
//	}
//
//	public List<String> getMemeberCmd(String cmd) {
//		Map<String, Object> map = (Map<String, Object>)((Map<String, Object>) propeties.get(CMD)).get("files");
//		return (List<String>)map.get(cmd);
//	}
//
//	public String getProjectHome() {
//		return (String) propeties.get(PROJECT_HOME);
//	}
//
//	public String getTemplateHome() {
//		return (String) propeties.get(TEMPLATE_HOME);
//	}
//
//	public void print() {
//		for (String key : propeties.keySet()) {
//			if (JDBC.equals(key)) {
//				Map<String, Object> map = (Map<String, Object>) propeties.get(key);
//				log.info("jdbc.driver=" + (String)map.get("driver"));
//				log.info("jdbc.url=" + (String)map.get("url"));
//				log.info("jdbc.username=" + (String)map.get("username"));
//				log.info("jdbc.password=" + (String)map.get("password"));
//			}
//			if (DBTYPE.equals(key)) {
//				Map<String, Object> map = (Map<String, Object>) propeties.get(key);
//				for (String item : map.keySet())  {
//					log.info(key + "." + item + "="+ map.get(item));
//				}
//			}
//			if (PROJECT_HOME.equals(key)) {
//				log.info(key + "="+ propeties.get(key));
//			}
//			if (TEMPLATE_HOME.equals(key)) {
//				log.info(key + "="+ propeties.get(key));
//			}
//		}
//	}
}
