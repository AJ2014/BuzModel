package com.example.buzmodel.view.map.lib;

import android.graphics.Color;

/**
 * author : 桥下�?粒砂 (chenyoca@gmail.com)
 * date   : 13-5-28
 * 根据坐标点数，转换成对应的图�?
 */
public class CoordsToShape {

    static final float default_radiu = 20f;

    static final int default_color = Color.RED;

    /**
     * 根据坐标，返回对应的图形�?
     * @param coordsGroup 坐标�?
     * @return 如果坐标为null，返回null
     */
    public static Shape toShape(Object tag,String coordsGroup){
        Shape shape = null;

        if( null == coordsGroup || "null".equals(coordsGroup) ) return shape;
        String[] croods = coordsGroup.split(",");

        shape = toShape(croods.length,tag);

        if( shape != null ){
            shape.setValues(coordsGroup);
        }

        return shape;
    }

    public static Shape toShape(Object tag,float... croods){
        Shape shape = toShape(croods.length, tag);
        if(shape != null){
            shape.setValues(croods);
        }
        return shape;
    }

    static Shape toShape(int length,Object tag){
        Shape shape = null;
        if(length < 2) return shape;

        if( length == 2 || length == 3){
            shape = new CircleShape(tag,default_color);
            if(length == 2){
                ((CircleShape)shape).setRadius(default_radiu);
            }
        }else{
            shape = new PolyShape(tag,default_color);
        }
        return shape;
    }
}
