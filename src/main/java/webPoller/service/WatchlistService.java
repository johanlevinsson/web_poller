package webPoller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webPoller.db.UrlStatusObject;
import webPoller.db.UrlStatusRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class WatchlistService {
    private static final long SECONDS_BETWEEN_UPDATES = 10;

    @Autowired
    private UrlStatusRepository urlStatusRepository;

    public Iterable<UrlStatusObject> getAllUrlStatuses() {
        Iterable<UrlStatusObject> result = null;
        try {
            result = urlStatusRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public Iterable<UrlStatusObject> addUrl(String urlString, String name) {
        try {
            urlStatusRepository.save(new UrlStatusObject(urlString, getResponseCode(urlString), name));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return getAllUrlStatuses();
    }

    public Iterable<UrlStatusObject> updateUrl(String url, String name, int id) {
        try {
            Optional<UrlStatusObject> urlStatusObject = urlStatusRepository.findById(id);
            if (urlStatusObject.isPresent()) {
                urlStatusObject.get().setUrl(url);
                urlStatusObject.get().setName(name);
                urlStatusRepository.save(urlStatusObject.get());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return getAllUrlStatuses();
    }

    public Iterable<UrlStatusObject> removeById(int id) {
        urlStatusRepository.deleteById(id);
        return getAllUrlStatuses();
    }

    /**
     * init will repeat forever and update every status code every SECONDS_BETWEEN_UPDATES seconds
     */
    @PostConstruct
    private void init() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateAllStatuses();
            }
        }, 0, SECONDS_BETWEEN_UPDATES * 1000);
    }

    private void updateAllStatuses() {
        for (UrlStatusObject urlStatusObject : urlStatusRepository.findAll()) {
            try {
                urlStatusObject.setCode(getResponseCode(urlStatusObject.getUrl()));
                urlStatusRepository.save(urlStatusObject);
            } catch (Error e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
    private int getResponseCode(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        }

        int code = -1;
        try {
            code = openConnection(url).getResponseCode();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return code;
    }

    private HttpURLConnection openConnection(URL url) {
        assert url != null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return connection;
    }
}
