package geoUtil;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class SRID {

    private CoordinateReferenceSystem sourceCrs;    // EPSG 4326
    private CoordinateReferenceSystem targetCrs;    // EPSG 5181
    private MathTransform engine;

    public SRID() {
        try {
            // reference : http://www.gisdeveloper.co.kr/?p=8942
            sourceCrs = CRS.decode("EPSG:4326");
            targetCrs = CRS.decode("EPSG:5181");
            engine = CRS.findMathTransform(sourceCrs, targetCrs);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }
    // EPSG 4326 -> EPSG 5181 변환
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
