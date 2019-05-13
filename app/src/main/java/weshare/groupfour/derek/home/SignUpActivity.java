package weshare.groupfour.derek.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import weshare.groupfour.derek.R;

public class SignUpActivity extends AppCompatActivity {
    WebView wvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        wvSignUp = findViewById(R.id.wvSignUp);
        wvSignUp.getSettings().setJavaScriptEnabled(true);
        wvSignUp.loadUrl("http://developer.android.com/index.html");
        wvSignUp.setWebViewClient(new WebViewClient() {
            @Override
            // 在7.0時deprecated(還是有支援)，為了向下版本支援仍用此方法
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvSignUp.canGoBack()) {
            wvSignUp.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
