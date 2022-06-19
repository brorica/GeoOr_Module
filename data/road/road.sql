CREATE TABLE IF NOT EXISTS road (
  id integer primary key generated always as identity,
  polygon geometry(MultiPolygon),
  opert_de character varying(14),
  rw_sn double precision,
  sig_cd character varying(14),
  hillshade integer default 0)