package com.example.tom.pictureapp2;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
//外部儲存是危險權限 需先要求權限的程式碼
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //定義一個int常數 代表向使用者邀求讀取聯絡人的辨識編號
    private static final int REQUEST_READ_STORAGE = 3;
    //GridView 元件使用的 SimpleCursorAdapter 時 其Cursor參數會先使用null值
    SimpleCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //檢查權限
        /*
        檢查權限語法 (this,檢查的權限名稱)
        int permission = ActivityCompat.
                checkSelfPermission(this, READ_EXTERNAL_STORAGE);
                結果有兩種 PackageManager.PERMISSION_GRANTED 已獲得權限
                           PackageManager.PERMISSION_DENIED 目前無該權限
         */
        int permission = ActivityCompat.
                checkSelfPermission(this, READ_EXTERNAL_STORAGE);
                //未取得權限
                if(permission != PackageManager.PERMISSION_GRANTED){
                    //向使用者請求權限
                    /*
                    ActivityCompat.requestPermissions(
                    Context context (this),
                    String[] permissions (欲要求的權限)
                    int requestCode (本次請求的辨識編號,自己編 注意可讀性)
                     */
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{READ_EXTERNAL_STORAGE},
                            REQUEST_READ_STORAGE);
                 //已有權限
                }else {
                    //檔案存取
                    readThumbnails();
                }
        /*
        在程式碼出現對話框之後 會執行if...else後續程式碼
        待使用者按下對話框的任一選擇鈕後 將自動執行
        onRequestPermissionsResult
        這是非同步的處理方式
         */
    }

    //當使用者允許或拒絕權限 (alt+O 覆寫此方法)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //使用switch 處理一個以上的權限請求回覆
        //參數requestCode可以辨識不同的權限回覆
        switch (requestCode){
            case REQUEST_READ_STORAGE:
                //取得權限 進行檔案存取
                if (grantResults.length>0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readThumbnails();
                }
                //使用者拒絕權限 停用檔案存取 顯示對話框告知
                else {
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許讀取外部儲存權限才能顯示圖檔")
                            .setPositiveButton("OK",null)
                            .show();
                }
                return;
        }

    }
    //自行定義的一個方法
    //使用者允許權限後會執行的方法
    private void readThumbnails() {
        GridView grid = (GridView)findViewById(R.id.grid);
        String[] from = {
                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.DISPLAY_NAME};
        int[] to = new int[]{
                R.id.thumb_image,
                R.id.thumb_text };
        //SimpleCursorAdapter是將資料庫查詢的結果顯示在ListView的每一列
        /*
        所需參數
        Context context this
        int layout 每一列的版面配置檔的資源ID
        Cursor cursor 查詢內容提供者聯絡人所得到的Cursor物件
        String[] from 資料查詢結果Cursor中想要顯示的欄位名稱
        String[] to 資料顯示的元件ID
        int flag 給0 表示資料庫中的紀錄如果被更動 ListView 將不自動重新查詢並更新資料
         */
        adapter = new SimpleCursorAdapter(
                getBaseContext(),
                R.layout.thumb_item,
                null,
                from,
                to,
                0);
        //執行adapter
        grid.setAdapter(adapter);
        /*
        Loader類別 同步資料來源端與資料展現端的一個機制
        CursorLoader類別 為SQLite資料庫而設計的一個Loader子類別
        當資料庫中資料變動時 會自動更新ListView或其他元件上資料
         */
        //參數 this 應該給的是LoaderManager 請將游標停在該處錯誤的地方
        //按alt+enter 選擇Make MainActivity... 實作Loader Manager介面
        getSupportLoaderManager().initLoader(0,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //使用android.net.Uri先儲存查詢的資料位置
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //產生並回傳資料讀取器物件 並將uri傳遞給她
        return new CursorLoader(this,uri,null,null,null,null);
    }

    @Override
    //當資料讀取器向內容提供者查詢完成時 會自動呼叫onLoadFinished方法
    //此時及呼叫Adapter的swapCursor方法替換adapter內的Cursor物件
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
