create table road_split as select id, sig_cd, opert_de, rw_sn, hillshade,
 (ST_DUMP(the_geom)).geom::geometry(Polygon, 4326) as the_geom from road