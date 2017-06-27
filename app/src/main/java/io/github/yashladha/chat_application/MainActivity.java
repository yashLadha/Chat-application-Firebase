package io.github.yashladha.chat_application;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    public String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
