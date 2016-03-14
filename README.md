String REST Service
=======================
Build and Run
-------------------------------
To build and run the project artifact, type the following:
```
$ gradle build
$ java -jar build/libs/string-rest-service-1.0-SNAPSHOT.jar
```
Use
-------------------------------
Open the browser and start Swagger UI at `http://localhost:8080/swagger-ui/index.html`

Operations to implement:
------------------------
- [x] **upload** ---upload a set of strings to the server, verify its validity (it must be a set, so no duplicate strings are allowed)
- [x] **search** ---find all the sets containing given string
- [x] **delete** ---delete the specified set of strings from the server
- [x] **set_statistic** ---return the statistics for one of the previously uploaded sets (number of strings, length of the shortest string, length of the longest string, average length, median length)
- [x] **most_common** ---find the string used in the most sets; if there are a few such strings, return them all, sorted alphabetically
- [x] **longest** ---find the longest string in all the sets; if there are a few such strings, return them all, sorted alphabetically
- [x] **exactly_in** ---find the string which is present in exactly given number of sets
- [x] **create_intersection** ---create a new set of strings which is an intersection of two previously uploaded sets
- [ ] **longest_chain** ---from the previously uploaded sets find the longest chain of strings