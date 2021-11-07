package com.example.gson;

import android.os.Message;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import androidx.core.util.Pools;

public class TestTypeAdapter extends TypeAdapter<TestBean> {
    @Override
    public void write(JsonWriter out, TestBean value) throws IOException {

    }

    @Override
    public TestBean read(JsonReader in) throws IOException {
        TestBean bean = TestBean.obtain();
        in.beginObject();
        while (in.hasNext()){
            switch (in.nextName()) {
                case "url":
                    bean.url = in.nextString();
                    break;
                case "title":
                    bean.title = in.nextString();
                    break;
                case "keywords":
                    bean.keywords = in.nextString();
                    break;
            }
        }
        in.endObject();
        return bean;
    }
}
