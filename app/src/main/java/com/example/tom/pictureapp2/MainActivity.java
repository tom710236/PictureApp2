package com.example.tom.pictureapp2;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
//外部儲存是危險權限 需先要求權限的程式碼
public class MainActivity extends AppCompatActivity {
    //定義一個int常數 代表向使用者邀求讀取聯絡人的辨識編號
    private static final int REQUEST_READ_STORAGE = 3;
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
    }
}
