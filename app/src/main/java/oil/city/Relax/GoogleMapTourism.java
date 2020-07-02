package oil.city.Relax;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import oil.city.R;

public class GoogleMapTourism extends AppCompatActivity {

    android.webkit.WebView webView;
    private String webUrl = "https://www.google.com/maps/d/u/0/viewer?mid=13VOD0qiNBGdtr0xZKwY8xw9WXxBDazSp&hl=uk&ll=49.28169395578278%2C23.424616350000015&z=13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_tourism);

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

