# DatabindingDemo
在熟悉MVVM框架中了解到DataBinding，MVVM的核心其实就是DataBinding。这篇文章基于登录界面小demo，给大家介绍如何使用DataBinding。
#### 使用DataBinding步骤
1、修改app的build.gradle启用DataBinding

```bash
apply plugin: 'com.android.application'

android {
    compileSdkVersion 28
    defaultConfig {
        applicationId "com.example.geekhou.myapplication"
        minSdkVersion 21
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    dataBinding {
        enabled true
    }
}
```
2、修改布局文件为DataBinding布局。
方法是：双击选中布局文件最外层布局，同时按住Alt+回车==>转换为Databinding布局。修改成功后，AS会自动生成对应的Databinding类，对应规则为test_main.xml --> TestMainBinding。

3、数据绑定：我们先要创建UserInfo实体类，再修改test_main.xml文件。数据绑定又包括单向和双向，单向绑定即当UserInfo数据发生改变时，控件(demo中的login_time_tv)会自动更新数据；双向绑定常用于输入框、ListView删除等View视图发生改变时，控件中对应的数据也需发生变化的情况。

```java
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

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
```

```markup
<!-- test_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <!-- 声明对象 -->
        <variable
            name="user"
            type="com.example.myapplication.UserInfo" />
        <variable
            name="Activity"
            type="com.example.myapplication.MainActivity" />
    </data>

    <LinearLayout
        android:layout_width="fill_parent"
        android:layout_height="fill_parent"
        android:id="@+id/container"
        tools:context=".MainActivity"
        android:orientation="vertical"
        android:gravity="center_horizontal|top">
        <EditText
            android:id="@+id/name_et"
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="100dp"
            android:singleLine="true"
            android:text="@={user.name}"
            android:hint="用户名"/>
        <EditText
            android:id="@+id/pwd_et"
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:singleLine="true"
            android:text="@={user.pwd}"
            android:inputType="numberPassword"
            android:hint="密码"/>
        <TextView
            android:id="@+id/login_time_tv"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.loginTime}"/>
        <Button
            android:id="@+id/login_btn"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{Activity.userLogin}"
            android:text="登录"/>
    </LinearLayout>
</layout>
```
#### 如何进行单向绑定？
1、UserInfo继承自BaseObservable
2、为UserInfo中需要绑定的get方法添加注解`@Bindable`，set方法添加`notifyPropertyChanged(BR.loginTime);`
3、如要进行双向绑定，再单向绑定基础上添加等号，如`android:text="@={user.pwd}"`

#### Activity中如何使用DataBinding？

```java
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.databinding.databinding.TestMainBinding;

public class MainActivity extends Activity {
    final static String TAG = "Main";

    private UserInfo user;
    private TestMainBinding binding;
    private Time mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test_main);
        mTime = new Time();
        binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.test_main);
        user = new UserInfo("","");
        binding.setUser(user);
        binding.setActivity(this);
    }

    public void userLogin(View v){
        mTime.setToNow();
        int year = mTime.year;
        int month = mTime.month+1;
        int day = mTime.monthDay;
        int hour = mTime.hour;
        int minute = mTime.minute;
        // 双向绑定
        Log.d(TAG, "userLogin: "+user.getName()+","+user.getPwd());
        Toast.makeText(getApplicationContext(),user.getName()+"登录中",Toast.LENGTH_SHORT).show();
        // 单向绑定
        user.setLoginTime("上次登录时间:"+year+"年"+month+"月"+day+"日"+hour+":"+minute);
    }
}

```
代码中，我们先使用`DataBindingUtil.setContentView(MainActivity.this,R.layout.test_main);`获取binding对象，初始化test_main中声明的对象属性`user、Activity`。
接下来为login_btn注册点击事件和编写userLogin方法验证我们的单向、双向绑定。
最后，我们看一下实际的效果，思考一下是不是比以前开发同样需求要轻松很多！所以赶快尝试一下这个开发利器吧。

<img src="https://img-blog.csdnimg.cn/20191103102927731.gif" width="400" height="800">
