package com.hynson.webview;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebMessage;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.base.base.BaseActivity;
import com.hynson.R;
import com.hynson.databinding.ActivityWebviewBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


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
//        binding.webv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i(TAG, "shouldOverrideUrlLoading: " + url);
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                Log.i(TAG, "shouldOverrideUrlLoading: " + request.toString());
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                Log.i("TAG", "onReceivedError");
//            }
//        });
        binding.webv.addJavascriptInterface(new JsToJava(), "animal1");
        final Animal p = new Animal("鸭子", "", "暂无信息", true, 23);
        binding.webv.addJavascriptInterface(p, "animal");
        binding.webv.addJavascriptInterface(this, "back");

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
    public void webvBack() {
        Log.i(TAG, "webvBack: ");
        binding.webv.post(new Runnable() {
            @Override
            public void run() {
                if (binding.webv.canGoBack()) {
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
                    Toast.makeText(getBaseContext(), "js调用java " + paramFromJS, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_webmsg:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cmd", "张三");
                    jsonObject.put("msg", "170cm");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String url = "file:///android_asset/webmessage.html";
                        binding.webv.postWebMessage(new WebMessage(jsonObject.toString()), Uri.parse(url));
                    } else {
                        binding.webv.evaluateJavascript("javascript:sendMessage(" + jsonObject + ")", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                                Log.i(TAG, "onReceiveValue: " + value);
                            }
                        });
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btn_detail:
                binding.webv.loadUrl("file:///android_asset/detail.html");
                break;
            case R.id.btn_list:
                binding.webv.loadUrl("file:///android_asset/list.html");
                break;
            case R.id.btn_h5:
                binding.webv.loadUrl("file:///android_asset/webmessage.html");
                break;
            case R.id.btn_body:
                // String body = "<style> p{ word-break:break-word } body,th,td,input,select,textarea,button,p,div{ font-size: 16px;font-family: PingFangSC-Regular;font-weight: normal;font-stretch: normal;font-style: normal;line-height: 1.5;letter-spacing: normal;color: rgba(0, 0, 0, 0.85); } </style><h3>Introduction to balance exercises</h3><img src=\"https://testonline-image.vesync.com/recipeThirdPartImage/v1/2022-08-01-28dc900c-e026-4d37-a2b4-29e138dd5157.jpg\" width=\"320\" height=\"240\" alt=\"A\" man=\"\" balances=\"\" on=\"\" one=\"\" foot=\"\" with=\"\" both=\"\" hands=\"\" in=\"\" the=\"\" air.=\"\"/> <div>Balance exercises can help you maintain your balance — and confidence — at any age. If you're an older adult, balance exercises are especially important because they can help you prevent falls and maintain your independence. It's a good idea to include balance training along with physical activity and strength training in your regular activity.</div> <br><div>Nearly any activity that keeps you on your feet and moving, such as walking, can help you maintain good balance. But specific exercises designed to enhance your balance are beneficial to include in your daily routine and can help improve your stability.</div> <br><div>For example, balance on one foot while you're standing for a period of time at home or when you're out and about. Or, stand up from a seated position without using your hands. Or try walking in a line, heel to toe, for a short distance. You can also try tai chi — a form of movement training that may improve balance and stability and reduce the incidence of falls.</div> <br><div>If you have severe balance problems or an orthopedic condition, get your doctor's OK before doing balance exercises.</div> <br><h3>Weight shifts</h3><img src=\"https://testonline-image.vesync.com/recipeThirdPartImage/v1/2022-08-01-89bfc877-1a49-4914-a214-1232c7aba908.jpg\" width=\"320\" height=\"240\" alt=\"Man\" doing=\"\" weight=\"\" shifts=\"\"/> <div>When you're ready to try balance exercises, start with weight shifts:</div> <br><ul>    <li style = \"margin: 10px 0;\">Stand with your feet hip-width apart and your weight equally distributed on both legs (A).</li>    <li style = \"margin: 10px 0;\">Shift your weight to your right side, then lift your left foot off the floor (B).</li>    <li style = \"margin: 10px 0;\">Hold the position as long as you can maintain good form, up to 30 seconds.</li>    <li style = \"margin: 10px 0;\">Return to the starting position and repeat on the other side. As your balance improves, increase the number of repetitions.</li></ul><h3>Single-leg balance</h3><img src=\"https://testonline-image.vesync.com/recipeThirdPartImage/v1/2022-08-01-6f7738cb-a33a-4911-a169-7a5fef389ca5.jpg\" width=\"320\" height=\"240\" alt=\"Woman\" doing=\"\" single-leg=\"\" balance=\"\" exercises=\"\"/> <div>Standing on one leg is another common balance exercise:</div> <br><ul>    <li style = \"margin: 10px 0;\">Stand with your feet hip-width apart and your weight equally distributed on both legs. Place your hands on your hips. Lift your left leg off the floor and bend it back at the knee (A).</li>    <li style = \"margin: 10px 0;\">Hold the position as long as you can maintain good form, up to 30 seconds.</li>    <li style = \"margin: 10px 0;\">Return to the starting position and repeat on the other side. As your balance improves, increase the number of repetitions.</li>    <li style = \"margin: 10px 0;\">For variety, reach out with your foot as far as possible without touching the floor (B).</li>    <li style = \"margin: 10px 0;\">For added challenge, balance on one leg while standing on a pillow or other unstable surface.</li></ul><h3>Biceps curls for balance</h3><img src=\"https://testonline-image.vesync.com/recipeThirdPartImage/v1/2022-08-01-32358d09-eee4-42bf-afe4-81c7a98a155e.jpg\" width=\"320\" height=\"240\" alt=\"Man\" doing=\"\" biceps=\"\" curls=\"\" for=\"\" balance=\"\"/> <div>You can do many balance exercises with weights. Try biceps curls with a dumbbell:</div> <br><ul>    <li style = \"margin: 10px 0;\">Stand with your feet hip-width apart and your weight equally distributed on both legs. Hold the dumbbell in your left hand with your palm facing upward (A). Lift your right leg off the floor and bend it back at the knee (B).</li>    <li style = \"margin: 10px 0;\">Hold the position as long as you can maintain good form, up to 30 seconds.</li>    <li style = \"margin: 10px 0;\">Return to the starting position and repeat on the other side. As your balance improves, increase the number of repetitions.</li>    <li style = \"margin: 10px 0;\">For added challenge, balance on the leg opposite the weight (C) or while standing on a pillow or other unstable surface (D).</li></ul><h3>Tai chi for balance</h3><img src=\"https://testonline-image.vesync.com/recipeThirdPartImage/v1/2022-08-01-a95860c4-fc06-42c9-a2b1-d3ae6aa10cb4.jpg\" width=\"320\" height=\"240\" alt=\"A\" group=\"\" during=\"\" a=\"\" tai=\"\" chi=\"\" exercise=\"\"/> <div>Another exercise that can help improve your balance and reduce your risk of falls is tai chi — a form of movement training.</div><div>Look for group classes offered at local fitness centers or senior centers. Or, rent or buy videos or books about tai chi. But keep in mind that it's difficult to ensure you're using the proper techniques when learning the exercises from a book.</div>";
                String body = readHtmlFile("body.html");
                binding.webv.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                break;
        }
    }

    private String readHtmlFile(String fileName) {
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
