package com.example.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String content;
    private boolean isSendSuccess;

    protected Message(Parcel in) {
        content = in.readString();
        isSendSuccess = in.readByte() != 0;
    }

    public Message() {
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeByte((byte) (isSendSuccess ? 1 : 0));
    }

    public String getContent() {
        return content;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }
}
