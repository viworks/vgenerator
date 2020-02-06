package cn.viworks.vgenerator.generator;

import cn.viworks.vgenerator.Config;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeneratorConfig extends Config {
    private String source;
    private Map<String, String> file;
    private Map<String, List<String>> files;
}
