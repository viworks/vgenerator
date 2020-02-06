package cn.viworks.vgenerator.generator;

import cn.viworks.vgenerator.Configure;
import cn.viworks.vgenerator.Generator;
import cn.viworks.vgenerator.Payload;
import cn.viworks.vgenerator.data.JdbcData;
import cn.viworks.vgenerator.data.pojo.Scaffold;
import cn.viworks.vgenerator.data.pojo.Table;
import cn.viworks.vgenerator.template.FreemarkerTemplate;
import cn.viworks.vgenerator.utils.FileUtil;
import cn.viworks.vgenerator.utils.StringUtil;
import freemarker.template.Template;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.List;

public class DefaultGenerator extends Generator {
    private static final Logger logger = Logger.getLogger(DefaultGenerator.class);

    public void run(Payload payload) {
        if (payload.getParams().length > 1) {
            System.out.println(payload.getCmd() + " " + payload.getParams()[0] + " " + payload.getParams()[1]);
        } else {
            System.out.println(payload.getCmd() + " " + payload.getParams()[0]);
        }

        String cmd = payload.getCmd();
        String[] params = payload.getParams();
        String tableName = params[0];
        String ormName = params.length > 1 ? params[1] : params[0];

        if (StringUtil.isEmpty(ormName)) {
            ormName = tableName;
        }

        JdbcData jdbcData = (JdbcData) getVgData();
        Scaffold scaffold = new Scaffold();
        if (!StringUtil.isEmpty(tableName)) {
            Table table = jdbcData.findTable(tableName);
            if (table != null) {
                scaffold.setClazz(jdbcData.tableToClazz(table, ormName));
            }
        }


        GeneratorConfig cfg = getGeneratorCfg();
        if (cfg.getFile() != null && cfg.getFile().containsKey(cmd)) {
            scaffold.setCmd(cmd);
            genFile(scaffold, (String) cfg.getFile().get(cmd));
        }

        if (cfg.getFiles() != null && cfg.getFiles().containsKey(cmd)) {
            List<String> members = cfg.getFiles().get(cmd);
            for (String member : members) {
                scaffold.setCmd(member);
                genFile(scaffold, (String) cfg.getFile().get(member));
            }
        }
    }

    private void genFile(Scaffold scaffold, String targetFile) {
        try {
            String cmd = scaffold.getCmd();

            // 模板文件
            Template template = FreemarkerTemplate.getTemplate(cmd + ".ftl");
            // 目标文件
            String outputFullPath = getOutputFullPath(scaffold, targetFile);
            if (!FileUtil.exist(outputFullPath)) {
                logger.info("create file: " + outputFullPath);
            } else {
                logger.info("update file: " + outputFullPath);
            }
            // 生成文件处理
//            logger.info(outputFullPath);
            Writer out = new OutputStreamWriter(new FileOutputStream(outputFullPath), "UTF-8");
            template.process(scaffold, out);
            out.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("genertor file error");
            throw new RuntimeException(e);
        }
    }

    private String getOutputFullPath(Scaffold scaffold, String targetFile) {
        String projectHome = (String) Configure.get(Configure.PROJECT_HOME);

        try {
            projectHome = URLDecoder.decode(projectHome,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (targetFile.indexOf("{Model}") > -1) {
            targetFile = targetFile.replace("{Model}", scaffold.getClazz().getName());
        }
        if (targetFile.indexOf("{table}") > -1) {
            targetFile = targetFile.replace("{table}", scaffold.getClazz().getTable().getName());
        }

        String path = projectHome + targetFile;

        String dir = path.substring(0, path.lastIndexOf("\\"));

        if (!FileUtil.exist(dir)) {
            FileUtil.createFolders(dir);
        }

        return path;
    }
}
