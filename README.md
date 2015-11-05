# library-integration
Library Integration project
==========
This project's goal is to store and merge all of the library data in one place.
The only requirement is the data format - [MODS](http://www.loc.gov/standards/mods/)

---
## It features:

* A simple REST API for sending new data.
* Support for PostgreSQL, MariaDB.
* Near-duplicate detection algorithm based on SimHash.

---
## How to run:

1. Clone the project.
2. Update *src/webapp/database.properties* and *src/webapp/WEB-INF/db/data-source.xml* to your database settings.
3. Run `gradlew jettyRunWar` to start the Jetty server with the application. Or use `gradlew war` to create `.war` file for your favorite application server.
4. The REST URL to start adding and merging the data is `"[server]/rest/upload?path=[path to the file folder]"` (The multipart is to be implemented).

## P.S.
There are also tasks in gradle for creating Intellij IDEA and Eclipse projects.
