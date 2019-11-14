package com.greenbike;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    private MapView mMapView;     // 定义百度地图组件
    private BaiduMap mBaiduMap;   // 定义百度地图对象
    private LocationClient mLocationClient;  //定义LocationClient
    private BDLocationListener myLocationListener = new MyLocationListener();  //定义位置监听
    private boolean isFirstLoc = true;  //定义第一次启动
    private MyLocationConfiguration.LocationMode mCurrentMode;  //定义当前定位模式

    private LocationManager locationManager;
    private static final int LOCATION_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());   //初始化地图SDK
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        start_map();
    }

    private void start_map(){
        mMapView = (MapView) findViewById(R.id.bmapView);  //获取地图组件
        mBaiduMap = mMapView.getMap();  //获取百度地图对象
        mLocationClient = new LocationClient(getApplicationContext());  //创建LocationClient类

        mLocationClient.registerLocationListener(myLocationListener);   //注册监听函数
        initLocation();  //调用initLocation()方法，实现初始化定位

        //按钮
        findViewById(R.id.bt_back).setOnClickListener(new Main3Activity.ButtonListener());
    }

    private void judge_permssion(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean flag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (flag) {//开了定位服务
            if (ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("BRG","没有权限");
                // 没有权限，申请权限。
                // 申请授权。
                ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                Toast.makeText(Main3Activity.this, "没有权限", Toast.LENGTH_SHORT).show();

            } else {
                // 有权限了，去放肆吧。
                Toast.makeText(Main3Activity.this, "已经开启权限", Toast.LENGTH_SHORT).show();

            }
        } else {
            Log.e("BRG","系统检测到未开启GPS定位服务");
            Toast.makeText(Main3Activity.this, "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1315);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。
                    Toast.makeText(Main3Activity.this, "已经开启权限", Toast.LENGTH_SHORT).show();

                } else {
                    // 权限被用户拒绝了。
                    Toast.makeText(Main3Activity.this, "定位权限被禁止，相关地图功能无法使用！",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void initLocation() {  //该方法实现初始化定位
        //创建LocationClientOption对象，用于设置定位方式
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");  //设置坐标类型
        option.setScanSpan(1000);      //1秒定位一次
        option.setOpenGps(true);      //打开GPS
        mLocationClient.setLocOption(option);  //保存定位参数与信息
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;  //设置定位模式
        //设置自定义定位图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        //位置构造方式，将定位模式，定义图标添加其中
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);  //地图显示定位图标
    }

    public class MyLocationListener implements BDLocationListener {  //设置定位监听器
        @Override
        public void onReceiveLocation(BDLocation location) {
            //销毁后不再处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())//29.8901109328,location.getLatitude()
                    .longitude(location.getLongitude())//location.getLongitude(),120.3875585874
                    .build();
            System.out.println("测试：经度"+location.getLatitude());
            System.out.println("测试：纬度"+location.getLongitude());
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {  //如果是第一次定位,就定位到以自己为中心
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude()); //获取用户当前经纬度
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);  //更新坐标位置
                mBaiduMap.animateMapStatus(u);                            //设置地图位置
                isFirstLoc = false;                                      //取消第一次定位
            }
        }
    }

    class ButtonListener implements View.OnClickListener {
        //实现单击事件处理方法
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.bt_back) {
                //蓝牙
                Intent intent = new Intent();
                intent.setClass(Main3Activity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {  //地图定位与Activity生命周期绑定
        super.onStart();
        judge_permssion();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }

    @Override
    protected void onStop() {  //停止地图定位
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {  //销毁地图
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}
