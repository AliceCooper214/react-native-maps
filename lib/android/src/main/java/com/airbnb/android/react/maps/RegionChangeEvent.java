/**
 * 这是一个名为 RegionChangeEvent 的 Java 类，用于在地图区域变化时发送事件。
 * 该类继承了 Event<RegionChangeEvent>，
 * 并实现了 getEventName、canCoalesce 和 dispatch 方法，这些方法用于指定事件名、是否可合并和事件分发的逻辑。
 * 在 RegionChangeEvent 类中，我们定义了一些属性，包括 bounds、center 和 continuous。bounds 表示地图显示的范围，
 * 是一个 LatLngBounds 对象；center 表示地图的中心点，是一个 LatLng 对象；continuous 表示是否持续发送事件，是一个布尔值。
 * 在构造函数中，我们通过传入的参数初始化了这些属性。然后，在 dispatch 方法中，
 * 我们创建了一个 WritableMap 对象 event，并将 continuous 属性添加到其中。我们还创建了一个 WritableMap 对象 region，
 * 并将其中包含的经度、纬度和经纬度跨度等信息添加到 region 中。最后，我们将 region 添加到 event 中，
 * 并通过 RCTEventEmitter 将事件发送出去。
 * 总之，RegionChangeEvent 类用于在地图区域变化时发送事件，提供了一种标准化的方式来处理地图区域变化事件。
 */
package com.airbnb.android.react.maps;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class RegionChangeEvent extends Event<RegionChangeEvent> {
     // LatLngBounds 表示地图显示的范围
    private final LatLngBounds bounds;

    // LatLng 表示地图的中心点
    private final LatLng center;

    // 是否持续发送事件
    private final boolean continuous;

    public RegionChangeEvent(int id, LatLngBounds bounds, LatLng center, boolean continuous) {
        super(id);
        this.bounds = bounds;
        this.center = center;
        this.continuous = continuous;
    }

    @Override
    public String getEventName() {
        return "topChange";
    }

    @Override
    public boolean canCoalesce() {
        return false;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {

        // 创建一个 WritableMap 对象，用于存储事件的数据
        WritableMap event = new WritableNativeMap();

        // 将 continuous 属性添加到 event 中
        event.putBoolean("continuous", continuous);

        // 创建一个 WritableMap 对象，用于存储地图区域信息
        WritableMap region = new WritableNativeMap();

        // 将经度、纬度和经纬度跨度等信息添加到 region 中
        region.putDouble("latitude", center.latitude);
        region.putDouble("longitude", center.longitude);
        region.putDouble("latitudeDelta", bounds.northeast.latitude - bounds.southwest.latitude);
        region.putDouble("longitudeDelta", bounds.northeast.longitude - bounds.southwest.longitude);

        // 将 region 添加到 event 中，并通过 RCTEventEmitter 将事件发送出去
        event.putMap("region", region);
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), event);
    }
}
