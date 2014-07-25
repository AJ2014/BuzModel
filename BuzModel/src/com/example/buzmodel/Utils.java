package com.example.buzmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.buzmodel.model.EBuzQuality;
import com.example.buzmodel.model.EBuzUnit;
import com.example.buzmodel.model.TBuz;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.format.DateFormat;

public class Utils {
	
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
	
	
	public static TBuz[] getRandomDataArray(int count, int index, String name) {
		TBuz[] node0 = new TBuz[count];
		long timeStamp = System.currentTimeMillis();
        Random rm = new Random(timeStamp);
        // initialize the node's data, see it as push test data
        for (int i = 0; i < count; i++) {
            node0[i] = new TBuz();
            // same node, same id
            node0[i].setIndex(index);
            node0[i].setDate(timeStamp++);
            node0[i].setName(name);
            node0[i].setQuality(EBuzQuality.NORMAL);
            node0[i].setUnit(EBuzUnit.DEGREE);
            node0[i].setValue(100f + rm.nextInt(50));
        }
        return node0;
	}
	
	public static List<TBuz> getRandomDataList(int count, int index, String name) {
		List<TBuz> nodes = new ArrayList<TBuz>();
		long timeStamp = System.currentTimeMillis();
        Random rm = new Random(timeStamp);
		
		for (int i = 0; i < count; i++) {
			TBuz node = new TBuz();
			// same node, same id
			node.setIndex(index);
            node.setDate(timeStamp);
            node.setName(name);
            node.setQuality(EBuzQuality.NORMAL);
            node.setUnit(EBuzUnit.DEGREE);
            node.setValue(100f + rm.nextInt(50));
            nodes.add(node);
            timeStamp += 1000;
		}
		return nodes;
	}
	
	private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static final String IMAGE_MIME_TYPE = "image/png";
	
	public static String saveBitmap2DCIMFolder(ContentResolver cr, Bitmap source, String title, String description) {
		

//		ContentValues values = new ContentValues(7);
//
//		values.put(Images.Media.TITLE, title);
//		values.put(Images.Media.DISPLAY_NAME, title);
//		values.put(Images.Media.DATE_TAKEN, dateTaken);
//		values.put(Images.Media.MIME_TYPE, IMAGE_MIME_TYPE);
//		values.put(Images.Media.ORIENTATION, degree[0]);
//		values.put(Images.Media.DATA, filePath);
//		values.put(Images.Media.SIZE, size);
//		   
//		Uri dataUri = cr.insert(STORAGE_URI, values);
		
		return MediaStore.Images.Media.insertImage(cr, source, title, description);
	}

}
