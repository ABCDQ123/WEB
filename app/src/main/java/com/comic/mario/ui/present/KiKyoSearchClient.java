package com.comic.mario.ui.present;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.comic.mario.R;
import com.comic.mario.ui.bean.ComicBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.util.recyclerview.MultiData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class KiKyoSearchClient {

    private Context mContext;
    private WebView mWebView;
    private WebBean.SearchBean mSearchBean;
    private int position = 1;

    private ArrayList<MultiData> mItems;

    public void cancel() {
        if (null != mWebView) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
            mSearchBean = null;
            mContext = null;
        }
    }

    public void request(Context context, ArrayList<MultiData> items, WebBean.SearchBean classifyBean, String key, ImpKiKyo listener) {
        if (null == mWebView) {
            mContext = context;
            mSearchBean = classifyBean;
            mItems = items;
            mWebView = new WebView(context);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setBlockNetworkLoads(false);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setDatabaseEnabled(true);
            mWebView.getSettings().supportMultipleWindows();
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(listener), "java_obj");
            if (null == mSearchBean.getAgent()) {

            } else if (!mSearchBean.getAgent().isEmpty()) {
                mWebView.getSettings().setUserAgentString(mSearchBean.getAgent());
            }
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    result.confirm();
                    return super.onJsAlert(view, url, message, result);
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (null == mSearchBean.getPrepareMethod()) {

                    } else if (!mSearchBean.getPrepareMethod().isEmpty()) {
                        mWebView.loadUrl("" + mSearchBean.getPrepareMethod());
                    }
                    view.loadUrl("javascript:window.java_obj.showHtml(document.getElementsByTagName('html')[0].innerHTML);");
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    view.loadUrl("javascript:window.java_obj.showHtml(document.getElementsByTagName('html')[0].innerHTML);");
                }
            });
        }
        position = 1;
        mWebView.loadUrl(mSearchBean.getUrl() + key);
    }

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    public class InJavaScriptLocalObj {

        private ImpKiKyo listener;

        public InJavaScriptLocalObj(ImpKiKyo listener) {
            this.listener = listener;
        }

        @JavascriptInterface
        public void showHtml(String html) {
            fixedThreadPool.submit(() -> {
                parse(html, listener);
            });
        }
    }

    private void parse(String html, ImpKiKyo listener) {
        ArrayList<MultiData> itemsTmp = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select(mSearchBean.getMainEl());
        for (Element element : elements) {
            ComicBean comicBean = new ComicBean();
            String link = ParseUtil.parseOption(element, mSearchBean.getLinkEl());
            String title = ParseUtil.parseOption(element, mSearchBean.getTitleEl());
            String image = ParseUtil.parseOption(element, mSearchBean.getImageEl());
            String intro = ParseUtil.parseOption(element, mSearchBean.getIntroEl());
            if (null == mSearchBean.getImageOption()) {

            } else if (!mSearchBean.getImageOption().isEmpty()) {
                String options[] = mSearchBean.getImageOption().split("@@");
                for (String op : options) {
                    String values[] = op.split("@#");
                    if (values[0].equals("replace")) {
                        image = image.replace(values[1], values[2]);
                    } else if (values[0].equals("put")) {
                        image = image + "" + values[1];
                    }
                }
                image = image.trim();
            }
            comicBean.setLink(link);
            comicBean.setTitle(title);
            comicBean.setImg(image);
            comicBean.setIntro(intro);
            itemsTmp.add(new MultiData(1, R.layout.item_comic, 3, comicBean));
        }
        if (((Activity) mContext).isDestroyed()) {
            return;
        }
        if (mItems.size() != 0 && itemsTmp.size() != 0) {
            List<MultiData> sameIndex = new ArrayList<>();
            for (MultiData multiData : mItems) {
                for (MultiData multiDataTemp : itemsTmp) {
                    ComicBean comicBean = (ComicBean) multiData.getData();
                    ComicBean comicBeanTemp = (ComicBean) multiDataTemp.getData();
                    if (comicBean.getTitle().equals(comicBeanTemp.getTitle())) {
                        sameIndex.add(multiDataTemp);
                    }
                }
            }
            for (MultiData multiData : sameIndex) {
                itemsTmp.remove(multiData);
            }
            ((Activity) mContext).runOnUiThread(() -> {
                int notify = mItems.size() - 1;
                mItems.addAll(itemsTmp);
                listener.response(notify, mItems);
            });
        } else if (mItems.size() == 0 && itemsTmp.size() != 0) {
            ((Activity) mContext).runOnUiThread(() -> {
                mItems.addAll(itemsTmp);
                listener.response(0, mItems);
            });
        }
    }
}
