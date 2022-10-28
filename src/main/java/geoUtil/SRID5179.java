package geoUtil;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class SRID5179 {

    private CoordinateReferenceSystem sourceCrs;
    private CoordinateReferenceSystem targetCrs;
    private MathTransform engine;

    public SRID5179() {
        try {
            // reference : http://www.gisdeveloper.co.kr/?p=8942
            sourceCrs = CRS.decode("EPSG:5179");
            targetCrs = CRS.decode("EPSG:4326");
            engine = CRS.findMathTransform(sourceCrs, targetCrs);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    // EPSG 5179 -> EPSG 4326 변환
    public Geometry convertGeometry(Geometry geometry) {
        Geometry transform = null;
        try {
            geometry.apply(new InvertCoordinateFilter());
            transform = JTS.transform(geometry, engine);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        return transform;
    }

}
