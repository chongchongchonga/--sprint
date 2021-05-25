package com.example.library_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class dingweidaka extends AppCompatActivity {
    MapView mMapView;
    BaiduMap mBaiduMap=null;
      TextView locationInfo;
      LocationClient mLocationClient;
    private static final int msgKey1 = 1;
    private TextView mTime;
    boolean isFirstLocate =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_dingweidaka);
        locationInfo=findViewById(R.id.locationInfo);
        mTime = (TextView) findViewById(R.id.time1);
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        mMapView=findViewById(R.id.bmapView);
        mBaiduMap=mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.setMyLocationEnabled(true);





        requesrtLocation();

        new TimeThread().start();


    }



    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    mTime.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };

    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒

        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay+"日"+"  \n"+"星期"+mWay+"\n"+mHour+":"+mMinute+":"+mSecond;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 ){
                    for(int result :grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "必须同意所有权限", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requesrtLocation();
                }else {
                    Toast.makeText(this,"发生未知错误,",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private  void requesrtLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(5000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
    }


    private  class  MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            navigateTo(bdLocation);
           StringBuilder currentPosition = new StringBuilder();
           currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("   ");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("   ");
//            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
//            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
//            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
//            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
//            currentPosition.append("村镇：").append(bdLocation.getTown()).append("\n");
//            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
//            currentPosition.append("地址：").append(bdLocation.getAddrStr()).append("\n");
            currentPosition.append("定位方式：");
            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation)
            {
                currentPosition.append("网络");
            }
            locationInfo.setText(currentPosition);
        }
    }

    private void navigateTo(BDLocation bdLocation){
        if(isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate=false;
        }

        MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
        locationBuilder.longitude(bdLocation.getLongitude());
        locationBuilder.latitude(bdLocation.getLatitude());
        MyLocationData locationData =locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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


    public void dingwei(View v){
        Toast.makeText(this, "您当前位置不在图书馆附近，无法定位打卡", Toast.LENGTH_SHORT).show();

    }
}
