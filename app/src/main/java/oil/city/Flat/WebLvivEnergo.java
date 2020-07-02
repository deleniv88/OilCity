package oil.city.Flat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import oil.city.R;

public class WebLvivEnergo extends AppCompatActivity {

    android.webkit.WebView webView;
    private String webUrl = "https://easypay.ua/ua/catalog/utility/lvov/lvovoblenergo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_lviv_energo);

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

