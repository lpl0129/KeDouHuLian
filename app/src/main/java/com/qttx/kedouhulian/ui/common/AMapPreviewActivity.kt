package com.qttx.kedouhulian.ui.common

import android.os.Bundle
import android.os.Parcelable

import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.utils.NavUtils
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.widget.dialog.ListDialog

import io.rong.imkit.plugin.location.AmapInfoWindowAdapter
import io.rong.message.LocationMessage

class AMapPreviewActivity : BaseActivity() {
    private var mAMapView: MapView? = null


    override fun onDestroy() {
        this.mAMapView!!.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        this.mAMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mAMapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.mAMapView!!.onSaveInstanceState(outState)
    }

    override fun getLayoutId(): Int {
        return R.layout.chat_activity_location
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("位置", "导航")
        {
            val locationMessage = intent.getParcelableExtra<Parcelable>("location") as LocationMessage

            val location = DoubleArray(2)
            location[0] = locationMessage.lat
            location[1] = locationMessage.lng

            val list = mutableListOf<String>()
            list.add("百度地图")
            list.add("高德地图")
            list.add("腾讯地图")
            ListDialog.newInstance(list, "")
                    .setListener {
                        setListItemClick { position, selectText ->

                            when (position) {
                                0 -> {
                                    NavUtils.baiduGuide(this@AMapPreviewActivity, location)
                                }
                                1 -> {
                                    NavUtils.gaodeGuide(this@AMapPreviewActivity, location)
                                }
                                2 -> {
                                    NavUtils.tencentGuide(this@AMapPreviewActivity, location)
                                }
                            }
                        }
                    }
                    .show(supportFragmentManager)


        }
        this.mAMapView = this.findViewById(R.id.rc_ext_amap)
        this.mAMapView!!.onCreate(savedInstanceState)
        initMap()
    }

    override fun liveDataListener() {

    }

    private fun initMap() {
        val amap = this.mAMapView!!.map
        amap.isMyLocationEnabled = false
        amap.uiSettings.isTiltGesturesEnabled = false
        amap.uiSettings.isZoomControlsEnabled = false
        amap.uiSettings.isMyLocationButtonEnabled = false
        val intent = this.intent
        val locationMessage = intent.getParcelableExtra<Parcelable>("location") as LocationMessage
        val lat = locationMessage.lat
        val lng = locationMessage.lng
        val poi = locationMessage.poi
//        val markerOptions = MarkerOptions().anchor(0.5f, 0.5f).position(LatLng(lat, lng)).title(poi).snippet("$lat,$lng").draggable(false)

        val markerOption = MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.user_location))
                .position(LatLng(lat, lng))
                .title(poi)
                .draggable(true)
        val marker = amap.addMarker(markerOption)

        mAMapView?.postDelayed(
                {
                    if (marker != null)
                        marker.showInfoWindow()
                }
                ,1000
        )
        amap.setInfoWindowAdapter(AmapInfoWindowAdapter(this))
//        marker.showInfoWindow()
        amap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(LatLng(lat, lng)).zoom(16.0f).bearing(0.0f).build()))
    }
}