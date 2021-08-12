package demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.Callable;

import static demo.Constants.UA;

/**
 * Created on 06/27 2021.
 *
 * @author Bennie
 */
public class Downloader implements Callable<Object> {

    private static final Logger logger = LoggerFactory.getLogger(Downloader.class);

    private       String videoUrl;
    private final Config config;

    public Downloader() {
        config = Config.getInstance();
    }

    public Downloader(String videoUrl) {
        this();
        this.videoUrl = videoUrl;
    }

    private String parseVideoSource(Document doc) throws UnsupportedEncodingException {
        String videoEleStr = doc.select("video > script").html();
        String beginStr    = "strencode2(\"";
        int    begin       = videoEleStr.indexOf("strencode2(\"");
        int    end         = videoEleStr.indexOf("\"));");

        String substring = videoEleStr.substring(begin + beginStr.length(), end);
        logger.debug("Substring: {}", substring);

        String source = URLDecoder.decode(substring, "UTF-8");
        logger.debug("Source: {}", source);

        Document parse   = Jsoup.parse(source);
        String   m3u8Src = parse.select("source").attr("src");
        logger.debug("M3u8 src: {}", m3u8Src);
        return m3u8Src;
    }

    private Document getDoc(String url) throws Exception {
        return Jsoup.connect(url)
                .userAgent(UA)
                .header("Accept-Language", "zh-CN")
                .get();
    }


    private void outputToMp4(String title, String m3u8Url) throws Exception {
        String downloadDir = config.getDownloadDir() + LocalDate.now() + "/";

        Files.createDirectories(Paths.get(downloadDir));
        String mp4Path = new StringBuilder(downloadDir)
                .append(title)
                .append(".mp4").toString();
        Files.deleteIfExists(Paths.get(mp4Path));

        String _cmd = "ffmpeg -i '%s' -acodec copy -vcodec copy '%s'";
        String cmd  = String.format(_cmd, m3u8Url, mp4Path);
        logger.debug("cmd: {}", cmd);

        ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", cmd);
        Process        p       = builder.start();
        logger.info("Working on '{}', please wait for a while...", title);

        int exitValue;
        try {
            exitValue = p.waitFor();
            logger.debug("Processor returning code {}", exitValue);
            if (exitValue == 0) logger.info("Download finished, Saved on '{}'.", mp4Path);
        } catch (InterruptedException e) {
            logger.error("error", e);
        }


    }

    @Override
    public Object call() throws Exception {
        Document doc;
        try {
            doc = getDoc(videoUrl);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (doc.location().contains("404.html")) {
            logger.info("Video has been deleted.");
            return "Video has been deleted.";
        }

        String title = doc.title().replace(" Chinese homemade video", "").trim();
        logger.info("Video '{}[{}]'", title, videoUrl);
        String m3u8Src = parseVideoSource(doc);

        outputToMp4(title, m3u8Src);

        System.out.println();
        return "Task end!";
    }

}
