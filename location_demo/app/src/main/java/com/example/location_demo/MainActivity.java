package com.example.location_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.ContextWrapper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.provider);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //获取系统所有的LocationProvider名称
        List<String> providersNames = locationManager.getAllProviders();
        StringBuilder stringBuilder = new StringBuilder();  //使用StringBuilder保存数据
        //遍历获取到的全部LocationProvider名称
        for (Iterator<String> iterator = providersNames.iterator(); iterator.hasNext(); ) {
            stringBuilder.append(iterator.next() + "\n");
        }
        textView.setText(stringBuilder.toString());  //显示LocationProvider名称

        //获取基于GPS的LocationProvider
        LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        textView.setText(locationProvider.getName());

//        Criteria criteria=new Criteria();   //创建过滤条件
//        criteria.setCostAllowed(false);    //使用不收费的
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //使用精度最准确的
//        criteria.setPowerRequirement(Criteria.POWER_LOW);  //使用耗电量最低的
//        //获取最佳的LocationProvider名称
//        String provider=locationManager.getBestProvider(criteria,true);
//        textView.setText(provider);  //显示最佳的LocationProvider名称

        //权限检查
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,//GPS定位提供者
                1000,//间隔时间
                1,//位置间隔
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {//定位信息改变时回调

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {//定位状态改变时回调

                    }

                    @Override
                    public void onProviderEnabled(String provider) {//定位开启时回调

                    }

                    @Override
                    public void onProviderDisabled(String provider) {//定位关闭时回调

                    }
                }
        );

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//获取最新的定位信息
        locationUpdates(location);//将最新的定位信息传入

    }

    public void locationUpdates(Location location){
        if(location != null){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("您的位置是：\n");
            stringBuilder.append("经度：");
            stringBuilder.append(location.getLongitude());
            stringBuilder.append("\n纬度是：");
            stringBuilder.append(location.getLatitude());
            textView.setText(stringBuilder.toString());
        }
        else{
            textView.setText("没有获得GPS信息");
        }
    }
}
