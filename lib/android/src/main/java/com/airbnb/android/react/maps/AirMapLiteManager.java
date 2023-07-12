package com.airbnb.android.react.maps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.android.gms.maps.GoogleMapOptions;

/**
 * AirMapLiteManager是一个AirMapManager的子类，用于创建一个Google地图视图，该视图使用Google Maps Lite Mode。
 */
public class AirMapLiteManager extends AirMapManager {

    private static final String REACT_CLASS = "AIRMapLite";

    /**
     * 返回视图的名称，这将用于在JavaScript中引用此视图。
     */
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /**
     * 创建一个新的AirMapLiteManager实例。
     */
    public AirMapLiteManager(ReactApplicationContext context) {
        super(context);
        this.googleMapOptions = new GoogleMapOptions().liteMode(true);
    }

}