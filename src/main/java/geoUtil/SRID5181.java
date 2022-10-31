package geoUtil;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class SRID5181 {

    // prj 파일에 있는 내용임
    private final String wkt5174 = "PROJCS[\"Korea 2000 / Central Belt\", \n"
        + "  GEOGCS[\"Korea 2000\", \n"
        + "    DATUM[\"Geocentric datum of Korea\", \n"
        + "      SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], \n"
        + "      TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], \n"
        + "      AUTHORITY[\"EPSG\",\"6737\"]], \n"
        + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n"
        + "    UNIT[\"degree\", 0.017453292519943295], \n"
        + "    AXIS[\"Geodetic longitude\", EAST], \n"
        + "    AXIS[\"Geodetic latitude\", NORTH], \n"
        + "    AUTHORITY[\"EPSG\",\"4737\"]], \n"
        + "  PROJECTION[\"Transverse_Mercator\"], \n"
        + "  PARAMETER[\"central_meridian\", 127.0], \n"
        + "  PARAMETER[\"latitude_of_origin\", 38.0], \n"
        + "  PARAMETER[\"scale_factor\", 1.0], \n"
        + "  PARAMETER[\"false_easting\", 200000.0], \n"
        + "  PARAMETER[\"false_northing\", 500000.0], \n"
        + "  UNIT[\"m\", 1.0], \n"
        + "  AXIS[\"Easting\", EAST], \n"
        + "  AXIS[\"Northing\", NORTH], \n"
        + "  AUTHORITY[\"EPSG\",\"5181\"]]";
    private CoordinateReferenceSystem sourceCrs;
    private CoordinateReferenceSystem targetCrs;
    private MathTransform engine;


    public SRID5181() {
        try {
            // reference : http://www.gisdeveloper.co.kr/?p=8942
            sourceCrs = CRS.parseWKT(wkt5174);
            targetCrs = CRS.decode("EPSG:4326");
            engine = CRS.findMathTransform(sourceCrs, targetCrs);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    // EPSG 5181 -> EPSG 4326 변환
    public Geometry convertGeometry(Geometry geometry) {
        Geometry transform = null;
        try {
            transform = JTS.transform(geometry, engine);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return transform;
    }
}
