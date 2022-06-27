insert into road_divide(origin_id, the_geom)
 select id, ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)) from road_dump