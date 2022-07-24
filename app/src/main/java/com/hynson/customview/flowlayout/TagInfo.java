package com.hynson.customview.flowlayout;

import android.graphics.Rect;

public class TagInfo {
    public String tagId;
    public String tagName;
    Rect rect = new Rect();
    public int childPosition;
    public int dataPosition = -1;
    public TagType type;

    @Override
    public String toString() {
        return "TagInfo{" +
                "tagId='" + tagId + '\'' +
                ", tagName='" + tagName + '\'' +
                ", type=" + type +
                '}';
    }
}
