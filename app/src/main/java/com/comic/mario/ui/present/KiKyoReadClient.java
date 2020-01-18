package com.comic.mario.ui.present;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.comic.mario.ui.bean.ComicBean;
import com.comic.mario.ui.bean.ComicReadBean;
import com.comic.mario.ui.bean.WebBean;
import com.comic.mario.util.LogUtil;
import com.comic.mario.util.recyclerview.MultiData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class KiKyoReadClient {

    private WebView mWebView;
    private WebBean.ReadBean mReadBean;
    private Context mContext;

    private int position;
    private ArrayList<ComicReadBean> mItems = new ArrayList<>();

    private int mPageCount = 1;

    public void cancel() {
        if (mWebView != null) {
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
            mReadBean = null;
            mContext = null;
            mItems.clear();
        }
    }

    public KiKyoReadClient(Context context, String url, WebBean.ReadBean readBean, ImpKiKyo listener) {
        parse(context, url, readBean, listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void parse(Context context, String url, WebBean.ReadBean readBean, ImpKiKyo listener) {
        if (null == mWebView) {
            mContext = context;
            mReadBean = readBean;
            mWebView = new WebView(context);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setBlockNetworkImage(false);
            mWebView.getSettings().setBlockNetworkLoads(false);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().supportMultipleWindows();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(listener), "java_obj");
            if (null == mReadBean.getAgent()) {

            } else if (!mReadBean.getAgent().isEmpty()) {
                mWebView.getSettings().setUserAgentString(mReadBean.getAgent());
            }
            mWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                    }
                }

                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    result.confirm();
                    return true;
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (null == readBean.getPrepareMethod()) {

                    } else if (!readBean.getPrepareMethod().isEmpty()) {
                        mWebView.loadUrl("" + readBean.getPrepareMethod());
                    }
                    view.loadUrl("javascript:window.java_obj.showHtml(document.getElementsByTagName('html')[0].innerHTML);");
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    view.loadUrl("javascript:window.java_obj.showHtml(document.getElementsByTagName('html')[0].innerHTML);");
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                }
            });
        }
        position = 1;
        if (null == mReadBean.getJointUrl()) {
            mWebView.loadUrl(url);
        } else if (!mReadBean.getJointUrl().isEmpty()) {
            String site = mReadBean.getJointUrl().split("@")[0];
            String joinUrl = mReadBean.getJointUrl().split("@")[1];
            if (site.equals("left")) {
                mWebView.loadUrl(joinUrl + url);
            } else if (site.equals("right")) {
                mWebView.loadUrl(url + joinUrl);
            }
        } else {
            mWebView.loadUrl(url);
        }
    }

    public void nextPage() {
        if (null == mReadBean || null == mReadBean.getNextPageMethod()) {
            return;
        }
        if (mReadBean.getNextPageMethod().isEmpty()) {
            return;
        }
        if (mReadBean.getNextPageMethod().contains("loadUrl")) {
            position++;
            String method = mReadBean.getNextPageMethod().split("@#")[1].split("@!")[0];
            mWebView.loadUrl("" + method + "" + position + "/");
        } else {
            String method = mReadBean.getNextPageMethod().split("@#")[1];
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
                if (null == mReadBean.getImageLoadMethod()) {
                    parse(html, listener);
                } else if (mReadBean.getImageLoadMethod().isEmpty()) {
                    parse(html, listener);
                } else if (mReadBean.getImageLoadMethod().equals("all")) {
                    parseAll(html, listener);
                } else {
                    parse(html, listener);
                }
            });
        }
    }

    private void parseAll(String xml, ImpKiKyo listener) {
        if (xml.equals("<head></head><body></body>") || xml.isEmpty())
            return;
        ArrayList<ComicReadBean> itemsTmp = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        String intro = ParseUtil.parseOption(document, mReadBean.getIntroUrl());
        Elements elements = document.select(mReadBean.getImageMainEl());
        for (Element element : elements) {
            String imageUrl = ParseUtil.parseOption(element, mReadBean.getImageUrl());
            if (imageUrl == null || imageUrl.isEmpty())
                return;
            if (null == mReadBean.getImageOption()) {

            } else if (!mReadBean.getImageOption().isEmpty()) {
                String options[] = mReadBean.getImageOption().split("@@");
                for (String op : options) {
                    String values[] = op.split("@#");
                    if (values[0].equals("replace")) {
                        imageUrl = imageUrl.replace(values[1], values[2]);
                    } else if (values[0].equals("put")) {
                        imageUrl = imageUrl + "" + values[1];
                    }
                }
                imageUrl = imageUrl.trim();
            }
            ComicReadBean comicReadBeanSingle = new ComicReadBean();
            comicReadBeanSingle.setIndex(itemsTmp.size() + 1);
            comicReadBeanSingle.setCount(elements.size() + 1);
            comicReadBeanSingle.setImageUrl(imageUrl);
            comicReadBeanSingle.setIntro(intro);
            itemsTmp.add(comicReadBeanSingle);
        }
        if (itemsTmp.size() == 0) {
            return;
        }
        if (mItems.size() == 0) {
            mItems.clear();
            mItems.addAll(itemsTmp);
            ((Activity) mContext).runOnUiThread(() -> {
                listener.response(1001, itemsTmp);
            });
        }
    }

    private void parse(String xml, ImpKiKyo listener) {
        if (xml.equals("<head></head><body></body>") || xml.isEmpty())
            return;
        ArrayList<ComicReadBean> itemsTmp = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        String index = ParseUtil.parseOption(document, mReadBean.getIndexEl());
        String imageUrl = ParseUtil.parseOption(document, mReadBean.getImageUrl());
        String intro = ParseUtil.parseOption(document, mReadBean.getIndexEl());
        if (null == mReadBean.getImageOption()) {

        } else if (!mReadBean.getImageOption().isEmpty()) {
            String options[] = mReadBean.getImageOption().split("@@");
            for (String op : options) {
                String values[] = op.split("@#");
                if (values[0].equals("replace")) {
                    imageUrl = imageUrl.replace(values[1], values[2]);
                } else if (values[0].equals("put")) {
                    imageUrl = imageUrl + "" + values[1];
                }
            }
            imageUrl = imageUrl.trim();
        }
        ComicReadBean comicReadBeanSingle = new ComicReadBean();
        if (isNumeric(index)) {
            comicReadBeanSingle.setIndexString(index);
            comicReadBeanSingle.setIndex(1);
            comicReadBeanSingle.setCount(Integer.parseInt(index));
        } else {
            comicReadBeanSingle.setIndexString(index);
            comicReadBeanSingle.setIndex(parseIndex(index).get(0));
            comicReadBeanSingle.setCount(parseIndex(index).get(1));
        }
        comicReadBeanSingle.setImageUrl(imageUrl);
        comicReadBeanSingle.setIntro(intro);
        itemsTmp.add(comicReadBeanSingle);
        if (((Activity) mContext).isDestroyed()) {
            return;
        }
        if (mItems.size() != 0 && itemsTmp.size() != 0) {
            List<ComicReadBean> sameIndex = new ArrayList<>();
            for (ComicReadBean comicReadBean : mItems) {
                for (ComicReadBean comicReadBeanTemp : itemsTmp) {
                    if (comicReadBean.getImageUrl().equals(comicReadBeanTemp.getImageUrl())) {
                        sameIndex.add(comicReadBeanTemp);
                    }
                }
            }
            for (ComicReadBean comicReadBean : sameIndex) {
                itemsTmp.remove(comicReadBean);
            }
            if (itemsTmp.size() != 0 && mItems.size() != 0 && mItems.get(0).getIndex() == 1) {
                itemsTmp.get(0).setIndex(mItems.size() + 1);
            }
            mItems.addAll(itemsTmp);
            ((Activity) mContext).runOnUiThread(() -> {
                if (itemsTmp.size() == 0) {

                } else {
                    listener.response(0, itemsTmp);
                    if (comicReadBeanSingle.getIndex() < comicReadBeanSingle.getCount()) {
                        nextPage();
                    }
                }
            });
        } else if (mItems.size() == 0 && itemsTmp.size() != 0) {
            mItems.addAll(itemsTmp);
            ((Activity) mContext).runOnUiThread(() -> {
                listener.response(0, comicReadBeanSingle);
                if (comicReadBeanSingle.getIndex() < comicReadBeanSingle.getCount()) {
                    nextPage();
                }
            });
        }
    }

    private void preLoad(String url) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.prefetchToDiskCache(imageRequest, mContext.getApplicationContext());
    }

    private List<Integer> parseIndex(String indexString) {
        String mPattern = "(\\d+)(/| / )(\\d+)";
        Pattern r = Pattern.compile(mPattern);
        Matcher m = r.matcher(indexString);
        List<Integer> integers = new ArrayList<>();
        if (m.find()) {
            int number1 = Integer.parseInt(m.group(1));
            int number2 = Integer.parseInt(m.group(3));
            integers.add(number1);
            integers.add(number2);
        }
        return integers;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public ArrayList<ComicReadBean> getmItems() {
        return mItems;
    }
}
