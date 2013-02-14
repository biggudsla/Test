package com.example.test;
import com.example.test.picture;
import java.util.ArrayList;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImageList extends Activity{

private Context mContext;
ArrayList<String> thumbsDaList;
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imagelist);
    mContext = this;
    Intent i = getIntent();
	Bundle extras = i.getExtras();
	String imgPath = extras.getString("filename");

	thumbsDaList= new ArrayList<String>();
	for(int h=0; h<picture.picturelist.size();h++){
		if(picture.picturelist.get(h).BUCKET_NAME.equals(imgPath)){
			thumbsDaList.add(picture.picturelist.get(h).DATA);
		}
	}
    
    GridView gv = (GridView)findViewById(R.id.ImgGridView3);
    final ImageAdapter ia = new ImageAdapter(this);
    gv.setAdapter(ia);
    gv.setOnItemClickListener(new OnItemClickListener(){
    	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
    		Intent i = new Intent(mContext, ImagePopup.class);
    		String imgPath = thumbsDaList.get(position);
    		i.putExtra("filename", imgPath);
    		startActivityForResult(i, 1);
    	}
    });
    Toast.makeText(this, Integer.toString(thumbsDaList.size()) , Toast.LENGTH_SHORT).show();
}

/**========================================== 
 * 		        Adapter class 
 * ==========================================*/
public class ImageAdapter extends BaseAdapter {
	/*
	private String imgData;
	private String geoData;
	private ArrayList<String> thumbsDataList;
	private ArrayList<String> thumbsIDList;
	
	*/
	
	ImageAdapter(Context c){
		mContext = c;
//		thumbsDataList = new ArrayList<String>();
	//	thumbsIDList = new ArrayList<String>();
		//getThumbInfo(thumbsIDList, thumbsDataList);
	}
	/*
	public final void callImageViewer(int selectedIndex){
		Intent i = new Intent(mContext, ImagePopup.class);
		String imgPath = thumbsDaList.get(selectedIndex);
		i.putExtra("filename", imgPath);
		startActivityForResult(i, 1);

	}
	*/
	public boolean deleteSelected(int sIndex){
		return true;
	}
	
	public int getCount() {
		return thumbsDaList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {	
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null){
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
			imageView.setAdjustViewBounds(false);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(1, 1, 1, 1);
		}else{
			imageView = (ImageView) convertView;
		}
		BitmapFactory.Options bo = new BitmapFactory.Options();
		bo.inSampleSize = 8;
		Bitmap bmp = BitmapFactory.decodeFile(thumbsDaList.get(position), bo);
		Bitmap resized = Bitmap.createScaledBitmap(bmp, 70, 70, true);
		imageView.setImageBitmap(resized);
		
		return imageView;
	}
	/*
	private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas){
		String[] proj = {MediaStore.Images.Media._ID,
						 MediaStore.Images.Media.DATA,
						 MediaStore.Images.Media.DISPLAY_NAME,
						 MediaStore.Images.Media.SIZE};
		
		Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				proj, "bucket_display_name='"+ p +"'", null, null);
		
		if (imageCursor != null && imageCursor.moveToFirst()){
			String title;
			String thumbsID;
			String thumbsImageID;
			String thumbsData;
			String data;
			String imgSize;
			
			int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
			int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
			int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
			int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
			int num = 0;
			
			do {
				thumbsID = imageCursor.getString(thumbsIDCol);
				thumbsData = imageCursor.getString(thumbsDataCol);
				thumbsImageID = imageCursor.getString(thumbsImageIDCol);
				imgSize = imageCursor.getString(thumbsSizeCol);
				num++;
				if (thumbsImageID != null){
					thumbsIDs.add(thumbsID);
					thumbsDatas.add(thumbsData);
				}
			}while (imageCursor.moveToNext());
			
		}
		
		imageCursor.close();
		return;
	}
	
	private String getImageInfo(String ImageData, String Location, String thumbID){
		String imageDataPath = null;
		String[] proj = {MediaStore.Images.Media._ID,
				 MediaStore.Images.Media.DATA,
				 MediaStore.Images.Media.DISPLAY_NAME,
				 MediaStore.Images.Media.SIZE};
		Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
}