package com.gkcreate.athenstour;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkcreate.athenstour.model.PoiList;
import com.gkcreate.athenstour.model.TourItem;
import com.gkcreate.athenstour.model.data.DataHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ActMain extends FragmentActivity implements ActionBar.TabListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, BeaconConsumer {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;


    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    static Activity context;

    GoogleApiClient mGoogleApiClient;

    static GoogleMap map;

    private TextToSpeech texttoSpeech;

    static boolean isMapInit = false;

    Location lastKnownLocation;

    private static int notificationIdentifier = 0;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        final ActionBar actionBar = getActionBar();
        context = this;

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        buildGoogleApiClient();

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                try {


                    Log.d("LOCATION", "changed");
                    String locationProvider = LocationManager.NETWORK_PROVIDER;
                    // Or use LocationManager.GPS_PROVIDER
                    Location loc = locationManager.getLastKnownLocation(locationProvider);

                    if (loc != null) {

                        if (lastKnownLocation != null) {
                            Log.v("SET LOC 1", "set");
                            float[] distance = new float[1];
                            Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), distance);
                            // distance[0] is now the distance between these lat/lons in meters
                            if (distance[0] > 10.0) {
                                if (lastKnownLocation != null && map != null) {
                                    LatLng pos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                    CameraUpdate center = CameraUpdateFactory.newLatLng(pos);
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                                    map.moveCamera(center);
                                    map.animateCamera(zoom);

                                    isMapInit = true;
                                }

                            }
                        } else {
                            Log.v("SET LOC NEW", "set");
                            lastKnownLocation = loc;

                            LatLng pos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            CameraUpdate center = CameraUpdateFactory.newLatLng(pos);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                            map.moveCamera(center);
                            map.animateCamera(zoom);

                            isMapInit = true;
                        }

                    }

                }
                catch (Exception e){
                    Log.v("EXCEPTION",e.toString());
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }


        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        // iBeacon
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        ActionBar actBar = getActionBar();
        if (actBar != null) {
            actBar.setHomeButtonEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // MMM notification
        if (StaticTools.mmmNamesList.size() > 0) {
            displayNotification("  Bus  " + StaticTools.mmmNamesList.get(0) + " arrived. Hop on!");
            speakText("  Bus  " + StaticTools.mmmNamesList.get(0) + " arrived. Hop on!");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class HomeSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.layout_home_view, container, false);

            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerList);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            recyclerView.setAdapter(new TourItemsAdapter(getTourItems()));

            return rootView;
        }

        private ArrayList<TourItem> getTourItems() {

            ArrayList<TourItem> list = new ArrayList<>();
            TourItem item = new TourItem();
            item.setName("Athens centre");

            list.add(item);
            item = new TourItem();
            item.setName("Monastiraki-Panormou");
            list.add(item);

            item = new TourItem();
            item.setName("Omonoia-Peiraias");
            list.add(item);

            return list;
        }


        public class TourItemViewHolder extends RecyclerView.ViewHolder {

            protected TextView titleText;
            protected CardView card;
            protected ImageView img;

            public TourItemViewHolder(View itemView) {
                super(itemView);
                titleText = (TextView) itemView.findViewById(R.id.text_name);
                card = (CardView) itemView;
                img = (ImageView) itemView.findViewById(R.id.img);
            }
        }

        class TourItemsAdapter extends RecyclerView.Adapter<TourItemViewHolder> {

            int[] tourImages = {R.drawable.monast, R.drawable.pir, R.drawable.panormou};

            private List<TourItem> tourItemList;

            public TourItemsAdapter(List<TourItem> items) {
                this.tourItemList = new ArrayList<TourItem>();
                this.tourItemList.addAll(items);
            }

            @Override
            public TourItemViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
                View itemView = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.item_card_home, viewGroup, false);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StaticTools.currentTourItem = tourItemList.get(i);
                        startActivity(new Intent(context, ActTourDetails.class));
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });

                return new TourItemViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(TourItemViewHolder holder, int i) {
                TourItem item = tourItemList.get(i);
                holder.titleText.setText(item.getName());
                if (i < tourImages.length)
                    holder.img.setImageResource(tourImages[i]);
            }

            @Override
            public int getItemCount() {
                return tourItemList.size();
            }

        }
    }

    public static class CatSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.layout_home_categories, container, false);
            return rootView;
        }
    }

    public static class MapSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.layout_home_map, container, false);

            map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapHome))
                    .getMap();
            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                Log.e("MAPS", "Have GoogleMap but then error", e);
            }

            map.setMyLocationEnabled(true);

            StaticTools.setMapPoints(DataHelper.getFoodPoiList(), map);
            StaticTools.setMapPoints(DataHelper.getMuseumsPoiList(), map);
            StaticTools.setMapPoints(DataHelper.getBeachesPoiList(), map);
            StaticTools.setMapPoints(DataHelper.getSightseeingPoiList(), map);


            isMapInit = true;

            return rootView;
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            MapFragment f = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapHome);
            if (f != null)
                getActivity().getFragmentManager().beginTransaction().remove(f).commit();
        }


    }


    public static class DashboardFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.item_dashboard, container, false);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

        }
    }


    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new DashboardFragment();
                case 1:
                    return new HomeSectionFragment();
                case 2:
                    return new CatSectionFragment();
                case 3:
                    return new MapSectionFragment();
                default:
                    return new HomeSectionFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Home";
            } else if (position == 1) {
                return "Tours";
            } else if (position == 2) {
                return "Interesting places";
            } else if (position == 3) {
                return "Nearby";
            }
            return "";
        }
    }


    public void launchCategoryMuseums(View v) {
        launchCategoryScreen(DataHelper.CATEGORY_MUSEUMS);
    }

    public void launchCategorySightSeeeing(View v) {
        launchCategoryScreen(DataHelper.CATEGORY_SIGHTSEEING);
    }

    public void launchCategoryBeaches(View v) {
        launchCategoryScreen(DataHelper.CATEGORY_BEACHES);
    }

    public void launchCategoryTheaters(View v) {
        launchCategoryScreen(DataHelper.CATEGORY_THEATERS);
    }

    public void launchCategoryFood(View v) {
        launchCategoryScreen(DataHelper.CATEGORY_FOOD);
    }

    private void launchCategoryScreen(int category) {
        Intent intent = new Intent(ActMain.this, ActCategoryList.class);
        intent.putExtra(ActCategoryList.CATEGORY_DATA_TO_DISPLAY, category);
        startActivity(intent);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GOOGLE API ERROR", "Code: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GOOGLE API", "suspended");
    }

    @Override
    protected void onPause() {
        if (texttoSpeech != null) {
            texttoSpeech.stop();
            texttoSpeech.shutdown();
            texttoSpeech = null;
        }
        super.onPause();
    }

    private void speakText(String text) {
        if (texttoSpeech != null) {
            texttoSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (texttoSpeech == null) {
            initTextToSpeech();
        }
    }

    private void initTextToSpeech() {
        texttoSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    texttoSpeech.setLanguage(Locale.US);
                    //texttoSpeech.setSpeechRate(0.80f);
                    //texttoSpeech.setPitch(0.9f);
                }
            }
        });
    }

    public void setMapPoints(PoiList list) {

        for (int i = 0; i < list.getPois().size(); i++) {

            LatLng pos = new LatLng(Double.parseDouble(list.getPois().get(i).getLat()), Double.parseDouble(list.getPois().get(i).getLon()));
            map.addMarker(new MarkerOptions().position(pos).title(list.getPois().get(i).getName()));
        }

    }

    public void onClickFood(View v) {
        map.clear();
        setMapPoints(DataHelper.getFoodPoiList());
    }

    public void onClickMuseums(View v) {
        map.clear();
        setMapPoints(DataHelper.getMuseumsPoiList());
    }

    public void onClickBeaches(View v) {
        map.clear();
        setMapPoints(DataHelper.getBeachesPoiList());
    }

    public void onClickSight(View v) {
        map.clear();
        setMapPoints(DataHelper.getSightseeingPoiList());
    }


    public void showAll(View v) {
        map.clear();
        setMapPoints(DataHelper.getFoodPoiList());
        setMapPoints(DataHelper.getMuseumsPoiList());
        setMapPoints(DataHelper.getBeachesPoiList());
        setMapPoints(DataHelper.getSightseeingPoiList());
    }

    public void showMmmConfigurationScreen(View v) {
//        if (StaticTools.mmmNamesList.size() > 0) {
//            displayNotification("Public transport vehicle " + StaticTools.mmmNamesList.get(0) + " arrived.");
//            speakText("Test");
//        } else {
//            displayNotification("Your selected public transport vehicle arrived.");
//            speakText("Test");
//        }
        Intent intent = new Intent(ActMain.this, ActMmmConfiguration.class);
        startActivity(intent);
    }

    public void onClickMyProfile(View v) {
        startActivity(new Intent(this, ActProfile.class));
    }

    public void onClickTours(View v) {
        getActionBar().setSelectedNavigationItem(1);
    }

    public void onClickPlaces(View v) {
        getActionBar().setSelectedNavigationItem(2);
    }

    public void onClickNear(View v) {
        getActionBar().setSelectedNavigationItem(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");

                // MMM notification
                if (StaticTools.mmmNamesList.size() > 0) {
                    displayNotification("  Bus  " + StaticTools.mmmNamesList.get(0) + " arrived. Hop on!");
                    speakText("  Bus  " + StaticTools.mmmNamesList.get(0) + " arrived. Hop on!");
                }
//                else {
//                    displayNotification("Your selected public transport vehicle arrived.");
//                    speakText("B");
//                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    protected void displayNotification(String text) {

        Log.i("Start", "notification");

        // Invoking the default notification service //
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true);

        mBuilder.setContentTitle("Bus arrived");
        mBuilder.setContentText(text);
        mBuilder.setTicker(text);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        // Increase notification number every time a new notification arrives //
        mBuilder.setNumber(0);

        // Creates an explicit intent for an Activity in your app //

//        Intent resultIntent = new Intent(this, ActMain.class);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(FreesmsLog.class);

        // Adds the Intent that starts the Activity to the top of the stack //
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);

//        //  mBuilder.setOngoing(true);
//        Notification note = mBuilder.build();
//        note.defaults |= Notification.DEFAULT_VIBRATE;
//        note.defaults |= Notification.DEFAULT_SOUND;

        // Now we set the vibrate member variable of our Notification
        // After a 100ms delay, vibrate for 200ms then pause for another
        //100ms and then vibrate for 500ms
        mBuilder.setVibrate(new long[]{100, 200, 100, 500, 100, 500});

        mBuilder.setLights(Color.BLUE, 500, 500);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on. //
        mNotificationManager.notify(notificationIdentifier, mBuilder.build());
        notificationIdentifier++;

    }
}



/*
public void connectedNotify() {
    Integer mId = 0;
    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle("Device Connected")
            .setContentText("Click to monitor");

    Intent resultIntent = new Intent(this, MainActivity.class);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent =
          PendingIntent.getActivity(getApplicationContext(),
          0,
          resultIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(resultPendingIntent);
    mBuilder.setOngoing(true);
    Notification note = mBuilder.build();
    note.defaults |= Notification.DEFAULT_VIBRATE;
    note.defaults |= Notification.DEFAULT_SOUND;
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(mId, note);

}
 */