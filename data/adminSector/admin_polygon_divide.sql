insert into admin_sector_divide(the_geom, adm_sect_cd)
 select ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)), adm_sect_cd from admin_sector
