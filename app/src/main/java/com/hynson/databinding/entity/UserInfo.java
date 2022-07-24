package com.hynson.databinding.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.hynson.BR;

public class UserInfo extends BaseObservable {
    private String pwd;
    private String name;
    private String loginTime;

    public UserInfo(String pwd, String name) {
        this.pwd = pwd;
        this.name = name;
    }
    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);
    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
    @Bindable
    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
        notifyPropertyChanged(BR.loginTime);
    }
}
