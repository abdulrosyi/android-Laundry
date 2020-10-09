package com.skripsi.greenlab;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionsActivity extends AppCompatActivity {

    public static final int PERMISSION_GRANTED=0,PERMISSION_DENIED=1,PERMISSION_REQUEST_CODE=0;
    public static final String EXTRA_PERMISSION="com.goblo.skripshit.EXTRA_PERMISSIONS",PACKAGE_URL_SCHEME="package:";
    private PermissionChecker checker;
    private boolean requiresCheck;

    public static void startActivityForResult(@NonNull Activity activity, int requestCode, String... permissions){
        Intent inten=new Intent(activity,PermissionsActivity.class);
        inten.putExtra(EXTRA_PERMISSION, permissions);
        ActivityCompat.startActivityForResult(activity,inten,requestCode,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()==null || !getIntent().hasExtra(EXTRA_PERMISSION)){
            throw new RuntimeException("this Activity needs to be launched using the static startActivityForResult() method");
        }
        checker=new PermissionChecker(this);
        requiresCheck=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(requiresCheck){
            String[] permissions=getPermissions();
            if (checker.lacksPermissions(permissions)){
                requestPermissions(permissions);
            }else{
                allPermissionGranted();
            }
        }else{
            requiresCheck=true;
        }
    }

    private String[] getPermissions(){
        return getIntent().getStringArrayExtra(EXTRA_PERMISSION);
    }

    private void requestPermissions(@NonNull String...permissions){
        ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST_CODE);
    }

    private void allPermissionGranted(){
        setResult(PERMISSION_GRANTED);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        if(requestCode==PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)){
            requiresCheck=true;
            allPermissionGranted();
        }else{
            requiresCheck=false;
            showMissingPermissionDialog();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults){
        for(int grantResult : grantResults){
            if(grantResult== PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog(){
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.string_permission_help);
        dialogBuilder.setMessage(R.string.string_permission_help_text);
        dialogBuilder.setNegativeButton(R.string.string_permission_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(PERMISSION_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.string_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }

    private void startAppSettings(){
        Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME+getPackageName()));
        startActivity(intent);
    }
}
