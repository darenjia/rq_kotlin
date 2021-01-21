package com.bkjcb.rqapplication.Map.Bean;

/**
 * Created by DengShuai on 2019/3/20.
 * Description :点设施类
 */
public class EquipmentPoint extends MapData {
    private LocationPosition position;

    public EquipmentPoint(){

    }

    public EquipmentPoint(LocationPosition position) {
        this.position = position;
    }

    public LocationPosition getPosition() {
        return position;
    }

    public void setPosition(LocationPosition position) {
        this.position = position;
    }

    @Override
    public int getType() {
        return POINT;
    }
}
