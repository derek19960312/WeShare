package weshare.groupfour.derek.member;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
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
        WebSettings webSettings = wvDeposit.getSettings();
        wvDeposit.getSettings().setJavaScriptEnabled(true);
        wvDeposit.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setUseWideViewPort(true);

        wvDeposit.loadUrl("https://ca107g4.ga/CA107G4/SimpleWebRTC-master/test/selenium/onlyWatch.jsp?tc00001");
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
