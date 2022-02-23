package webPoller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webPoller.db.UrlStatusObject;
import webPoller.service.WatchlistService;

@RestController
@ResponseBody
@RequestMapping("/api")
public class RestApiController {
    @Autowired
    private WatchlistService watchlistService;

    /**
     * Fetches all url status entries from the database
     * @return an array of url entries, empty when no entries
     */
    @GetMapping(value = "/all")
    Iterable<UrlStatusObject> all() {
        return watchlistService.getAllUrlStatuses();
    }

    /**
     * Saves a url with a name to the watch list.
     * @param urlObject a json object with keys url and name.
     * @return all monitored url entries (including the one just added).
     */
    @GetMapping(value = "/add")
    Iterable<UrlStatusObject> addUrl(@RequestParam String urlObject) {
        JSONObject json = new JSONObject(urlObject);
        return watchlistService.addUrl(json.getString("url"), json.getString("name"));
    }

    /**
     * Updates an entry in the database, if it exists.
     * @param updateUrlObject
     * @return all monitored url entries.
     */
    @GetMapping(value = "/update")
    Iterable<UrlStatusObject> updateUrl(@RequestParam String updateUrlObject) {
        JSONObject json = new JSONObject(updateUrlObject);
        return watchlistService.updateUrl(json.getString("url"), json.getString("name"), json.getInt("id"));
    }

    /**
     * Removes a url entry from the database.
     * @param id of the desired entry.
     * @return all monitored url entries (excluding the one just deleted).
     */
    @PostMapping(value = "/remove/{id}")
    Iterable<UrlStatusObject> removeById(@PathVariable("id") int id) {
        return watchlistService.removeById(id);
    }
}
