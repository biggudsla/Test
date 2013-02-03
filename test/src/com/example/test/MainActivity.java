package com.example.test;


import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	int foldernum=0;
	String p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		gv = (GridView)findViewById(R.id.ImgGridView);
		
        
        final ImageAdapter ia = new ImageAdapter(this);

        
        gv.setAdapter(ia);

      //  gv.setVisibility(View.INVISIBLE);
    
        gv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
        		
        		
      //  		gv.setVisibility(View.INVISIBLE);
        //		gv2.setVisibility(View.VISIBLE);
        	//	p = thumbsBucketList.get(position);
        		ia.callImageViewer(position);
        
        	}
        });
	
	}
	public class ImageAdapter extends BaseAdapter {
		private String imgData;
		private String geoData;
		private ArrayList<String> thumbsDataList;
		private ArrayList<String> thumbsIDList;
		private ArrayList<String> thumbsBucketList;
/**/		
/**/		private Integer[] Bucketnum;

		ImageAdapter(Context c){
			mContext = c;
			thumbsDataList = new ArrayList<String>();
			thumbsIDList = new ArrayList<String>();
/**/			thumbsBucketList = new ArrayList<String>();
/**/			getThumbInfo(thumbsIDList, thumbsDataList,thumbsBucketList);
			
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
			return foldernum;
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
		
		private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas,ArrayList<String> thumbsBuckets){
			String[] proj = {MediaStore.Images.Media._ID,
							 MediaStore.Images.Media.DATA,
							 MediaStore.Images.Media.DISPLAY_NAME,
							 MediaStore.Images.Media.SIZE,
/**/							 MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
			
			Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					proj, null, null, null);
			
			if (imageCursor != null && imageCursor.moveToFirst()){
				String title;
				String thumbsID;
				String thumbsImageID;
				String thumbsData;
				String data;
				String imgSize;
				String bucketname;
				String temp="";
				
				
				
				int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
				int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
				int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
				int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
				int thumbsBucketCol = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
				int num = 0;
				int check;
				do {
					thumbsID = imageCursor.getString(thumbsIDCol);
					thumbsData = imageCursor.getString(thumbsDataCol);
					thumbsImageID = imageCursor.getString(thumbsImageIDCol);
					imgSize = imageCursor.getString(thumbsSizeCol);
					bucketname = imageCursor.getString(thumbsBucketCol);
					num++;
					check=0;
					
					if (thumbsImageID != null){
						thumbsIDs.add(thumbsID);
						thumbsDatas.add(thumbsData);
						for(int j= 0; j< thumbsBuckets.size(); j++ )
						{
							if(bucketname.equals(thumbsBuckets.get(j))){
								check = 1;	
								break;
							}
						}
						if(check == 0){
							thumbsBuckets.add(bucketname);
							foldernum++;
						}
							
					}
				}while (imageCursor.moveToNext());
					Bucketnum = new Integer[foldernum];
/**/				for(int j=0 ; j<foldernum; j++){
						Bucketnum[j] = R.drawable.boyoung;
	//					Toast.maketext(this, Integer.toString(foldernum), Toast.LENGTH_SHORT).show();
}
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

		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return true;
	}
	

}
