package cn.viworks.vgenerator.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;

public class YamlUtil {
    private static InputStream load(String path) throws IOException {
        if (path.startsWith("http")) {
            URL url = new URL(path);
            return url.openStream();
        } else if (path.startsWith("/")) {
            return new FileInputStream(path);
        } else {
            return YamlUtil.class.getClassLoader().getResourceAsStream(path);
        }
    }

    public static <T> T load(String path, Class<T> clz) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = load(path);
            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, clz);
        } finally {
            inputStream.close();
        }
    }


    public static <T> T loadAs(String path, Class<T> clz) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = load(path);
            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, clz);
        } finally {
            inputStream.close();
        }
    }
}
