package com.skripsi.greenlab;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionChecker {
    public static String[] REQUIRED_PERMISSION=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final Context context;

    public PermissionChecker(Context context){
        this.context=context;
    }


    public boolean lacksPermissions(String... permissions){
        for(String permission: permissions){
            if(lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission){
        return ContextCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_DENIED;
    }
}
