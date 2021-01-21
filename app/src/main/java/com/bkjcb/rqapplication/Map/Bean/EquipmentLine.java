package com.bkjcb.rqapplication.Map.Bean;

import com.esri.arcgisruntime.geometry.Polyline;

import java.util.ArrayList;

/**
 * Created by DengShuai on 2019/3/20.
 * Description :
 */
public class EquipmentLine extends MapData {
    private ArrayList<EquipmentPoint> line;
    private Polyline polyline;

    public EquipmentLine() {

    }

    public EquipmentLine(Polyline polyline) {
        this.polyline = polyline;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    @Override
    public int getType() {
        return LINE;
    }

    public ArrayList<EquipmentPoint> getLine() {
        return line;
    }

    public void setLine(ArrayList<EquipmentPoint> line) {
        this.line = line;
    }

    public void addPoint(EquipmentPoint point) {
        if (this.line == null) {
            this.line = new ArrayList<>();
        }
        this.line.add(point);
    }
}
