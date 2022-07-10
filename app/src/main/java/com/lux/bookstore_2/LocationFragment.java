package com.lux.bookstore_2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.kakao.util.maps.helper.Utility;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class LocationFragment extends Fragment {

    MapView mapView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MaterialToolbar materialToolbar=view.findViewById(R.id.toolbar_locationView);
        ((MainActivity)getActivity()).setSupportActionBar(materialToolbar);

        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);




        mapView=new MapView(getActivity());
        RelativeLayout mapViewContainer=view.findViewById(R.id.container_map);
        mapViewContainer.addView(mapView);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);

        mapView.setZoomLevel(7,true);

        mapView.zoomIn(true);
        mapView.zoomOut(true);

        MapPoint mapPoint=MapPoint.mapPointWithGeoCoord(37.5,127.5);
        MapPOIItem marker=new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(marker);

        String keyHash= Utility.getKeyHash(getActivity());
        Log.i("KEY",keyHash);


    }
}
