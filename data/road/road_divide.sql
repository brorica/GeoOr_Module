CREATE TABLE IF NOT EXISTS road_divide (
  id integer primary key generated always as identity,
  origin_id integer,
  the_geom geometry(Polygon, 4326)
)