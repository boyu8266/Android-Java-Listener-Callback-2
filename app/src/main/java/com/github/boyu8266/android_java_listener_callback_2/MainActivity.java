package com.github.boyu8266.android_java_listener_callback_2;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //  View
    private ProgressDialog progressDialog;

    //  control
    private int process = 0;
    private final int TOTAL_COUNT = 10;
    private final int DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("請稍後");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (++process <= TOTAL_COUNT) {
                System.out.println("thread run on: " + Thread.currentThread().getName());
                try {
                    if (null != onLoadingListener) {
                        if (process < TOTAL_COUNT) {
                            //  還在處理時
                            //  呼叫 inProcess
                            String str = "(" + process + " / " + TOTAL_COUNT + ")";
                            onLoadingListener.inProcess(str);
                        } else {
                            //  處理結束
                            //  呼叫 onFinished
                            onLoadingListener.onFinished();
                        }
                    }

                    //  為了模擬耗時, 加入延遲
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    //  發生例外, 呼叫 onFailure .
                    if (null != onLoadingListener) {
                        onLoadingListener.onFailure();
                    }
                }
            }
        }
    };

    private OnLoadingListener onLoadingListener = new OnLoadingListener() {
        @Override
        public void inProcess(String text) {
            //  UI 操作需 runOnUiThread
            runOnUiThread(() -> {
                //  還在處理時
                //  更新當前進度給使用者看
                progressDialog.setMessage(text);

                System.out.println("inProcess run on: " + Thread.currentThread().getName());
            });
        }

        @Override
        public void onFinished() {
            //  UI 操作需 runOnUiThread
            runOnUiThread(() -> {
                //  成功時
                //  關閉對話框
                progressDialog.dismiss();

                System.out.println("finished run on: " + Thread.currentThread().getName());
            });
        }

        @Override
        public void onFailure() {
            //  UI 操作需 runOnUiThread
            runOnUiThread(() -> {
                //  失敗時
                //  關閉對話框
                progressDialog.dismiss();

                System.out.println("failure run on: " + Thread.currentThread().getName());
            });
        }
    };
}