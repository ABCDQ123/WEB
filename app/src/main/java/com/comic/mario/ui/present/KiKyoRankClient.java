package com.comic.mario.ui.present;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
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
import com.comic.mario.util.LogUtil;
import com.comic.mario.util.recyclerview.MultiData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class KiKyoRankClient {

    private Context mContext;
    private WebView mWebView;
    private WebBean.RankBean mRankBean;
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
            mRankBean = null;
            mContext = null;
        }
    }

    public void request(Context context, ArrayList<MultiData> items, WebBean.RankBean rankBean, ImpKiKyo listener) {
        if (null == mWebView) {
            mContext = context;
            mRankBean = rankBean;
            mItems = items;
            mWebView = new WebView(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mWebView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
            }
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setBlockNetworkLoads(false);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setDatabaseEnabled(true);
            mWebView.getSettings().supportMultipleWindows();
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(listener), "java_obj");
            if (null == mRankBean.getAgent()) {

            } else if (!mRankBean.getAgent().isEmpty()) {
                mWebView.getSettings().setUserAgentString(mRankBean.getAgent());
            }
            mWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }

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
                    if (null == mRankBean.getPrepareMethod()) {

                    } else if (!mRankBean.getPrepareMethod().isEmpty()) {
                        mWebView.loadUrl("" + mRankBean.getPrepareMethod());
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
        mWebView.loadUrl(mRankBean.getUrl());
    }

    public void next() {
        if (null == mRankBean.getNextPageMethod()) {
            return;
        }
        if (mRankBean.getNextPageMethod().isEmpty()) {
            return;
        }
        if (mRankBean.getNextPageMethod().contains("loadUrl")) {
            position++;
            String methods[] = mRankBean.getNextPageMethod().split("@#")[1].split("@!");
            String method = "";
            for (String string : methods) {
                if (string.equals("position")) {
                    method = method + position;
                } else {
                    method = method + string;
                }
            }
            mWebView.loadUrl("" + method + "");
        } else {
            String method = mRankBean.getNextPageMethod().split("@#")[1];
            mWebView.loadUrl("javascript:" + method + "");
        }
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
        Elements elements = document.select(mRankBean.getMainEl());
        for (Element element : elements) {
            ComicBean comicBean = new ComicBean();
            String link = ParseUtil.parseOption(element, mRankBean.getLinkEl());
            String title = ParseUtil.parseOption(element, mRankBean.getTitleEl());
            String image = ParseUtil.parseOption(element, mRankBean.getImageEl());
            if (null == mRankBean.getImageOption()) {

            } else if (!mRankBean.getImageOption().isEmpty()) {
                String options[] = mRankBean.getImageOption().split("@@");
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
            String intro = ParseUtil.parseOption(element, mRankBean.getIntroEl());
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
            if (itemsTmp.size() == 0) {
                return;
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
