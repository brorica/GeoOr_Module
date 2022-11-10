CREATE TABLE IF NOT EXISTS road (
  id integer primary key generated always as identity,
  the_geom geometry(MultiPolygon, 4326),
  opert_de character varying(14),
  rw_sn double precision,
  sig_cd integer,
  hillshade integer default 0)