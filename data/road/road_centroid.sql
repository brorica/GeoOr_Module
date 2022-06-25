CREATE TABLE IF NOT EXISTS road_centroid (
  id integer primary key generated always as identity,
  centroid geometry(Point, 4326),
  sig_cd character varying(14)
)