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
# Version 1.0
1. Clone the project.
2. Update *src/webapp/database.properties* and *src/webapp/WEB-INF/db/data-source.xml* to your database settings.
3. Run `gradlew jettyRunWar` to start the Jetty server with the application. Or use `gradlew war` to create `.war` file for your favorite application server.
4. The REST URL to start adding and merging the data is `"[server]/rest/upload?path=[path to the file folder]"` (The multipart is to be implemented).
# Version 1.1
Version 1.1 is a console application, this was changed due to overhead of a web service.
Requirements:
1. Redis for Windows - in-memory database.
2. Java 8+.

Commands:
1. `-parseInit [path]`- parses directory for mods XML files and extract data needed for the program. A file `docs.blob` is going to be created and filled with this data.
2. `-find`- parses the docs blob-file and creates `duplicates.blob` file with first duplicate filtered results.
3. `-collect` - collects the `duplicates.blob` result to create final results in file. For this you need to setup redis. The application is configured for default redis settings.
4. `-getstat` - calculates different statistics for found duplicates.
## IDE import
There are tasks in gradle for creating Intellij IDEA and Eclipse projects. `gradlew idea` and `gradle eclipse`.
