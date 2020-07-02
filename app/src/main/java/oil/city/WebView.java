package oil.city;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

public class WebView extends AppCompatActivity {

    android.webkit.WebView webView;
    private String webUrl = "https://askep.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//відображення на весь екран пристрою

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


