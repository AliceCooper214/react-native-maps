/**
 * 这是一段用于在React Native应用程序中创建和管理Google Maps组件实例的自定义视图管理器的Java代码。 
 * AirMapManager 扩展了ViewGroupManager类，用于创建和管理 AirMapView 实例，AirMapView 是一个自定义视图，
 * 使用 Google Maps SDK for Android 渲染地图组件。
 * AirMapManager 类包含处理 AirMapView 组件的属性、事件和布局的方法。 
 * ReactProp 注释用于定义可以从React Native中设置的属性。 RCTEventEmitter 用于向应用程序的JavaScript端发出事件。
 * AirMapManager 类还从Google Maps SDK for Android导入各种类，
 * 包括 GoogleMap、GoogleMapOptions、MapsInitializer、LatLng 和 MapStyleOptions。 这些类用于配置和渲染地图组件。
 * 总的来说，这段代码是使用Google Maps SDK for Android实现在React Native应用程序中创建Google Maps组件的自定义视图管理器的实现。
 */
package com.airbnb.android.react.maps;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Map;

import javax.annotation.Nullable;

public class AirMapManager extends ViewGroupManager<AirMapView> {

    private static final String REACT_CLASS = "AIRMap";
    private static final int ANIMATE_TO_REGION = 1;
    private static final int ANIMATE_TO_COORDINATE = 2;
    private static final int FIT_TO_ELEMENTS = 3;
    private static final int FIT_TO_SUPPLIED_MARKERS = 4;
    private static final int FIT_TO_COORDINATES = 5;

    private final Map<String, Integer> MAP_TYPES = MapBuilder.of(
            "standard", GoogleMap.MAP_TYPE_NORMAL,
            "satellite", GoogleMap.MAP_TYPE_SATELLITE,
            "hybrid", GoogleMap.MAP_TYPE_HYBRID,
            "terrain", GoogleMap.MAP_TYPE_TERRAIN,
            "none", GoogleMap.MAP_TYPE_NONE
    );

    private final ReactApplicationContext appContext;

    protected GoogleMapOptions googleMapOptions;

    public AirMapManager(ReactApplicationContext context) {
        this.appContext = context;
        this.googleMapOptions = new GoogleMapOptions();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected AirMapView createViewInstance(ThemedReactContext context) {
        return new AirMapView(context, this, googleMapOptions);
    }

    private void emitMapError(ThemedReactContext context, String message, String type) {
        WritableMap error = Arguments.createMap();
        error.putString("message", message);
        error.putString("type", type);

        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onError", error);
    }

    @ReactProp(name = "region")
    public void setRegion(AirMapView view, ReadableMap region) {
        view.setRegion(region);
    }

    @ReactProp(name = "mapType")
    public void setMapType(AirMapView view, @Nullable String mapType) {
        int typeId = MAP_TYPES.get(mapType);
        view.map.setMapType(typeId);
    }

    @ReactProp(name = "customMapStyleString")
    public void setMapStyle(AirMapView view, @Nullable String customMapStyleString) {
        view.map.setMapStyle(new MapStyleOptions(customMapStyleString));
    }

    @ReactProp(name = "showsUserLocation", defaultBoolean = false)
    public void setShowsUserLocation(AirMapView view, boolean showUserLocation) {
        view.setShowsUserLocation(showUserLocation);
    }

    @ReactProp(name = "showsMyLocationButton", defaultBoolean = true)
    public void setShowsMyLocationButton(AirMapView view, boolean showMyLocationButton) {
        view.setShowsMyLocationButton(showMyLocationButton);
    }

    @ReactProp(name = "toolbarEnabled", defaultBoolean = true)
    public void setToolbarEnabled(AirMapView view, boolean toolbarEnabled) {
        view.setToolbarEnabled(toolbarEnabled);
    }

    // This is a private prop to improve performance of panDrag by disabling it when the callback is not set
    @ReactProp(name = "handlePanDrag", defaultBoolean = false)
    public void setHandlePanDrag(AirMapView view, boolean handlePanDrag) {
        view.setHandlePanDrag(handlePanDrag);
    }

    @ReactProp(name = "showsTraffic", defaultBoolean = false)
    public void setShowTraffic(AirMapView view, boolean showTraffic) {
        view.map.setTrafficEnabled(showTraffic);
    }

    @ReactProp(name = "showsBuildings", defaultBoolean = false)
    public void setShowBuildings(AirMapView view, boolean showBuildings) {
        view.map.setBuildingsEnabled(showBuildings);
    }

    @ReactProp(name = "showsIndoors", defaultBoolean = false)
    public void setShowIndoors(AirMapView view, boolean showIndoors) {
        view.map.setIndoorEnabled(showIndoors);
    }

    @ReactProp(name = "showsIndoorLevelPicker", defaultBoolean = false)
    public void setShowsIndoorLevelPicker(AirMapView view, boolean showsIndoorLevelPicker) {
        view.map.getUiSettings().setIndoorLevelPickerEnabled(showsIndoorLevelPicker);
    }

    @ReactProp(name = "showsCompass", defaultBoolean = false)
    public void setShowsCompass(AirMapView view, boolean showsCompass) {
        view.map.getUiSettings().setCompassEnabled(showsCompass);
    }

    @ReactProp(name = "scrollEnabled", defaultBoolean = false)
    public void setScrollEnabled(AirMapView view, boolean scrollEnabled) {
        view.map.getUiSettings().setScrollGesturesEnabled(scrollEnabled);
    }

    @ReactProp(name = "zoomEnabled", defaultBoolean = false)
    public void setZoomEnabled(AirMapView view, boolean zoomEnabled) {
        view.map.getUiSettings().setZoomGesturesEnabled(zoomEnabled);
    }

    @ReactProp(name = "rotateEnabled", defaultBoolean = false)
    public void setRotateEnabled(AirMapView view, boolean rotateEnabled) {
        view.map.getUiSettings().setRotateGesturesEnabled(rotateEnabled);
    }

    @ReactProp(name = "cacheEnabled", defaultBoolean = false)
    public void setCacheEnabled(AirMapView view, boolean cacheEnabled) {
        view.setCacheEnabled(cacheEnabled);
    }

    @ReactProp(name = "loadingEnabled", defaultBoolean = false)
    public void setLoadingEnabled(AirMapView view, boolean loadingEnabled) {
        view.enableMapLoading(loadingEnabled);
    }

    @ReactProp(name = "moveOnMarkerPress", defaultBoolean = true)
    public void setMoveOnMarkerPress(AirMapView view, boolean moveOnPress) {
        view.setMoveOnMarkerPress(moveOnPress);
    }

    @ReactProp(name = "loadingBackgroundColor", customType = "Color")
    public void setLoadingBackgroundColor(AirMapView view, @Nullable Integer loadingBackgroundColor) {
        view.setLoadingBackgroundColor(loadingBackgroundColor);
    }

    @ReactProp(name = "loadingIndicatorColor", customType = "Color")
    public void setLoadingIndicatorColor(AirMapView view, @Nullable Integer loadingIndicatorColor) {
        view.setLoadingIndicatorColor(loadingIndicatorColor);
    }

    @ReactProp(name = "pitchEnabled", defaultBoolean = false)
    public void setPitchEnabled(AirMapView view, boolean pitchEnabled) {
        view.map.getUiSettings().setTiltGesturesEnabled(pitchEnabled);
    }

    @Override
    public void receiveCommand(AirMapView view, int commandId, @Nullable ReadableArray args) {
        Integer duration;
        Double lat;
        Double lng;
        Double lngDelta;
        Double latDelta;
        ReadableMap region;

        switch (commandId) {
            case ANIMATE_TO_REGION:
                region = args.getMap(0);
                duration = args.getInt(1);
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                lngDelta = region.getDouble("longitudeDelta");
                latDelta = region.getDouble("latitudeDelta");
                LatLngBounds bounds = new LatLngBounds(
                        new LatLng(lat - latDelta / 2, lng - lngDelta / 2), // southwest
                        new LatLng(lat + latDelta / 2, lng + lngDelta / 2)  // northeast
                );
                view.animateToRegion(bounds, duration);
                break;

            case ANIMATE_TO_COORDINATE:
                region = args.getMap(0);
                duration = args.getInt(1);
                lng = region.getDouble("longitude");
                lat = region.getDouble("latitude");
                view.animateToCoordinate(new LatLng(lat, lng), duration);
                break;

            case FIT_TO_ELEMENTS:
                view.fitToElements(args.getBoolean(0));
                break;

            case FIT_TO_SUPPLIED_MARKERS:
                view.fitToSuppliedMarkers(args.getArray(0), args.getBoolean(1));
                break;
            case FIT_TO_COORDINATES:
                view.fitToCoordinates(args.getArray(0), args.getMap(1), args.getBoolean(2));
                break;
        }
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
                "onMapReady", MapBuilder.of("registrationName", "onMapReady"),
                "onPress", MapBuilder.of("registrationName", "onPress"),
                "onLongPress", MapBuilder.of("registrationName", "onLongPress"),
                "onMarkerPress", MapBuilder.of("registrationName", "onMarkerPress"),
                "onMarkerSelect", MapBuilder.of("registrationName", "onMarkerSelect"),
                "onMarkerDeselect", MapBuilder.of("registrationName", "onMarkerDeselect"),
                "onCalloutPress", MapBuilder.of("registrationName", "onCalloutPress")
        );

        map.putAll(MapBuilder.of(
                "onMarkerDragStart", MapBuilder.of("registrationName", "onMarkerDragStart"),
                "onMarkerDrag", MapBuilder.of("registrationName", "onMarkerDrag"),
                "onMarkerDragEnd", MapBuilder.of("registrationName", "onMarkerDragEnd"),
                "onPanDrag", MapBuilder.of("registrationName", "onPanDrag")
        ));

        return map;
    }

    @Override
    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "animateToRegion", ANIMATE_TO_REGION,
                "animateToCoordinate", ANIMATE_TO_COORDINATE,
                "fitToElements", FIT_TO_ELEMENTS,
                "fitToSuppliedMarkers", FIT_TO_SUPPLIED_MARKERS,
                "fitToCoordinates", FIT_TO_COORDINATES
        );
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        // A custom shadow node is needed in order to pass back the width/height of the map to the
        // view manager so that it can start applying camera moves with bounds.
        return new SizeReportingShadowNode();
    }

    @Override
    public void addView(AirMapView parent, View child, int index) {
        parent.addFeature(child, index);
    }

    @Override
    public int getChildCount(AirMapView view) {
        return view.getFeatureCount();
    }

    @Override
    public View getChildAt(AirMapView view, int index) {
        return view.getFeatureAt(index);
    }

    @Override
    public void removeViewAt(AirMapView parent, int index) {
        parent.removeFeatureAt(index);
    }

    @Override
    public void updateExtraData(AirMapView view, Object extraData) {
        view.updateExtraData(extraData);
    }

    void pushEvent(ThemedReactContext context, View view, String name, WritableMap data) {
        context.getJSModule(RCTEventEmitter.class)
            .receiveEvent(view.getId(), name, data);
    }

    @Override
    public void onDropViewInstance(AirMapView view) {
        view.doDestroy();
        super.onDropViewInstance(view);
    }

}
