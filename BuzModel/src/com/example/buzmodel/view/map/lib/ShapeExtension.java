package com.example.buzmodel.view.map.lib;

/**
 * ShapeExtension是出于ImageMap继承于FrameLayout而又�?要嵌入到HighlightImageView内部过程而设计的�?
 * 主要是将HighlhgitImageView内部过程的操作扩展到ImageMap中处理�??
 */
public interface ShapeExtension{

    public interface OnShapeActionListener {

        /**
         * 当一个Shape被点�?
         * @param shape
         * @param xOnImage
         * @param yOnImage
         */
        void onShapeClick(Shape shape, float xOnImage, float yOnImage);

    }

    /**
     * 添加形状
     * @param shape 形状描述
     */
    void addShape(Shape shape);

    /**
     * 删除指定Tag的形�?
     * @param tag 指定Tag
     */
    void removeShape(Object tag);

    /**
     * 清除�?有形�?
     */
    void clearShapes();
}