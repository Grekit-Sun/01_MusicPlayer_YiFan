package cn.weli.mediaplayer.module;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.base.BaseActivity;


public class WebViewActivity extends BaseActivity<IWebViewPresenter,IWebView> implements IWebView {

    private WebView mWebView;
    private String url ;

    @Override
    protected int getLayout() {
        return R.layout.webview_layout;
    }

    @Override
    protected Class<IWebView> getViewClass() {
        return IWebView.class;
    }

    @Override
    protected Class<IWebViewPresenter> getPresenterClass() {
        return IWebViewPresenter.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mWebView = $(R.id.wv_news);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        url = extras.getString("url");

        //方式一：加载一个网页
        mWebView.loadUrl(url);

    }
}
