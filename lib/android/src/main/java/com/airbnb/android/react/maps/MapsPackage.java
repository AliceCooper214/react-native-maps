java
Copy
package com.airbnb.android.react.maps;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsPackage implements ReactPackage {

    public MapsPackage(Activity activity) {
    } // 为了向后兼容，保留一个 Activity 参数的构造函数

    public MapsPackage() {
    }

    // 创建原生模块
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(new AirMapModule(reactContext));
    }

    // 创建 JavaScript 模块，这里没有实现，返回空列表
    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    // 创建 ViewManager，负责管理视图
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        // 创建各种视图管理器
        AirMapCalloutManager calloutManager = new AirMapCalloutManager();
        AirMapMarkerManager annotationManager = new AirMapMarkerManager();
        AirMapPolylineManager polylineManager = new AirMapPolylineManager(reactContext);
        AirMapPolygonManager polygonManager = new AirMapPolygonManager(reactContext);
        AirMapCircleManager circleManager = new AirMapCircleManager(reactContext);
        AirMapManager mapManager = new AirMapManager(reactContext);
        AirMapLiteManager mapLiteManager = new AirMapLiteManager(reactContext);
        AirMapUrlTileManager tileManager = new AirMapUrlTileManager(reactContext);

        return Arrays.<ViewManager>asList(
                calloutManager,
                annotationManager,
                polylineManager,
                polygonManager,
                circleManager,
                mapManager,
                mapLiteManager,
                tileManager);
    }
}
