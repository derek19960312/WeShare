package weshare.groupfour.derek.member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import weshare.groupfour.derek.R;

public class DepositActivity extends AppCompatActivity {
    WebView wvDeposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        wvDeposit = findViewById(R.id.wvDeposit);
        wvDeposit.getSettings().setJavaScriptEnabled(true);
        wvDeposit.loadUrl("http://developer.android.com/index.html");
        wvDeposit.setWebViewClient(new WebViewClient() {
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
        if (keyCode == KeyEvent.KEYCODE_BACK && wvDeposit.canGoBack()) {
            wvDeposit.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
