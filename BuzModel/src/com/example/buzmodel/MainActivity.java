package com.example.buzmodel;

import com.example.buzmodel.model.EBuzQuality;
import com.example.buzmodel.model.EBuzUnit;
import com.example.buzmodel.model.TBuz;
import com.example.buzmodel.view.grid.lib.IGridChart;
import com.example.buzmodel.view.grid.view.TemperatureChart;
import com.example.buzmodel.view.map.lib.CircleShape;
import com.example.buzmodel.view.map.lib.Shape;
import com.example.buzmodel.view.map.lib.ShapeExtension.OnShapeActionListener;
import com.example.buzmodel.view.map.lib.TextShape;
import com.example.buzmodel.view.map.view.HighlightImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

    int node0_id = 0x010;
    TBuz node0[];

	HighlightImageView mHImg;

    CircleShape node0_shape_c;
    TextShape node0_shape_t;
	
    IGridChart chart;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mHImg = (HighlightImageView) findViewById(R.id.himg_buz);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main, new BitmapFactory.Options());
		mHImg.setImageBitmap(bitmap);

        setupNodes();

        initShape();

        initChart();
	}

    private void initChart() {
    	chart = new TemperatureChart();
	}

	private void setupNodes() {
        node0 = new TBuz[10];
        long timeStamp = System.currentTimeMillis();
        Random rm = new Random(timeStamp);
        // initialize the node's data, see it as push test data
        for (int i = 0; i < 10; i++) {
            node0[i] = new TBuz();
            // same node, same id
            node0[i].setIndex(node0_id);
            node0[i].setDate(timeStamp++);
            node0[i].setName("Machine_0");
            node0[i].setQuality(EBuzQuality.NORMAL);
            node0[i].setUnit(EBuzUnit.DEGREE);
            node0[i].setValue(100f + rm.nextInt(50));
        }
    }

    private void initShape() {
        TBuz curNode = node0[0];
        // one node with two type of shapes
        // one display the real time status
        node0_shape_c = new CircleShape(node0_id + 1, curNode.getQuality().color);
        // one display the real time value
        node0_shape_t = new TextShape(node0_id, Color.BLACK);

        /*
        shape's coordinates in the background picture
        must use the dip value
         */
        String[] coords_circle0 = getApplicationContext().getResources().getStringArray(R.array.circle_0);
        String[] coords_rect0 = getApplicationContext().getResources().getStringArray(R.array.rect_0);

        node0_shape_c.setValues(Utils.dip2px(getApplicationContext(), Float.valueOf(coords_circle0[0])),
        		Utils.dip2px(getApplicationContext(), Float.valueOf(coords_circle0[1])),15);
        node0_shape_t.setValues(Utils.dip2px(getApplicationContext(), Float.valueOf(coords_rect0[0])),
        		Utils.dip2px(getApplicationContext(), Float.valueOf(coords_rect0[1])),
        		Utils.dip2px(getApplicationContext(), Float.valueOf(coords_rect0[2])),
        		Utils.dip2px(getApplicationContext(), Float.valueOf(coords_rect0[3])));
        node0_shape_t.setText(String.valueOf(curNode.getValue()), curNode.getUnit().flag);
        // add shapes to the flow picture
        mHImg.addShape(node0_shape_c);
        mHImg.addShape(node0_shape_t);
        // add click listener
        mHImg.setOnShapeClickListener(new OnShapeActionListener() {
            @Override
            public void onShapeClick(Shape shape, float xOnImage, float yOnImage) {
                if (node0_id == (Integer)shape.tag) {
                    Toast.makeText(getApplicationContext(), "进入趋势图", Toast.LENGTH_SHORT).show();
                    Intent intent = chart.initDataSet(MainActivity.this, node0);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

}
