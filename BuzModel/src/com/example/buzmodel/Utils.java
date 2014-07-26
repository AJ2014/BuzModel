package com.example.buzmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.buzmodel.model.EBuzQuality;
import com.example.buzmodel.model.EBuzUnit;
import com.example.buzmodel.model.TBuz;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;

public class Utils {
	
	private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
	private static final String IMAGE_MIME_TYPE = "image/png";
	private static MediaScannerConnection msc;
	
	private static final long DEFAULT_PUSH_TIME = 1000;
	
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
            node0[i].setDate(timeStamp);
            node0[i].setName(name);
            node0[i].setQuality(EBuzQuality.NORMAL);
            node0[i].setUnit(EBuzUnit.DEGREE);
            node0[i].setValue(100f + rm.nextInt(50));
            timeStamp += DEFAULT_PUSH_TIME;
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
	
	public static String saveBitmap2DCIMFolder(Context context, Bitmap source, String title, String description) {
		
		final String uri = MediaStore.Images.Media.insertImage(context.getContentResolver(), source, title, description);
		return getFilePathByContentResolver(context, Uri.parse(uri));
	}
	
	/** 
     * Insert an image and create a thumbnail for it. 
     * 
     * @param cr The content resolver to use 
     * @param imagePath The path to the image to insert 
     * @param name The name of the image 
     * @param description The description of the image 
     * @return The URL to the newly created image 
     * @throws FileNotFoundException 
     */  
	public static final String insertImage(ContentResolver cr,
			String imagePath, String name, String description)
			throws FileNotFoundException {
		// Check if file exists with a FileInputStream
		FileInputStream stream = new FileInputStream(imagePath);
		try {
			Bitmap bm = BitmapFactory.decodeFile(imagePath);
			String ret = insertImage(cr, bm, name, description);
			bm.recycle();
			return ret;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}
	
	/** 
     * Insert an image and create a thumbnail for it. 
     * 
     * @param cr The content resolver to use 
     * @param source The stream to use for the image 
     * @param title The name of the image 
     * @param description The description of the image 
     * @return The URL to the newly created image, or <code>null</code> if the image failed to be stored 
     *              for any reason. 
     */  
	public static final String insertImage(ContentResolver cr, Bitmap source,
			String title, String description) {
		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DESCRIPTION, description);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");

		Uri url = null;
		String stringUrl = null; /* value to be returned */

		try {
			url = cr.insert(STORAGE_URI, values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				} finally {
					imageOut.close();
				}
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	}
	
	/**
	 * 根据Uri获取文件路径
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getFilePathByContentResolver(Context context, Uri uri) {
		if (null == uri) {
			return null;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null, null);
		String filePath = null;
		if (null == c) {
			throw new IllegalArgumentException("Query on " + uri
					+ " returns null result.");
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} else {
				filePath = c.getString(c.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} finally {
			c.close();
		}
		return filePath;
	}
	
	private void funcToNotifyImgAdded(Context context, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}
	
	private void funcToNotifyImgAdded2(Context context, final String path) {
		msc = new MediaScannerConnection(context, new MediaScannerConnectionClient() {
			
			@Override
			public void onScanCompleted(String path, Uri uri) {
				msc.disconnect();
			}
			
			@Override
			public void onMediaScannerConnected() {
				msc.scanFile(path, IMAGE_MIME_TYPE);
			}
		});
		msc.connect();
	}

}
