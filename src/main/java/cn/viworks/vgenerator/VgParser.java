package cn.viworks.vgenerator;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VgParser {
	private final static String REG_PARSE = "^vg\\s*([a-zA-Z_/]+)\\s*([a-zA-Z_/]+)";

	public static String getCmdTextPath(String startTxt) {

		String cmdFile = "";
		URL resource = Thread.currentThread().getContextClassLoader().getResource("");
		if (resource == null) {
			return "";
		}
		System.out.println(resource.getPath());
		String resourcePath = resource.getPath();

		try {
			resourcePath = URLDecoder.decode(resourcePath,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		int idx = resourcePath.indexOf("resources");
		if (idx > 0) {
			System.out.println(resourcePath.substring(0, idx));
			cmdFile = resourcePath.substring(0, idx) + startTxt;
		} else {
			cmdFile = resourcePath + startTxt;
		}

		return cmdFile;
	}

	public static List<String> getCmdContextList(String cmdFilePath) throws Exception {

		FileReader reader = null;
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		try {
			try {
				reader = new FileReader(cmdFilePath);
				br = new BufferedReader(reader);
				String line = "";
				Pattern p = Pattern.compile(REG_PARSE);

				while ((line = br.readLine()) != null) {
					if (line.startsWith("#")) {
						continue;
					}
					lines.add(line);
				}
			} catch (Exception e) {
				throw new VgException(e);
			}
		} finally {
			try {
				reader.close();
				br.close();
			} catch (IOException e) {
				throw new VgException(e);
			}
		}

		return lines;
	}

	public static void parse(String startTxt) throws Exception {
		String cmdFile = getCmdTextPath(startTxt);
		List<String> lines = getCmdContextList(cmdFile);
		for (String line : lines) {
			String[] args = line.split("\\s");
			int length = args.length;
			if (length < 2 ) {
				continue;
			}
			String cmd = args[1];

			List<String> params = new ArrayList<String>();

			if (args.length > 2) {
				int i = 2;
				while (i < args.length) {
					params.add(args[i]);
					i++;
				}
			}

			Payload payload = new Payload();
			payload.setCmd(cmd);
			payload.setParams(params.toArray(new String[]{}));

			Generator generator = Configure.getGenCmdMap().get(cmd);
			generator.run(payload);
		}
	}
}
