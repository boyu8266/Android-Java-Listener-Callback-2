package com.github.boyu8266.android_java_listener_callback_2;

public interface OnLoadingListener {

    //  可以顯示當前的進度
    void inProcess(String text);

    //  在成功時呼叫
    void onFinished();

    //  在失敗時呼叫
    void onFailure();

}
