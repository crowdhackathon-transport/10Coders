package com.gkcreate.athenstour;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActDetails extends Activity{

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_details);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        ((TextView)findViewById(R.id.txtName)).setText(StaticTools.currentPoi.getName());
        LatLng pos = new LatLng(Double.parseDouble(StaticTools.currentPoi.getLat()),Double.parseDouble(StaticTools.currentPoi.getLon()));
        map.addMarker(new MarkerOptions().position(pos).title(StaticTools.currentPoi.getName()));

        CameraUpdate center = CameraUpdateFactory.newLatLng(pos);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
        map.moveCamera(center);
        map.animateCamera(zoom);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String uriBegin = "geo:" + StaticTools.currentPoi.getLat() + "," + StaticTools.currentPoi.getLon();
                String query = StaticTools.currentPoi.getLat() + "," + StaticTools.currentPoi.getLon() + " (Navigation)";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            }
        });
    }
}
