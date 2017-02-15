package com.example.tom.pictureapp2;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
//讀取圖檔並顯示
public class DetailActivity extends AppCompatActivity
    //先實作介面 alt+enter 加入方法 (
implements GestureDetector.OnGestureListener{
    private int position;
    private ImageView image;
    private Cursor cursor;
    //先宣告屬性
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //GestureDetector(Context,GestureDetecto.OnGestureListener)
        //               (this   ,DetailActivity實作此介面所以可用this 此時代表OnGestureListener 未來偵測出手勢 會自動通知DetailActivity)
        detector = new GestureDetector(this,this);
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
    //在畫面中按下
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    //輕碰螢幕 未放開
    @Override
    public void onShowPress(MotionEvent e) {

    }
    //輕碰螢幕 放開時
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    //使用者按下後移動時
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    //長觸螢幕時 (兩秒鐘左右)
    @Override
    public void onLongPress(MotionEvent e) {

    }
    //覆寫onTouchEvent方法 ctrl+0
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把super改成detector 未來使用者在手機螢幕上的操作將會交給GestureDetector
        return detector.onTouchEvent(event);
    }
    //使用者快速滑動螢幕後 放開時
    /*
    MotionEvent e1 滑動的起始點 座標的X值(e1.getX()) 座標的Y值(e1.getY())
    MotionEvent e2 滑動的結束點 手勢放開時的位置
    float velocityX 橫向移動的速度值
    float velocity 直向移動的速度值
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //取得滑動的水平位置 ; - (減) ;
        float distance = e2.getX() - e1.getX();
        //假如距離大於100 代表由左往右滑 往前一張圖
        if(distance>100){
            //先判斷往前一筆是否有資料 無資料就直接跳到最後一筆
            //並更新圖檔
            if(!cursor.moveToPrevious()){
                cursor.moveToLast();
            }
            updateImage();
            //假如距離小於-100 代表由右往左滑 往後一張圖
        }else if (distance<-100){
            //先判斷往後一筆是否有資料 無資料就直接跳到第一筆
            //並更新圖檔
            if (!cursor.moveToNext()){
                cursor.moveToFirst();
            }
            updateImage();
        }
        return false;
    }
}
