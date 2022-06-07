package geoUtil;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class SRID {

    private CoordinateReferenceSystem sourceCrs;    // EPSG 4326
    private CoordinateReferenceSystem targetCrs;    // EPSG 5181
    private MathTransform engine;
    // prj 파일에 있는 내용임
    private final String wkt5174 = "PROJCS[\"Korean 1985 / Modified Central Belt\", "
        + "GEOGCS[\"Korean 1985\", "
        + "DATUM[\"Korean Datum 1985\", "
        + "SPHEROID[\"Bessel 1841\", 6377397.155, 299.1528128, "
        + "AUTHORITY[\"EPSG\",\"7004\"]], "
        + "TOWGS84[-115.8, 474.99, 674.11, 1.16, -2.31, -1.63, 6.43], "
        + "AUTHORITY[\"EPSG\",\"6162\"]], "
        + "PRIMEM[\"Greenwich\", 0.0, "
        + "AUTHORITY[\"EPSG\",\"8901\"]], "
        + "UNIT[\"degree\", 0.017453292519943295], "
        + "AXIS[\"Geodetic longitude\", EAST], "
        + "AXIS[\"Geodetic latitude\", NORTH], "
        + "AUTHORITY[\"EPSG\",\"4162\"]], "
        + "PROJECTION[\"Transverse_Mercator\"], "
        + "PARAMETER[\"central_meridian\", 127.00289027777775], "
        + "PARAMETER[\"latitude_of_origin\", 38.0], "
        + "PARAMETER[\"scale_factor\", 1.0], "
        + "PARAMETER[\"false_easting\", 200000.0], "
        + "PARAMETER[\"false_northing\", 500000.0], "
        + "UNIT[\"m\", 1.0], "
        + "AXIS[\"Easting\", EAST], "
        + "AXIS[\"Northing\", NORTH], "
        + "AUTHORITY[\"EPSG\",\"5174\"]]";


    public SRID() {
        try {
            // reference : http://www.gisdeveloper.co.kr/?p=8942
            sourceCrs = CRS.parseWKT(wkt5174);
            targetCrs = CRS.decode("EPSG:4326");
            engine = CRS.findMathTransform(sourceCrs, targetCrs);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    // EPSG 5174 -> EPSG 4326 변환
    public Geometry revertGeometry(Geometry geometry) {
        Geometry transform = null;
        try {
            transform = JTS.transform(geometry, engine);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return transform;
    }
}
