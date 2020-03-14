package com.qttx.kedouhulian.utils;


import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qttx.kedouhulian.App;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by huangyr
 * on 2017/12/12.
 */

public class LocationHelper {

    private AMapLocationClient locationClient = null;
    private LocationListener locationCallBck;

    public LocationHelper(@NonNull LocationListener locationCallBck) {
        this.locationCallBck = locationCallBck;
        //初始化client
        locationClient = new AMapLocationClient(App.Companion.getInstance());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void startLocation() {

        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (locationCallBck == null) {
                return;
            }
            if (null != loc && loc.getErrorCode() == 0) {
                //解析定位结果
                LocationBean bean = new LocationBean();

                StringBuilder builder = new StringBuilder();
                builder.append(loc.getStreet())
                        .append(loc.getStreetNum())
                        .append(loc.getPoiName());

                bean.setAddress(builder.toString());
                bean.setLatitude(loc.getLatitude());
                bean.setLongitude(loc.getLongitude());
                bean.setAdcode(loc.getAdCode());
                bean.setProvince(loc.getProvince());
                bean.setDistrict(loc.getDistrict());
                bean.setCityName(loc.getCity());
                bean.setCityCode(loc.getCityCode());
                bean.setPoiname(loc.getPoiName());
                locationCallBck.getLocationSuccess(bean,loc);
            } else {
                locationCallBck.getLocationFailed();
            }
        }
    };

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        locationCallBck = null;
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    public interface LocationListener {

        void getLocationSuccess(LocationBean locationBean, AMapLocation aMapLocation);

        void getLocationFailed();
    }

    public static class LocationBean   {


        private double longitude;

        private double latitude;


        public int getIsCustomSelect() {
            return isCustomSelect;
        }

        public void setIsCustomSelect(int isCustomSelect) {
            this.isCustomSelect = isCustomSelect;
        }

        /**
         * 0是我的定位
         * 1.是城市选择的
         * 2.是地图拖动的
         */
        private int  isCustomSelect;


        /**
         * 省份
         */
        private String province;
        /**
         * 城市
         */
        public String cityName;
        public String cityCode;

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String adcode;
        /**
         * 区
         */
        public String district;
        /**
         * 描述性地址,街道加街道号加位置
         */
        private String address;
        /**
         * 描述性poiname
         */
        private String poiname;
        private int cityId;
        private int districtId;

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        private int provinceId;

        public String getPoiname() {
            return poiname;
        }

        public void setPoiname(String poiname) {
            this.poiname = poiname;
        }

        public double getLongitude() {
            return longitude;
        }


        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }


        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }


        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }


        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }


        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
}
