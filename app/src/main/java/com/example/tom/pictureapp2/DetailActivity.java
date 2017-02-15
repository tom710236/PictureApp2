package com.example.tom.pictureapp2;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
//讀取圖檔並顯示
public class DetailActivity extends AppCompatActivity {
    private int position;
    private ImageView image;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //取得由MainActivity所傳遞的點擊項目位置
        //用整數position儲存 並將其由區域變數提升為屬性
        position = getIntent().getIntExtra("POSITION",0);
        //取得畫面中的imageView物件 也將其由區域變數提升為屬性
        image = (ImageView)findViewById(R.id.imageView);
        //產生CursorLoader物件並提供她查詢的位置和條件
        CursorLoader loader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,null,null,null);
        //呼叫CursorLoader的loadInBackground方法要求他已背景方法查詢
        //查詢後的結果儲存在cursor物件 並將其由區域變數提升為屬性
        cursor = loader.loadInBackground();
        cursor.moveToPosition(position);
        //執行自行設計的updateImage方法
        updateImage();

    }
    //自行設計一個專門更新圖檔的updateImage
    private void updateImage() {
        //取得查詢結果中該列的原圖在裝置中儲存的路徑
        String imagePath=
                cursor.getString
                        (cursor.getColumnIndex
                                (MediaStore.Images.Media.DATA));
        //使用BitmapFactory.decodeFile讀取路徑中的圖檔 並傳喚為Bitmap物件
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //設定畫面中的ImageView 已顯示該Bitmap物件
        image.setImageBitmap(bitmap);
    }
}
