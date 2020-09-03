package com.bkjcb.rqapplication.Map;


import com.bkjcb.rqapplication.Map.Bean.EquipmentLine;
import com.bkjcb.rqapplication.Map.Bean.EquipmentPoint;
import com.bkjcb.rqapplication.Map.Bean.LocationPosition;
import com.bkjcb.rqapplication.Map.Bean.MapData;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DengShuai on 2019/3/19.
 * Description :
 */
public class ArcGisSearchUtil {

    private static final String PIPELINE_QUERY_URL = "http://180.167.216.66:6080/arcgis/rest/services/js_zong/MapServer/0";
    private int searchAroundStatus = 0;
    SearchStatus searchStatus;
    private ArrayList<MapData> data;
    private ListenableFuture<FeatureQueryResult> queryResult;
    private Point mapPoint;

    public interface SearchStatus {
        void searchSuccess(ArrayList<MapData> locationPositions, int type);

        void searchFailed();
    }

    public ArcGisSearchUtil() {
    }

    public ArcGisSearchUtil(SearchStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public void search(double x, double y) {
        Point mapPoint = new Point(x, y);
        if (queryResult != null && queryResult.isDone()) {
            queryResult.cancel(true);
        }

        ServiceFeatureTable featureTable = new ServiceFeatureTable(PIPELINE_QUERY_URL);
        QueryParameters query = new QueryParameters();
        int tolerance = 500;
        Envelope envelope = new Envelope(mapPoint, tolerance, tolerance);
        //Polygon polygon = getCircle(mapPoint, mapTolerance);
        query.setGeometry(envelope);
        data = new ArrayList<>();
        queryResult = featureTable.queryFeaturesAsync(query, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        queryResult.addDoneListener(() -> {

            try {
                FeatureQueryResult result = queryResult.get();
                for (Feature feature : result) {
                    if (feature.getGeometry().getGeometryType() == GeometryType.POLYLINE) {
                        Polyline polyline = (Polyline) feature.getGeometry();
                        EquipmentLine equipmentLine = new EquipmentLine(polyline);
                        for (Point p : polyline.getParts().getPartsAsPoints()) {
                            equipmentLine.addPoint(new EquipmentPoint(new LocationPosition(p.getX(), p.getY())));
                        }
                        equipmentLine.setEquimentCode((String) feature.getAttributes().get("PipeID"));
                        equipmentLine.setName((String) feature.getAttributes().get("TypeName"));
                        data.add(equipmentLine);
                    }
                    //searchResults.add((String) feature.getAttributes().get("部件编码"));
                }
                searchStatus.searchSuccess(data, 1);

            } catch (Exception ex) {
                ex.printStackTrace();
                searchStatus.searchFailed();
            }

        });

    }


    public void cancel() {
        if (queryResult != null && queryResult.isDone()) {
            queryResult.cancel(true);
        }
    }

    private Polygon getCircle(Point point, double radius) {
        //        polygon.setEmpty();
        Point[] points = getPoints(point, radius);
        PointCollection mPointCollection = new PointCollection(SpatialReferences.getWebMercator());
        mPointCollection.clear();
        mPointCollection.addAll(Arrays.asList(points));

        return new Polygon(mPointCollection);
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     *
     * @param center
     * @param radius
     * @return
     */
    private static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

}
