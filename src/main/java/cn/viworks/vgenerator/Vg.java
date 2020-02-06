package cn.viworks.vgenerator;

import cn.viworks.vgenerator.template.FreemarkerTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Vg {

	private String applicationXml;
	private String startTxt;

	public Vg(String defaultApplicationXml, String defaultStartTxt) {
		this.applicationXml = defaultApplicationXml;
		this.startTxt = defaultStartTxt;
	}

	public void loadAndParse() throws Exception {
		loadApplicationYml(applicationXml);

		loadTemplate();

		VgParser.parse(startTxt);
	}

	private void loadTemplate() {
		try {
			FreemarkerTemplate.instance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadApplicationYml(String applicationXml) throws Exception {
		log.info("loading configure, {}", applicationXml);
		Configure.load(applicationXml);
	}
}
