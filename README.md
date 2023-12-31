# car-park-management

## Prerequisite
Before using this project, you need :
* Register an account to [One Map API](https://www.onemap.gov.sg/apidocs/apidocs) to use coordinate API converter
* After you get token from [One Map API](https://www.onemap.gov.sg/apidocs/apidocs), register the token to your local environment variable with name `ONEMAP_SG_TOKEN`
* Or, if you are lazy, you can just change value in `application.yml` in car-park-scheduler. change `${ONEMAP_SG_TOKEN}` with your token. (But, it's not recommended for security matter)
```properties
one-map-sg:
  url: 'https://www.onemap.gov.sg/api/common/convert/3414to4326'
  token: ${ONEMAP_SG_TOKEN}
```
* Install Redis and PostgreSQL container. You can use `prerequisite-compose.yml` file inside `prerequisite` directory. Use this command to install
```
docker-compose -f prerequisite.yml up -d
```
* to check inf installation success, you can use `docker ps` command. You will get an output like below if installation succeed.
```
$ docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED        STATUS        PORTS                                           NAMES
03b18009ca79   redis:latest   "docker-entrypoint.s…"   12 hours ago   Up 12 hours   0.0.0.0:6379->6379/tcp, :::6379->6379/tcp       redis-car-park
9ac52aa0344b   postgres:12    "docker-entrypoint.s…"   36 hours ago   Up 26 hours   0.0.0.0:5445->5432/tcp, :::5445->5432/tcp       postgres-car-park
```

## Project Architecture
<p align="center">
  <img src="./static/project-diagram.png">
</p>

### Explanation
* We separate this project in to 2 microservices
  * `car-park-api`
  * `car-park-scheduler`
* `car-park-api` responsibilties are :
  * provides end-user REST API (the `GET /carparks/nearest?latitude=1.37326&longitude=103.897&page=1&per_page=3` request)
  * retrieve parking lots availability that is stored in Redis.
* `car-park-scheduler` responsibilties are :
  * populates parking lots data from csv to PostgreSQL (that will act as the data pattern).
  * provides scheduler to populate data pattern
    * the scheduler will run every 2 minutes
    * the scheduler will call [One Map API](https://www.onemap.gov.sg/apidocs/apidocs) 240 times every 2 minutes.
    * we set 240 times due to request/min limit from [One Map API](https://www.onemap.gov.sg/apidocs/apidocs).
    * this scheduler will compare the data from csv and the coordinate comparison results, then store is in PostgreSQL that will be used as data reference in the future.
    * if we already populates all data from the csv file (2214 data), then the scheduler will do nothing.
    * **PS** : it will take some times to populates the data (around 18 mins) in you run this project for the first time.
  * provides scheduler to populate parking lots availability from this [API](https://api.data.gov.sg/v1/transport/carpark-availability) .
    * the scheduler will run every 15 minutes to update lots availability data.
    * we utilize Redis Geospatial feature to store the data.

### Reason for using Redis and PostgreSQL
* Redis is cache database that has a best fit to apply with a data that frequently accessed and modified. Especially, if the application targets many users to access (i.e. B2C applications).
* We utilize PostgreSQL to store the data pattern for parking lots information because :
  * We need to store the data conversion for coordinates
  * The data will persist relatively longer.
  * Minimize hit to converter API (because of hits/min limit and performance issue).

## How to run
* If you run this app for the first time, you need to build the `car-par-scheduler` first before the `car-park-api` because `car-park-schduler` will run `flyWy` database migration to create the schema.
* Then, the scheduler will populate data pattern (the car parks pattern data) to PostgreSQL> You need to wait.
* But, if you are in hurry, I provided you the sql dumps that you can populate inside `prerequisite` folder (`sql-dumps.sql`). You can populate manually using this dumps.
* I provided 2 profiles for this application for both `car-par-scheduler` and `car-par-api` :
  * default (`application.yml`)
  * local (`application-local.yml`)
* You must choose between the profile. My intention for this is to provide a separate profile to run in container and local. If I have a time to provide the containerization.
* I prefer you to use `local` profile to run locally.
* You can use your favourite IDE to run this application. (mine is IntelliJ).


The example of API request is like this :
```
GET  http://localhost:9090/carpark/nearest?latitude=1.3035&longitude=103.76397&page=1&per_page=5
```
Then you will get response such us this :
```json
[
    {
        "carParkNo": "C38",
        "address": "BLK 715-717 CLEMENTI WEST STREET 2",
        "latitude": 1.3028066678186696,
        "longitude": 103.76252679035993,
        "lotsAvailable": 64
    },
    {
        "carParkNo": "C37",
        "address": "BLK 720-727,730-731 CLEMENTI WEST STREET 2",
        "latitude": 1.3035383171263104,
        "longitude": 103.76397169772991,
        "lotsAvailable": 228
    },
    {
        "carParkNo": "C36",
        "address": "BLK 728/729 CLEMENTI WEST STREET 2",
        "latitude": 1.3053096745211659,
        "longitude": 103.76355742121389,
        "lotsAvailable": 14
    },
    {
        "carParkNo": "C35",
        "address": "BLK 704-714 CLEMENTI WEST STREET 2",
        "latitude": 1.3056123024137545,
        "longitude": 103.76143922562332,
        "lotsAvailable": 225
    },
    {
        "carParkNo": "C34",
        "address": "BLK 702/703 WEST COAST ROAD",
        "latitude": 1.3070103153063926,
        "longitude": 103.76093627015362,
        "lotsAvailable": 37
    }
]
```

**PS NOTE! IMPORTANT!!** Please follow all the steps to make this project runnable!