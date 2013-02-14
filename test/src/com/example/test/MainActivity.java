package com.example.test;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.test.picture;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity {

	GridView gv;
	private Context mContext;
	OURdb mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
	
		gv = (GridView)findViewById(R.id.ImgGridView);
        final ImageAdapter ia = new ImageAdapter(this);
        gv.setAdapter(ia);
        gv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        		ia.callImageViewer(position);
        	}
        });
        
	
    
    	mHelper = new OURdb(this);
		SQLiteDatabase db = mHelper.getWritableDatabase();
//		ContentValues row = new ContentValues();
//		row.put("pictureid", "_ID");
//		row.put("diary", "DIARY");
//		db.insert("db1", null, row);
		Cursor cursor;
//		cursor = db.query("db1", new String[] { "pictureid", "diary",
//				"lati", "longi" }, null, null,
//				null, null, null);
		cursor = db.rawQuery("SELECT pictureid,diary,lati,longi FROM db1",null);
		int num = 0;
//		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			for(int e=0; e<picture.picturelist.size(); e++)
			{
				if(picture.picturelist.get(e)._ID.equals(cursor.getString(0))){
					picture.picturelist.get(e).DIARY = cursor.getString(1);
					picture.picturelist.get(e).LATI = cursor.getString(2);
					picture.picturelist.get(e).LONGI = cursor.getString(3);
					
					break;
				}
			}
			num++;
		}
		Toast.makeText(this, Integer.toString(num) , Toast.LENGTH_SHORT).show();
		cursor.close();
		mHelper.close();
		
   }

	
	public class ImageAdapter extends BaseAdapter {
		
		private ArrayList<String> thumbsBucketList;

		ImageAdapter(Context c){
			mContext = c;
			thumbsBucketList = new ArrayList<String>();
			getThumbInfo();
			
		}
		
		public final void callImageViewer(int selectedIndex){
			Intent i = new Intent(mContext, ImageList.class);
			//String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
			i.putExtra("filename", thumbsBucketList.get(selectedIndex));
			startActivityForResult(i, 1);
		}
		
		public boolean deleteSelected(int sIndex){
			return true;
		}
		
		public int getCount() {
			return  thumbsBucketList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {	
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			
	//		ImageView imageView;
			if (convertView == null){
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.image, null);
				TextView tv=(TextView)v.findViewById(R.id.icon_text);
				tv.setText(thumbsBucketList.get(position));
				ImageView iv=(ImageView)v.findViewById(R.id.icon_image);
				iv.setImageResource(R.drawable.ic_launcher);
			
				
				/*imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
				imageView.setAdjustViewBounds(false);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(2, 2, 2, 2);*/
			}else{
				v = convertView;
				//imageView = (ImageView) convertView;
			}
/*			
			BitmapFactory.Options bo = new BitmapFactory.Options();
			bo.inSampleSize = 2;
			Bitmap bmp = BitmapFactory.decodeFile(thumbsDataList.get(position), bo);
			Bitmap resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);
			imageView.setImageBitmap(resized);
	*/
		
			//imageView.setImageResource(R.drawable.boyoung);
			return v;
		}
		
		private void getThumbInfo(){
			String[] proj = {MediaStore.Images.Media.LATITUDE,
					MediaStore.Images.Media.LONGITUDE,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
					MediaStore.Images.Media.DATE_TAKEN,
					MediaStore.Images.Media.DATA};
			
			Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			proj, null, null, null);
	
			if (imageCursor != null && imageCursor.moveToFirst()){
				String bucketname;
				String idname;
				String latiname;
				String longiname;
				String dataname;
				String datename;		
		
		int thumbsBucketCol = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
		int thumbsDateCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
		int thumbsIdCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
		int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
		int thumbsLatiCol = imageCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
		int thumbsLongiCol = imageCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
		
		
		boolean check;
		picture.picturelist.clear();
		do {
			bucketname = imageCursor.getString(thumbsBucketCol);
			idname = imageCursor.getString(thumbsIdCol);
			dataname = imageCursor.getString(thumbsDataCol);
			datename = imageCursor.getString(thumbsDateCol);
			latiname = imageCursor.getString(thumbsLatiCol);
			longiname = imageCursor.getString(thumbsLongiCol);
			
			
			check=false;
			if (idname != null){
				picture.picturelist.add(new picture(idname,datename,latiname,longiname,dataname,bucketname));
						
				for(int j= 0; j< thumbsBucketList.size(); j++ )
				{
					if(bucketname.equals(thumbsBucketList.get(j))){
						check = true;	
						break;
					}
				}
				if(check == false){
					thumbsBucketList.add(bucketname);
				}
			}
		}while (imageCursor.moveToNext());
		
	}
		
		
		
		imageCursor.close();
	return;
}
		
/*		private String getImageInfo(String ImageData, String Location, String thumbID){
			String imageDataPath = null;
			String[] proj = {MediaStore.Images.Media._ID,
					 MediaStore.Images.Media.DATA,
					 MediaStore.Images.Media.DISPLAY_NAME,
					 MediaStore.Images.Media.SIZE};
			Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					proj, "_ID='"+ thumbID +"'", null, null);
			
			if (imageCursor != null && imageCursor.moveToFirst()){
				if (imageCursor.getCount() > 0){
					int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
					imageDataPath = imageCursor.getString(imgData);
				}
			}
			imageCursor.close();
			return imageDataPath;
		}
		*/
	}

		
/*		private String getImageInfo(String ImageData, String Location, String thumbID){
			String imageDataPath = null;
			String[] proj = {MediaStore.Images.Media._ID,
					 MediaStore.Images.Media.DATA,
					 MediaStore.Images.Media.DISPLAY_NAME,
					 MediaStore.Images.Media.SIZE};
			Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					proj, "_ID='"+ thumbID +"'", null, null);
			
			if (imageCursor != null && imageCursor.moveToFirst()){
				if (imageCursor.getCount() > 0){
					int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
					imageDataPath = imageCursor.getString(imgData);
				}
			}
			imageCursor.close();
			return imageDataPath;
		}
		*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		String text = null;
		
		switch(item.getItemId()){
		case android.R.id.home:
			text = "Application icon";
			break;
			
		case R.id.item1:
			text = "Action item, with text, displayed if room exists";
			gv.setVisibility(View.INVISIBLE);
    
			break;
			
		case R.id.item2:
			text = "Action item, icon only, always displayed";
			gv.setVisibility(View.VISIBLE);

			break;

		default:
			return false;
		}
		
		/*
		Date d = new Date(las);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		String formattedDate = format.format(d);
		*/
		if(picture.picturelist.get(0).LATI ==null)
		Toast.makeText(this,picture.picturelist.get(0).LATI  , Toast.LENGTH_SHORT).show();
		return true;
	}
	

}
