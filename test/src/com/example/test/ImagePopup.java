package com.example.test;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagePopup extends Activity{
	private final int imgWidth = 500;
	private final int imgHeight = 800;
	TextView textview;
	EditText edittext;
	Button btn;
	Button btn1;
	Button btn2;
	Button btn4;
	
	int point;
	OURdb mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_popup);
		/** 전송메시지 */
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String imgPath = extras.getString("filename");
		
		for(point = 0; point <picture.picturelist.size();point++){
			if(imgPath.equals(picture.picturelist.get(point).DATA)){
				break;
			}
		}
		
		
		textview = (TextView)findViewById(R.id.textView);
		edittext = (EditText)findViewById(R.id.editView);
		btn = (Button)findViewById(R.id.btn);
		btn1 = (Button)findViewById(R.id.btn1);
		btn2 = (Button)findViewById(R.id.btn2);
		btn4 = (Button)findViewById(R.id.btn3);
		/** 완성된 이미지 보여주기  */
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inSampleSize = 2;
		ImageView iv = (ImageView)findViewById(R.id.imageView);
		Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
		Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
		iv.setImageBitmap(resized);
		
		textview.setText(picture.picturelist.get(point).DIARY);
		
		/** 리스트로 가기 버튼 */

	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	
	public void monclick1(View v) {
		textview.setVisibility(View.INVISIBLE);
		edittext.setVisibility(View.VISIBLE);
		btn.setVisibility(View.INVISIBLE);
		btn4.setVisibility(View.INVISIBLE);
		btn1.setVisibility(View.VISIBLE);
		btn2.setVisibility(View.VISIBLE);
		edittext.setText(textview.getText());
		
	}
	public void monclick4(View v) {
		Intent i = new Intent(ImagePopup.this, Assignpoint.class);
		i.putExtra("filename", point);
		startActivityForResult(i, 1);
	}
	public void monclick2(View v) {
		textview.setVisibility(View.VISIBLE);
		edittext.setVisibility(View.INVISIBLE);
		btn.setVisibility(View.VISIBLE);
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn4.setVisibility(View.VISIBLE);
		}
	public void monclick3(View v) {
				
		mHelper = new OURdb(this);
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues row = new ContentValues();
//		cursor = db.query("db1", new String[] { "pictureid", "diary" }, null, null,
	//			null, null, null);
		
		
		if(picture.picturelist.get(point).DIARY.equals("")&&picture.picturelist.get(point).LATI==null){
			
			row.put("pictureid", picture.picturelist.get(point)._ID);
			row.put("diary", edittext.getText().toString());
			row.put("lati", "");
			row.put("longi", "");
			db.insert("db1", null, row);
		}
		
		else{
			row.put("diary", edittext.getText().toString());
			db.update("db1",row,"pictureid ='"+picture.picturelist.get(point)._ID+"'",null);
		}
		
		picture.picturelist.get(point).DIARY = edittext.getText().toString();
	//		cursor.moveToFirst();
		mHelper.close();
		
		
		textview.setVisibility(View.VISIBLE);
		edittext.setVisibility(View.INVISIBLE);
		textview.setText(edittext.getText());
		btn.setVisibility(View.VISIBLE);
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn4.setVisibility(View.VISIBLE);
	}
	
	}
