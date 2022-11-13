CREATE TABLE IF NOT EXISTS admin_sector (
  the_geom geometry(MultiPolygon, 4326),
  adm_sect_cd integer,
  sgg_nm character varying(60),
  sgg_oid integer,
  col_adm_se character varying(5),
  gid integer primary key
 )