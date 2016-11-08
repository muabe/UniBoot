package com.muabe.uniboot.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-08
 */

public class AutoPermission {

    public void check(Context context){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            for (String permission : packageInfo.requestedPermissions) {
                try {
                    PermissionInfo pinfo = packageManager.getPermissionInfo(permission, PackageManager.GET_META_DATA);
                    String protectionLevel;
                    switch(pinfo.protectionLevel) {
                        case PermissionInfo.PROTECTION_NORMAL : protectionLevel = "normal"; break;
                        case PermissionInfo.PROTECTION_DANGEROUS : protectionLevel = "dangerous"; break;
                        case PermissionInfo.PROTECTION_SIGNATURE : protectionLevel = "signature"; break;
                        default : protectionLevel = "<unknown>"; break;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
