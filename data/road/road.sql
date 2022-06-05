 CREATE TABLE road (
  id SERIAL PRIMARY KEY,
  geom geometry(MultiPolygon),
  opert_de character varying(14),
  rw_sn double precision,
  sig_cd character varying(14),
  hillshade integer default 0)