package com.airbnb.android.react.maps;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * AirMapCalloutManager是一个ViewGroupManager，用于管理AirMapCallout的布局和属性。
 */
public class AirMapCalloutManager extends ViewGroupManager<AirMapCallout> {

    /**
     * 返回视图的名称，这将用于在JavaScript中引用此视图。
     */
    @Override
    public String getName() {
        return "AIRMapCallout";
    }

    /**
     * 创建并返回一个新的AirMapCallout实例。
     */
    @Override
    public AirMapCallout createViewInstance(ThemedReactContext context) {
        return new AirMapCallout(context);
    }

    /**
     * 设置AirMapCallout的tooltip属性。
     */
    @ReactProp(name = "tooltip", defaultBoolean = false)
    public void setTooltip(AirMapCallout view, boolean tooltip) {
        view.setTooltip(tooltip);
    }

    /**
     * 返回导出的事件类型，这里只有onPress事件。
     */
    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of("onPress", MapBuilder.of("registrationName", "onPress"));
    }

    /**
     * 创建并返回一个新的SizeReportingShadowNode实例，用于在布局完成后报告视图的宽度和高度。
     */
    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        // we use a custom shadow node that emits the width/height of the view
        // after layout with the updateExtraData method. Without this, we can't generate
        // a bitmap of the appropriate width/height of the rendered view.
        return new SizeReportingShadowNode();
    }

    /**
     * 更新AirMapCallout的额外数据，包括视图的宽度和高度。
     */
    @Override
    public void updateExtraData(AirMapCallout view, Object extraData) {
        // This method is called from the shadow node with the width/height of the rendered
        // marker view.
        //noinspection unchecked
        Map<String, Float> data = (Map<String, Float>) extraData;
        float width = data.get("width");
        float height = data.get("height");
        view.width = (int) width;
        view.height = (int) height;
    }
}