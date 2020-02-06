package cn.viworks.vgenerator;

import java.util.Map;

public abstract class VgData {

    private Config config;

    private String source;

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public abstract void init();
}
