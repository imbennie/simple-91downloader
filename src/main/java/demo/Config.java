package demo;

import java.util.HashMap;

/**
 * Created on 08/12 2021.
 *
 * @author Bennie
 */
public class Config {


    private String downloadDir;
    private String downloadList;

    private Config() {
    }


    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public String getDownloadList() {
        return downloadList;
    }

    public void setDownloadList(String downloadList) {
        this.downloadList = downloadList;
    }


    private static Config instance;

    public static Config getInstance() {
        return instance;
    }

    public static void loadConfig(String[] args) {
        HashMap<String, Object> map = new HashMap<>();

        for (String arg : args) {
            String[] config = arg.split("=");
            map.put(config[0], config[1]);
        }

        instance = new Config();
        instance.setDownloadList((String) map.get("dl_list"));
        instance.setDownloadDir((String) map.get("dl_dir"));
    }
}
