create table "public"."car_park_info"
(
    "car_park_no" character varying(10) primary key,
    "address" character varying(100),
    "x" double precision,
    "y" double precision,
    "latitude" double precision,
    "longitude" double precision
)