package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY;

/**
 * Created on 06/20 2021.
 *
 * @author Bennie
 */
public class Main {

    static {
        System.setProperty(DEFAULT_LOG_LEVEL_KEY, "INFO");
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        System.out.println("configs: " + Arrays.toString(args));
        Config.loadConfig(args);
        download();
    }

    static void download() throws Exception {
        Config config       = Config.getInstance();
        String downloadList = config.getDownloadList();

        Path            path = Paths.get(downloadList);
        ExecutorService pool = Executors.newCachedThreadPool();

        try (Stream<String> lines = Files.lines(path, Charset.defaultCharset())) {

            List<Downloader> collect = lines
                    .distinct()
                    .filter(line -> !line.startsWith("#"))
                    .map(line -> new Downloader(line))
                    .collect(Collectors.toList());

            pool.invokeAll(collect);
        } catch (Exception e) {
            logger.error("error", e);
        }
    }


}