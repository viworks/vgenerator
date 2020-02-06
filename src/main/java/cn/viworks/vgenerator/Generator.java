package cn.viworks.vgenerator;

import cn.viworks.vgenerator.generator.GeneratorConfig;

public abstract class Generator {
    private GeneratorConfig generatorCfg;

    private String source;

    private VgData vgData;

    public void setGenCfg(GeneratorConfig generatorCfg) {
        this.generatorCfg = generatorCfg;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setVgData(VgData vgData) {
        this.vgData = vgData;
    }

    public VgData getVgData() {
        return vgData;
    }

    public GeneratorConfig getGeneratorCfg() {
        return generatorCfg;
    }

    public String getSource() {
        return source;
    }

    public abstract void run(Payload payload);
}
