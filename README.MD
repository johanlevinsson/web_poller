# Web Poller

## Setup
1. Start the MySql docker on port 3309.
2. Build using gradle, then run using bootRun. This will serve a rest api on localhost:8080/api/
```
.\gradlew.bat build
.\gradlew.bat bootRun
```
3. Either serve `frontend/` or just open `frontend/index.html` in your browser.
```
cd frontend/
http-server -p 8081
```

## Use
1. At the top you can enter a url (including `http://`) and press Add. (Currently the only statuses are a green OK if they return HTTP code 200, and a red FAIL upon any other reply)
2. You can also delete any url that you no longer wish to monitor.
3. To update the name and url of an entry, press edit fill out the new name/url and Save changes.