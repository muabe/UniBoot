package com.muabe.uniboot.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-08
 */

public class AutoPermission {

    public ArrayList<String> getNames(Context context, @NotNull int... levels){
        ArrayList<String> list = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if(packageInfo.requestedPermissions!= null) {
                for (String permissionName : packageInfo.requestedPermissions) {
                    try {
                        PermissionInfo pinfo = packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA);
                        int bitLevel = pinfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE;
                        for (int level : levels) {
                            if (pinfo.protectionLevel == level || bitLevel == level) {
                                list.add(permissionName);
                                break;
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public ArrayList<PermissionInfo> getAllPermission(Context context){
        ArrayList<PermissionInfo> list = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            for (String permissionName : packageInfo.requestedPermissions) {
                try {
                    PermissionInfo pinfo = packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA);
                    list.add(pinfo);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public ArrayList<String> getDangerous(Context context){
        return getNames(context, PermissionInfo.PROTECTION_DANGEROUS);
    }

    public ArrayList<String> getNormal(Context context){
        return getNames(context, PermissionInfo.PROTECTION_NORMAL);
    }

    public ArrayList<String> getSignature(Context context){
        return getNames(context, PermissionInfo.PROTECTION_SIGNATURE);
    }

    public void checkDangerous(Context context){

    }
}
