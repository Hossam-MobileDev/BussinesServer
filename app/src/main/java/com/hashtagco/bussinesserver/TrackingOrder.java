package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.hashtagco.bussinesserver.Common.Common;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TrackingOrder extends AppCompatActivity{
//        implements OnMapReadyCallback{

   /* private GoogleMap mMap;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11101;
    private SupportMapFragment mapFragment;
    private Switch btnSwitch;
    private Button btnCallClint,btnCallShipper;
    //----------------------------
    private FirebaseDatabase database;
    private DatabaseReference refDriversLocation;


    //-------------
    //-------------
    private Marker markerYorShipper;
    private   Marker markerClintLocation;
    private   Marker markerServerAppLocation;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoFire geoFireDataBase; //???????? ???????? ???????? ?????????? ???????????? ????????????

    //---------Direction--------//
    private LatLng shipperCurrentLocation, clintCurrentLocation,serverCurrentLocation;
    //-----------------
    private   String shipperPhone;
    private   String clientPhone;*/


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);



       /* initViews();

        //-------init--------//
        database = FirebaseDatabase.getInstance();
        refDriversLocation = database.getReference(Common.DRIVER_LOCATION);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geoFireDataBase = new GeoFire(refDriversLocation);

        //--Get Constant Location Clint
        if (getIntent() != null)
        {

            String latLngClint = getIntent().getStringExtra("Latlng");
            shipperPhone = getIntent().getStringExtra("shipperPhone");
            clientPhone=getIntent().getStringExtra("clientPhone");

            String[] separatedLocation = latLngClint.split(",");

            separatedLocation[0] = separatedLocation[0].trim();
            separatedLocation[1] = separatedLocation[1].trim();

            clintCurrentLocation = new LatLng(Double.parseDouble(separatedLocation[0]), Double.parseDouble(separatedLocation[1]));


        }

        //--------Check open GPS ------------//
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
*/
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }



    private void initViews() {
        //-------------Id------------------//
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnSwitch = (Switch) findViewById(R.id.pin);
        btnCallClint = (Button)findViewById(R.id.btnCallClien);
        btnCallShipper=(Button)findViewById(R.id.btnCallShipp);


        //------Event---------------//

        //---Run Map and Gel your Location and Draw Rout to ClintLocation

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked) //True
                {
                    startGettingLocation();
                } else //False
                {

                    stopGettingLocation();
                }


            }
        });




        btnCallShipper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                String uri = "tel:" + shipperPhone.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));

                if (ActivityCompat.checkSelfPermission(TrackingOrder.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(intent);
            }
        });


        btnCallClint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String uri = "tel:" + clientPhone.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));

                if (ActivityCompat.checkSelfPermission(TrackingOrder.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(intent);
            }
        });


    }


    //---------Get Your Location-------//
    private void startGettingLocation()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}
                    ,MY_PERMISSIONS_REQUEST_LOCATION);
        }else
        {
            prepareLocationRequest();
            prepareCallBack();
            fusedLocationProviderClientPermission();
        }


    }

    private  void stopGettingLocation()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        fusedLocationProviderClient.removeLocationUpdates(locationCallback); //???????? ????response ???????? ???????? ???? ??????????????
        mMap.setMyLocationEnabled(false);

        Snackbar.make(mapFragment.getView(),"Your OffLine", Snackbar.LENGTH_SHORT).show();


    }

    private void fusedLocationProviderClientPermission()
    {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);

        Snackbar.make(mapFragment.getView(),"Your Online",Snackbar.LENGTH_SHORT).show();

    }

    private void prepareLocationRequest()
    {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setSmallestDisplacement(.00001f);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void prepareCallBack()
    {
        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {super.onLocationResult(locationResult);

                //Your Location
                List<Location>locations=locationResult.getLocations();

                if (locations.size()>0)
                {
                    final Location mLastLocationServerApp = locations.get(locations.size() - 1);//First Rout Storage in List

                    //---get Storage Location In Firebase
                    geoFireDataBase
                            .getLocation(shipperPhone, new com.firebase.geofire.LocationCallback() {
                                @Override
                                public void onLocationResult(String key, GeoLocation locationShipper) {

                                    //--------------------------------Server App----------------------------------//
                                    //Marker Clint
                                    if (markerServerAppLocation != null)//???????? ?????????? ?????? ?????????????? ?????????? location
                                        markerServerAppLocation.remove(); //???????? ?????????????? ?????? ???????? ?????????? ????location ???????????? ????????????

                                    //Add Clint Marker
                                    markerServerAppLocation = mMap.addMarker(new MarkerOptions()
                                            .title("Food Spotting")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.
                                                    storeonline))
                                            .position(new LatLng(mLastLocationServerApp.getLatitude(), mLastLocationServerApp.getLongitude())));

                                    //Add Circle Around Location Clint
                                    CircleOptions circleOptionsServer = new CircleOptions();
                                    circleOptionsServer.center(new LatLng(mLastLocationServerApp.getLatitude(), mLastLocationServerApp.getLongitude()));
                                    circleOptionsServer.radius(20);
                                    circleOptionsServer.fillColor(Color.GREEN);
                                    circleOptionsServer.strokeColor(Color.RED);
                                    circleOptionsServer.strokeWidth(4);

                                    mMap.addCircle(circleOptionsServer);

                                    //-------------------------------Shipper-----------------------------------------//

                                    //Marker Your Location Shipper
                                    if (markerYorShipper != null)//???????? ?????????? ?????? ?????????????? ?????????? location
                                        markerYorShipper.remove(); //???????? ?????????????? ?????? ???????? ?????????? ????location ???????????? ????????????

                                    //Add Marker On Your Location
                                    markerYorShipper = mMap.addMarker(new MarkerOptions()
                                            .title("Shipper")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markershipper))
                                            .position(new LatLng(locationShipper.latitude, locationShipper.longitude)));

                                    //markerYorShipper.setDraggable(true);//???????? ???????? ?????????? ??????????

                                    //???????? ?????? ???????? ????animatCamer ?????? ?????? ??????????zome ?????? ????yourLocation ???????????????? ???????? ????????????
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationShipper.latitude, locationShipper.longitude), 15));

                                    //-------------------------------------------Client-----------------------------------//
                                    //Marker Clint
                                    if (markerClintLocation != null)//???????? ?????????? ?????? ?????????????? ?????????? location
                                        markerClintLocation.remove(); //???????? ?????????????? ?????? ???????? ?????????? ????location ???????????? ????????????

                                    //Add Clint Marker
                                    markerClintLocation = mMap.addMarker(new MarkerOptions()
                                            .title("Your Location")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerclint))
                                            .position(new LatLng(clintCurrentLocation.latitude, clintCurrentLocation.longitude)));

                                    //Add Circle Around Location Clint
                                    CircleOptions circleOptionsClient = new CircleOptions();
                                    circleOptionsClient.center(new LatLng(clintCurrentLocation.latitude, clintCurrentLocation.longitude));
                                    circleOptionsClient.radius(50);
                                    //circleOptionsClient.fillColor(Color.BLUE);
                                    circleOptionsClient.strokeColor(Color.RED);
                                    circleOptionsClient.strokeWidth(4);

                                    mMap.addCircle(circleOptionsClient);

                                    //-----------------------------------------------------------------------------------//

                                    //---After Call Back Draw And Chose Route between 2 Points
                                    shipperCurrentLocation = new LatLng(locationShipper.latitude, locationShipper.longitude);
                                    serverCurrentLocation=new LatLng(mLastLocationServerApp.getLatitude(),mLastLocationServerApp.getLongitude());

                                    // getDirectionAndDraw();
                                    if (shipperCurrentLocation != null && clintCurrentLocation != null && serverCurrentLocation !=null) {

                                        getDirectionAndDraw(shipperCurrentLocation, clintCurrentLocation,serverCurrentLocation);
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }
            }
        };


    }

    *//*
    * holder.btnTrackingShipper.setVisibility(View.GONE); //will hide the button and space acquired by button
                    //// holder.btnTrackingShipper.setVisibility(View.INVISIBLE); //will hide button only.
                    if (adapter.getItem(position).getStatus().equals("2")) //shipping
                    {

                        holder.btnTrackingShipper.setVisibility(View.VISIBLE); //Shaw Button

                        //open Map for Shaw RealTime Location to Shipper
                        holder.btnTrackingShipper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intentTracing=new Intent(OrderStatus.this,TrackingShipper.class);
                                       intentTracing.putExtra("Latlng",model.getLatlng());
                                       intentTracing.putExtra("shipperPhone",model.getPhoneShipper());
                                       startActivity(intentTracing);
                            }
                        });
                    }else
                        {
                            holder.btnTrackingShipper.setVisibility(View.GONE); //will hide button only.
                        }
    *
    * *//*


    //Method Open Gps
    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:

                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    prepareLocationRequest();
                    prepareCallBack();
                    fusedLocationProviderClientPermission();

                }else
                {
                    Toast.makeText(this, "?????????????? ?????????????????? ?????????? ", Toast.LENGTH_SHORT).show();
                }

                break;




        }



    }

    private void getDirectionAndDraw(LatLng shipperCurrentLocation, LatLng clintCurrentLocation,LatLng serverCurrentLocation)
    {
        //?????????????? ?????????????? ?????????????????? ?????? ???????????? ?????? ???????? ??????????
        GoogleDirection.withServerKey(getResources().getString(R.string.MAPS_API_KEY))
                .from(shipperCurrentLocation)
                .and(clintCurrentLocation)
                .to(serverCurrentLocation)
                .transportMode(TransportMode.DRIVING) //?????????????? ?????? ???????? ???????? ?????????????? ?????????? ????????????????
                //.avoid(AvoidType.HIGHWAYS) //???????????? ?????? ???????? ?????????? ??????????????
                //.avoid(AvoidType.FERRIES)
                .transitMode(TransitMode.BUS)
                .language("AR")
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override//???? ?????? ?????? ?????????????? ?????? ?????????????????? ???????? ??????
                    public void onDirectionSuccess(Direction direction, String rawBody) //?????????? ?????? ???? ???????? ?????????????????? ?????? ?????????? ?????????? ?????????????????? ???? ?????????? ???????? ?????? ?????????? ???????? ???? ???????? ?????????????? ?????? ???? ?????? ?????????????? ???????? ?????? ??????????????????
                    {
                        //?????????? ?????????????? ???????????? ?????? ???????? List ?????????? ?????? Route ???? ???????? ?????? ???????????? ???????? ???????? ?????? ?????? ???????????? ?????????? ?????? ???? ?????? ????????????
                        //?????????? ?????? Route ?????? Leg ???? ????????  ?????? ?????????????? ?????? ???????? ????????
                        //???????????? ?????? Leg ?????? step ?????? ?????????????? ?????? ???????? ???????? ???????? ?????? ?????? Leg
                        //?????? ???????????? ?????? ???? ?????????????? ?????????? ?????????? ???????? ???????????? Polyline ???????????? ?????? ?????????????? ?????????? ?????? ???????????? ?????????????????? ?????? ?????????? ?????????? ?????? ???????? ???? ?????????? ????????????

                        if (direction.isOK()) {
*//*

                            Leg leg = direction.getRouteList().get(0).getLegList().get(0);

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(TrackingOrder.this, directionPositionList, 5, Color.RED);//.jointType(JointType.ROUND);

                            mMap.addPolyline(polylineOptions);
*//*


                            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();

                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(TrackingOrder.this, stepList, 5, Color.RED, 3, Color.BLUE);

                            for (PolylineOptions polylineOption : polylineOptionList)
                            {
                                mMap.addPolyline(polylineOption);
                            }




                            Log.e("Direction", "onDirectionSuccess: ");
                        } else {

                            Log.e("Direction", direction.getErrorMessage());

                        }

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("Direction", "faild" + t.getLocalizedMessage());
                    }
                });
    }


*/

}