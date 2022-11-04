CREATE TABLE IF NOT EXISTS road_segment (
  origin_id integer,
  sig_cd character varying(14),
  the_geom geometry(Polygon, 4326)
)