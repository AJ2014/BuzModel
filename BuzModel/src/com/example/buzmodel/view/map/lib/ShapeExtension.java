package com.example.buzmodel.view.map.lib;

public interface ShapeExtension{

    public interface OnShapeActionListener {

        void onShapeClick(Shape shape, float xOnImage, float yOnImage);

    }

    void addShape(Shape shape);

    void removeShape(Object tag);

    void clearShapes();
}