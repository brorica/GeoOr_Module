package domain;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

public class Shp implements Closeable {

    private File file;
    // shp 파일을 close 처리 할 때만 사용.
    private DataStore dataStore;
    // shp 스키마 까지 보는데 사용
    private FeatureSource<SimpleFeatureType, SimpleFeature> source;
    // shp 내부 데이터만 보는데 사용
    private FeatureIterator<SimpleFeature> feature;

    public Shp(File file) throws IOException {
        this.file = file;
        setDataStore(file);
        setSource();
        setFeature();
    }

    private void setDataStore(File file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());
        map.put("charset", "EUC-KR");
        dataStore = DataStoreFinder.getDataStore(map);
    }

    private void setSource() throws IOException {
        source = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
    }

    private void setFeature() throws IOException {
        Filter filter = Filter.INCLUDE;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
        collection = source.getFeatures(filter);
        feature = collection.features();
    }

    public List<AttributeDescriptor> getAttributeNames() {
        SimpleFeatureType schema = source.getSchema();
        return schema.getAttributeDescriptors();
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public void close() {
        feature.close();
        dataStore.dispose();
    }

    public FeatureIterator<SimpleFeature> getFeature() {
        return feature;
    }

}
