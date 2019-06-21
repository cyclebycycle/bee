package com.ccnu.hiic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ccnu.hiic.Bean.UserInfo;

/**
 * 保存用户信息的管理类
 */

public class UserManage {

    private static UserManage instance;

    private UserManage() {
    }

    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }


    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String username, String password,int monitor,String areaName_get,String companyName_get) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", username);
        editor.putString("PASSWORD", password);
        editor.putInt("monitor", monitor);
        editor.putString("areaName_get", areaName_get);
        editor.putString("companyName_get", companyName_get);
        editor.commit();
    }


    /**
     * 获取用户信息model
     *
     * @param context
     * @param
     * @param
     */
    public UserInfo getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(sp.getString("USER_NAME", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        userInfo.setMonitor(sp.getInt("monitor",-1));
        userInfo.setAreaName_get(sp.getString("areaName_get",""));
        userInfo.setCompanyName_get(sp.getString("companyName_get",""));
        return userInfo;
    }


    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        if (userInfo != null) {
            if ((!TextUtils.isEmpty(userInfo.getUserName())) || (!TextUtils.isEmpty(userInfo.getPassword())) || (userInfo.getMonitor()!=-1)
             || (!TextUtils.isEmpty(userInfo.getAreaName_get())) || (!TextUtils.isEmpty(userInfo.getCompanyName_get()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public  void clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
