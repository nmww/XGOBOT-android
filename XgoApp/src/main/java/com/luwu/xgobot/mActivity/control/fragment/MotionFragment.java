package com.luwu.xgobot.mActivity.control.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luwu.xgobot.AppContext;
import com.luwu.xgobot.R;
import com.luwu.xgobot.data.RobotFunction;
import com.luwu.xgobot.mView.ButtonView;

import static com.luwu.xgobot.mView.ButtonView.DOWNPRESS;
import static com.luwu.xgobot.mView.ButtonView.LEFTPRESS;
import static com.luwu.xgobot.mView.ButtonView.RIGHTPRESS;
import static com.luwu.xgobot.mView.ButtonView.UPPRESS;

/**
 * <p>文件描述：<p>
 * <p>作者：zhangyibin<p>
 * <p>创建时间：2022/11/6<p>
 */
public class MotionFragment extends Fragment {
    private static final String TAG = "MotionFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motionmode, null);
        initView(view);
        Log.d(TAG, "onCreateView: " + RobotFunction.getWebUrl());
        mWeb.loadUrl(RobotFunction.getWebUrl());
        return view;
    }

    private ButtonView btnView;
    private RelativeLayout mWebLayout;
    private WebView mWeb;
    private TextView mWalk_tv, mTrot_tv;
    private ImageButton upBtn,middleBtn,downBtn,turnLeftBtn,turnRightBtn;
    private LinearLayout mWeb_errorLayout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        btnView.setButtonViewListener(new ButtonView.IButtonViewListener() {
            @Override
            public void actionDown(int num) {
                switch (num) {
                    case UPPRESS:
                        RobotFunction.btnControl(1);
                        break;
                    case DOWNPRESS:
                        RobotFunction.btnControl(2);
                        break;
                    case LEFTPRESS:
                        RobotFunction.btnControl(3);
                        break;
                    case RIGHTPRESS:
                        RobotFunction.btnControl(4);
                        break;
                }
            }

            @Override
            public void actionUp(int num) {
                RobotFunction.btnControl(0);
            }
        });
        turnLeftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        RobotFunction.btnControl(5);
                        break;
                    case MotionEvent.ACTION_UP:
                        RobotFunction.btnControl(0);
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        RobotFunction.btnControl(0);
                        break;
                    default:
//                        RobotFunction.btnControl(0);
                        break;
                }
                return false;
            }
        });
        turnRightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        RobotFunction.btnControl(6);
                        break;
                    case MotionEvent.ACTION_UP:
                        RobotFunction.btnControl(0);
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        RobotFunction.btnControl(0);
                        break;
                    default:
//                        RobotFunction.btnControl(0);
                        break;
                }
                return false;
            }
        });

        mWalk_tv.setOnClickListener(v -> {
            mWalk_tv.setTextColor(this.getResources().getColor(R.color.white));
            mWalk_tv.setBackgroundResource(R.drawable.bg_text_blue);
            mTrot_tv.setBackgroundResource(R.drawable.bg_text_white);
            RobotFunction.setStepState(1);
        });
        mTrot_tv.setOnClickListener(v -> {
            mWalk_tv.setBackgroundResource(R.drawable.bg_text_white);
            mTrot_tv.setTextColor(this.getResources().getColor(R.color.white));
            mTrot_tv.setBackgroundResource(R.drawable.bg_text_blue);
            RobotFunction.setStepState(0);
        });

        upBtn.setOnClickListener(v -> RobotFunction.grap(128));
        middleBtn.setOnClickListener(v -> RobotFunction.grap(129));
        downBtn.setOnClickListener(v -> {
            RobotFunction.grap(130);
        });
    }

    private void initView(View view) {
        btnView = view.findViewById(R.id.motionmode_btn_view);

        mWebLayout = view.findViewById(R.id.layout_webview);
        mWeb = new WebView(AppContext.getappContext());
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT);
        mWeb.setLayoutParams(params);
        mWebLayout.addView(mWeb);
        WebSettingsConfiguration(mWeb);
        mWeb_errorLayout = view.findViewById(R.id.web_error_layout);
        mWalk_tv = view.findViewById(R.id.motionmode_walk_tv);
        mTrot_tv = view.findViewById(R.id.motionmode_trot_tv);
        upBtn = view.findViewById(R.id.btn_grap_up);
        middleBtn = view.findViewById(R.id.btn_grap_middle);
        downBtn = view.findViewById(R.id.btn_grap_down);
        turnLeftBtn = view.findViewById(R.id.btn_turn_left);
        turnRightBtn = view.findViewById(R.id.btn_turn_right);
    }

    public void WebSettingsConfiguration(WebView webView) {
        WebSettings webSettings = webView.getSettings();
////如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
////支持插件
//        webSettings.setPluginState(WebSettings.PluginState.OFF);
//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setAllowContentAccess(true);
        //设置错误监听
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //开始加载
                mWeb_errorLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //加载失败
                Toast.makeText(getContext(),"url loading Fail",Toast.LENGTH_LONG).show();
                if (request.isForMainFrame()) {
                    mWeb_errorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWeb != null) {
            mWeb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWeb.clearHistory();
            mWebLayout.removeView(mWeb);
            mWeb.destroy();
            mWeb = null;
        }
    }
}
