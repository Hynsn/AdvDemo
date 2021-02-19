package com.example.webview;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebMessage;
import android.webkit.WebMessagePort;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.R;
import com.example.base.BaseActivity;
import com.example.databinding.ActivityWebviewBinding;

import org.json.JSONObject;

import androidx.annotation.RequiresApi;


public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {
    final static String TAG = WebviewActivity.class.getSimpleName();
    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void bindView() {
        WebSettings settings = binding.webv.getSettings();
        settings.setJavaScriptEnabled(true);

//        binding.webv.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                Log.i(TAG, "onProgressChanged: "+newProgress);
//            }
//        });
        //设置不用系统浏览器打开,直接显示在当前Webview
        binding.webv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: "+url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "shouldOverrideUrlLoading: "+request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i("TAG","onReceivedError");
            }
        });
        binding.webv.addJavascriptInterface(new JsToJava(), "animal1");
        final Animal p = new Animal("鸭子","","暂无信息",true,23);
        binding.webv.addJavascriptInterface(p,"animal");
        binding.webv.addJavascriptInterface(this,"back");

        //"file:///android_asset/js.html"
        binding.webv.loadUrl("file:///android_asset/list.html");
        //允许webview对文件的操作
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
//        binding.webv.loadUrl("https://www.baidu.com");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @JavascriptInterface
    public void webvBack(){
        Log.i(TAG, "webvBack: ");
        binding.webv.post(new Runnable() {
            @Override
            public void run() {
                if(binding.webv.canGoBack()){
                    binding.webv.goBack();
                }
            }
        });
//        binding.webv.postWebMessage(new WebMessage("存储"), Uri.parse("http://siis"));
    }

    // 原生的方式
    private class JsToJava {
        // 高版本需要加这个注解才能生效
        @JavascriptInterface
        public void save(final String paramFromJS) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(),"js调用java "+paramFromJS,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    int i = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void click(View v){
        switch (v.getId()){
            case R.id.btn_calljs:
                i++;
                /*binding.webv.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.i(TAG, "onReceiveValue: "+value);
                    }
                });*/
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cmd","张三");
                    jsonObject.put("msg","170cm");
                    binding.webv.evaluateJavascript("javascript:sendMessage("+jsonObject+")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.i(TAG, "onReceiveValue: "+value);
                        }
                    });
                }catch (Exception e){

                }
                break;
            case R.id.btn_detail:
                binding.webv.loadUrl("file:///android_asset/detail.html");
                break;
            case R.id.btn_home:
                binding.webv.loadUrl("file:///android_asset/list.html");
                break;
            case R.id.btn_h5:
                binding.webv.loadUrl("file:///android_asset/webmessage.html");
                break;
            case R.id.btn_webmsg:
                String url = "file:///android_asset/webmessage.html";
                binding.webv.postWebMessage(new WebMessage("test"), Uri.parse(url));
                WebMessagePort[] ports = binding.webv.createWebMessageChannel();
                ports[0].setWebMessageCallback(new WebMessagePort.WebMessageCallback() {
                    @Override
                    public void onMessage(WebMessagePort port, WebMessage message) {
                        super.onMessage(port, message);
                    }
                });
                break;
        }
    }
}
