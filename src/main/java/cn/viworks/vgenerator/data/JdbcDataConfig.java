package cn.viworks.vgenerator.data;

import cn.viworks.vgenerator.Config;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JdbcDataConfig extends Config {
    private String alias;
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, String> dbtype;
}
