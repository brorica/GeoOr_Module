CREATE TABLE IF NOT EXISTS road_centroid (
  id integer primary key generated always as identity,
  centroid geometry(Point),
  sig_cd character varying(14)
)