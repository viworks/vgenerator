package cn.viworks.vgenerator.template;

import cn.viworks.vgenerator.VgTemplate;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 模板文件载入类.
 */
public class FreemarkerTemplate implements VgTemplate {
	private static final Logger logger = Logger.getLogger(FreemarkerTemplate.class);

	private static Configuration cfg;

	public void init() {

	}

	public static Configuration instance() throws IOException, URISyntaxException {
		cfg = new Configuration();

//		String templateHome = (String) Configure.get(Configure.TEMPLATE_HOME);
//		try {
//			templateHome = URLDecoder.decode(templateHome,"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}


		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		String dir = URLDecoder.decode(url.getPath(),"utf-8");

//		URL url = YamlUtil.class.getClassLoader().getResource("templates");

		logger.info("templates path:" + url.getPath());

		cfg.setDirectoryForTemplateLoading(new File(dir + "/templates"));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		return cfg;
	}

	public static Template getTemplate(String templateName) throws IOException, URISyntaxException {
		if (cfg == null) {
			cfg = FreemarkerTemplate.instance();
		}

//		URL resource = TemplateResource.class.getResource("");
//		String path = resource.getPath();
////		path = path.split("!")[0] + "!" + "/" + Constants.TEMPLATE_PATH;
//		path = Constants.TEMPLATE_PATH;
//		System.out.println(path);
		
		return cfg.getTemplate(templateName, "UTF-8");
	}
}
