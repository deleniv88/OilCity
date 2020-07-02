package oil.city.Flat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import oil.city.R;

public class WebVododar extends AppCompatActivity {

    android.webkit.WebView webView;
    private String webUrl = "https://boryslavvoda.com.ua/content/oplata_poslug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_vododar);

        webView = findViewById(R.id.webView);
        webView.loadUrl(webUrl);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        } else
            super.onBackPressed();
    }
}



