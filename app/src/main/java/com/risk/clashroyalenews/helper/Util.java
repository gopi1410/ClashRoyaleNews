package com.risk.clashroyalenews.helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.risk.clashroyalenews.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;


public class Util {

    private Context mContext;
    private String mLink;
    private WebView mWebview;
    private ProgressBar mProgressBar;

    private String TAG = getClass().getSimpleName();

    public Util(Context context, String link, WebView webView, ProgressBar progressBar) {
        mContext = context;
        mLink = link;
        mWebview = webView;
        mProgressBar = progressBar;
        WebSettings mWebsettings = mWebview.getSettings();
        mWebsettings.setJavaScriptEnabled(true);
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);

                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                }
            }
        });
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public void refresh() {
        mWebview.loadUrl(mLink);
    }

    public void shareLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mLink);
        mContext.startActivity(intent);
    }

    public void copyLink() {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("link", mLink);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(mContext, "Link Copied !", Toast.LENGTH_LONG).show();
    }

    public void openInWebView() {
        mWebview.loadUrl(mLink);
    }

    public void openInBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));
        mContext.startActivity(browserIntent);
    }

    public static void loadImageByPicasso(Context context, ImageView imageView, String imageUrl) {

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .noFade()
                .into(imageView);
    }

    private static Date convertToDate(String crDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        try {
            return sdf.parse(crDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertToString(Date date) {
        if (date == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }

    public static String reformatDate(String crDate) {
        return convertToString(convertToDate(crDate));
    }

}