package com.example.mbedtls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.base.base.BaseActivity;
import com.example.R;
import com.example.databinding.ActivityMbedtlsBinding;

import java.nio.charset.StandardCharsets;

public class MbedtlsActivity extends BaseActivity<ActivityMbedtlsBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_mbedtls;
    }

    @Override
    protected void bindView() {
        setTitle("mbed.ver:"+AesUtil.getInstance().version());
    }
    public void click(View v) {
        switch (v.getId()){
            case R.id.btn_encrypt:
                String text1 = binding.etTop.getText().toString().trim();
                byte[] bytes1 = text1.getBytes(StandardCharsets.UTF_8);
                byte[] encrypt = AesUtil.getInstance().encrypt(bytes1,bytes1.length);
                String str1 = new String(encrypt, 0, encrypt.length, StandardCharsets.ISO_8859_1);
                binding.tvBottom.setText(str1);
                break;
            case R.id.btn_decrypt:
                String text2 = binding.etTop.getText().toString().trim();
                byte[] bytes2 = text2.getBytes(StandardCharsets.UTF_8);
                byte[] decrypt = AesUtil.getInstance().decrypt(bytes2,bytes2.length);
                String str2 = null;
                if(decrypt!=null) {
                    str2 = new String(decrypt, 0, decrypt.length, StandardCharsets.UTF_8);
                }
                else {
                    Toast.makeText(this, "解密失败!", Toast.LENGTH_SHORT).show();
                }
                binding.tvBottom.setText(str2);
                break;
            case R.id.btn_clear:
                binding.etTop.setText(null);
                binding.tvBottom.setText(null);
                break;
            case R.id.btn_copy:
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("result", binding.tvBottom.getText().toString().trim());
                manager.setPrimaryClip(clipData);
                Toast.makeText(this, "复制成功!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String byte2Hex(byte[] bytes){
        String hex = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            hex = Integer.toHexString(bytes[i] & 0xFF);
            sb.append(hex.length()==1 ? ("0"+hex) : hex);
        }
        return sb.toString().trim();
    }
}
