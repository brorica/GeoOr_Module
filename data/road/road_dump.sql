create table road_dump as select id
 (ST_DUMP(the_geom)).geom::geometry(Polygon, 4326) as the_geom from road