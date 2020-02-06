package cn.viworks.vgenerator;

public class VgApp {
    public static void main(String[] agrs) throws Exception {
        Vg vg = new Vg("application.yml", "vstart.txt");
        vg.loadAndParse();
    }
}
