package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.santeh.petone.crm.Adapter.Adapter_MapsActivity;
import com.santeh.petone.crm.DBase.DB_Helper_PetOneCRM;
import com.santeh.petone.crm.DBase.DB_Query_PetOneCRM;
import com.santeh.petone.crm.Obj.CustInfoObject;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.FusedLocation;
import com.santeh.petone.crm.Utils.Helper;
import com.santeh.petone.crm.Utils.Logging;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FusedLocation fusedLocation;
    private Marker selectedMarker;
    Activity activity;
    Context context;

    TextView txtposition, txtName, txtMaptype, txtExit, txtTop, txtSettings;
    ImageButton btn_AddMarker, btn_closeAddMarker, btnChangeMarkerLocation, btnDeleteMarker;
    DrawerLayout drawerLayout;

    CircleOptions circleOptions_addLocation;
    Circle mapcircle;

    DB_Helper_PetOneCRM dbHelper;
    DB_Query_PetOneCRM db;
    ListView lvcustomers;
    List<CustInfoObject> customerList;
    public static int requestCODE_addMarker = 1;
    Adapter_MapsActivity adapterMapsActivity;
    boolean isMarkerSelected = false;
    ViewGroup hiddenPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        activity = this;
        context = MapsActivity.this;
        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);
        fusedLocation.connectToApiClient();

        dbHelper = new DB_Helper_PetOneCRM(this);
        db = new DB_Query_PetOneCRM(this);
        db.open();

    }






    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);



        mMap = googleMap;

        btn_AddMarker = (ImageButton) findViewById(R.id.btnAddMarker);
        btn_closeAddMarker = (ImageButton) findViewById(R.id.btnCloseAddMarker);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtposition = (TextView) findViewById(R.id.txt_position);
        txtMaptype = (TextView) findViewById(R.id.txt_maptype);
        txtExit = (TextView) findViewById(R.id.txt_exit);
        txtSettings = (TextView) findViewById(R.id.txt_settings);
        btn_closeAddMarker.setVisibility(View.GONE);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        lvcustomers = (ListView) findViewById(R.id.lv_map_customers);
        hiddenPanel = (ViewGroup)findViewById(R.id.ll_bottomedit);
        btnChangeMarkerLocation = (ImageButton) findViewById(R.id.btn_changeMarkerLocation);
        btnDeleteMarker = (ImageButton) findViewById(R.id.btn_deleteMarker);

        txtTop = (TextView) findViewById(R.id.txtTopTextView);
        txtTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_Unsynched_Data.class);
                startActivity(intent);
            }
        });

        showSycnState();



        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        txtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(activity, Activity_Settings.class);
                        startActivity(intent);
                    }
                }, 280);
            }
        });

        txtMaptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String[] maptypes = {"Normal", "Satellite", "Terrain", "Hybrid"};
                        final Dialog dd = Helper.common.dialogThemedList(activity, maptypes, "Map Types", R.color.green_500);
                        ListView lstMapType = (ListView) dd.findViewById(R.id.dialog_list_listview);
                        dd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dd.show();

                        lstMapType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    dd.hide();
                                } else if (position == 1) {
                                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                    dd.hide();
                                } else if (position == 2) {
                                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                    dd.hide();
                                } else if (position == 3) {
                                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                    dd.hide();
                                }
                            }
                        });
                    }
                }, 280);
            }
        });
        txtName.setText(Helper.variables.getGlobalVar_currentUserFirstname(activity)
                + " " + Helper.variables.getGlobalVar_currentUserLastname(activity));
        String position = "";
        if (Helper.variables.getGlobalVar_currentLevel(activity) == 0) {
            position = "Admin";
        }else  if (Helper.variables.getGlobalVar_currentLevel(activity) == 1) {
            position = "Top Management";
        }else  if (Helper.variables.getGlobalVar_currentLevel(activity) == 2) {
            position = "Manager";
        }else  if (Helper.variables.getGlobalVar_currentLevel(activity) == 3) {
            position = "Supervisor";
        }else  if (Helper.variables.getGlobalVar_currentLevel(activity) == 4) {
            position = "TSR";
        }
        txtposition.setText(position);

        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);
        fusedLocation.disconnectFromApiClient();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    fusedLocation.connectToApiClient();
                    LatLng latLng = fusedLocation.getLastKnowLocation();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(zoom);
//                    Helper.common.toastShort(activity, Helper.variables.getGlobalVar_currentUserName(activity));

                } catch (Exception e) {
                    LatLng center = new LatLng(12.832288, 122.524313);
                    Helper.map.moveCameraAnimate(googleMap, center, 6);
                }
            }
        }, 280);


        btn_closeAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closerAddingMarker();
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] splitted = marker.getSnippet().split("~");
                String id = splitted[0];

                Intent intent = new Intent(MapsActivity.this, Activity_ClientUpdates.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });
        lvcustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                closeDrawer();
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Helper.map.moveCameraAnimate(googleMap, customerList.get(position).getLatLng(), 17);
                    }
                }, 300);


            }
        });


        btn_AddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.map.isLocationEnabled(context)) {
                    fusedLocation.disconnectFromApiClient();
                    fusedLocation.connectToApiClient();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final LatLng center = fusedLocation.getLastKnowLocation();
                            Helper.map.moveCameraAnimate(googleMap, center, 19);

                            final Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (mapcircle == null || !mapcircle.isVisible()) {
                                        circleOptions_addLocation = Helper.map.addCircle(activity, center, 1, R.color.skyblue_20,
                                                R.color.skyblue_20, 1000);
                                        mapcircle = googleMap.addCircle(circleOptions_addLocation);
                                    }
                                    btn_closeAddMarker.setVisibility(View.VISIBLE);
                                    Helper.common.dialogThemedOkOnly(activity, "Add Marker", "Long press any location inside the blue circle to add a marker.", "OK", R.color.skyblue_500);

                                    if (btn_AddMarker.isEnabled()) {
                                        btn_AddMarker.setEnabled(false);
                                    }

                                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                        @Override
                                        public void onMapLongClick(LatLng latLng) {

                                            final LatLng touchLocation = latLng;
                                            LatLng center = fusedLocation.getLastKnowLocation();

                                            float[] results = new float[1];
                                            Location.distanceBetween(center.latitude, center.longitude,
                                                    touchLocation.latitude, touchLocation.longitude, results);

                                            if (results[0] > 1000) {
                                                final Dialog d = Helper.common.dialogThemedOkOnly(activity, "Out of range", "Selection is out of 1km range from your location", "OK", R.color.red);
                                                d.show();

                                                Button ok = (Button) d.findViewById(R.id.btn_dialog_okonly_OK);
                                                ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        d.hide();
                                                    }
                                                });
                                            } else {
                                                Intent intent = new Intent(activity, Activity_Add_ClientInfo.class);
                                                intent.putExtra("userid", Helper.variables.getGlobalVar_currentUserID(activity) + "");
                                                intent.putExtra("lat", latLng.latitude);
                                                intent.putExtra("long", latLng.longitude);
                                                closerAddingMarker();
                                                startActivityForResult(intent, requestCODE_addMarker);
                                            }


                                        }
                                    });
                                }
                            }, 1400);
                        }
                    }, 200);
                } else {
                    Helper.common.toastShort(activity, "Location Service not Available!");
                }

            }
        });

        showAllMarker(googleMap);


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                isMarkerSelected = true;
                animateBottom();

                selectedMarker = marker;

                String splitter = "~";
                String[] splitted = selectedMarker.getSnippet().split(splitter);

                if (db.isClientInfoPosted(splitted[0])) {
                    btnChangeMarkerLocation.setEnabled(false);
                    btnDeleteMarker.setEnabled(false);
                }else{
                    btnChangeMarkerLocation.setEnabled(true);
                    btnDeleteMarker.setEnabled(true);
                }


                return false;
            }
        });


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (isMarkerSelected) {
                    isMarkerSelected = false;
                }
                animateBottom();
            }
        });

        btnDeleteMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMarker != null){


                    final Dialog d = Helper.common.dialogThemedYesNO(activity, "Are you sure you want to delete selected marker?" +
                            "\nNOTE: All updates related to this will also be deleted", "Delete", "NO", "YES", R.color.red_material_600);
                    Button btnyes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                    Button btnNo = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);

                    btnyes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.hide();
                            if(db.deleteClientInfoWithUpdatesByInfoID(Helper.random.splitter("~", selectedMarker.getSnippet())[0]) ){
                                selectedMarker.remove();
                                isMarkerSelected = false;
                                Logging.userAction(activity, context, Logging.ACTION_DELETE, Helper.random.splitter("~", selectedMarker.getSnippet())[0],
                                        DB_Helper_PetOneCRM.TBL_UPDATES, Logging.TYPE_USER);
                                animateBottom();
                                showSycnState();

                                Helper.common.toastShort(activity, "Marker was successfully deleted.");
                            }else{
                                Helper.common.toastShort(activity, "Delete failed. Please try again.");
                                animateBottom();
                            }
                        }
                    });

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.hide();
                        }
                    });

                }
            }
        });

        btnChangeMarkerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMarker != null) {

                    Helper.common.dialogThemedOkOnly(activity, "Change Location", "Longpress marker until InfoWindow is gone then drag marker to desired location.", "OK", R.color.blue_400);
                    hiddenPanel.setVisibility(View.GONE);

                    final LatLng[] originalPosition = new LatLng[1];

                    selectedMarker.setDraggable(true);
                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                            hiddenPanel.setVisibility(View.GONE);
                            marker.hideInfoWindow();
                            originalPosition[0] = marker.getPosition();
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {
                            hiddenPanel.setVisibility(View.GONE);
                            marker.hideInfoWindow();
                        }

                        @Override
                        public void onMarkerDragEnd(final Marker marker) {
                            final Dialog d = Helper.common.dialogThemedYesNO(activity, "Move location of client's marker here?", "Change Location", "NO", "YES", R.color.red_material_600);
                            Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                            Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                            d.setCancelable(false);
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.hide();
                                    marker.setDraggable(false);
                                    selectedMarker.setDraggable(false);

                                    hiddenPanel.setVisibility(View.VISIBLE);
                                    marker.showInfoWindow();
                                }
                            });

                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    marker.setPosition(originalPosition[0]);
                                    d.hide();
                                }
                            });


                        }
                    });

//                    final LatLng center = fusedLocation.getLastKnowLocation();
//                    Helper.map.moveCameraAnimate(googleMap, center, 19);
//
//                    final Handler handler1 = new Handler();
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (mapcircle == null || !mapcircle.isVisible()) {
//                                circleOptions_addLocation = Helper.map.addCircle(activity, center, 1, R.color.skyblue_20,
//                                        R.color.skyblue_20, 1000);
//                                mapcircle = googleMap.addCircle(circleOptions_addLocation);
//                            }
//                            btn_closeAddMarker.setVisibility(View.VISIBLE);
//                            Helper.common.dialogThemedOkOnly(activity, "Add Marker", "Long press any location inside the blue.", "OK", R.color.skyblue_500);
//
//                            if (btn_AddMarker.isEnabled()) {
//                                btn_AddMarker.setEnabled(false);
//                            }
//
//                            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//                                @Override
//                                public void onMapLongClick(LatLng latLng) {
//
//                                    final LatLng touchLocation = latLng;
//                                    LatLng center = fusedLocation.getLastKnowLocation();
//
//                                    float[] results = new float[1];
//                                    Location.distanceBetween(center.latitude, center.longitude,
//                                            touchLocation.latitude, touchLocation.longitude, results);
//
//                                    if (results[0] > 1000) {
//                                        final Dialog d = Helper.common.dialogThemedOkOnly(activity, "Out of range", "Selection is out of 1km range from your location", "OK", R.color.red);
//                                        d.show();
//
//                                        Button ok = (Button) d.findViewById(R.id.btn_dialog_okonly_OK);
//                                        ok.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                d.hide();
//                                            }
//                                        });
//                                    } else {
//                                        Intent intent = new Intent(activity, Activity_Add_ClientInfo.class);
//                                        intent.putExtra("userid", Helper.variables.getGlobalVar_currentUserID(activity) + "");
//                                        intent.putExtra("lat", latLng.latitude);
//                                        intent.putExtra("long", latLng.longitude);
//                                        closerAddingMarker();
//                                        startActivityForResult(intent, requestCODE_addMarker);
//                                    }
//
//
//                                }
//                            });
//                        }
//                    }, 1400);

                }
            }
        });



    }



    private void animateBottom(){
        Animation animatelayout;
        String selected;
        int visibility;

        if (isMarkerSelected){
            animatelayout  = AnimationUtils.loadAnimation(context, R.anim.bottom_up);
            animatelayout.setDuration(200);
            visibility = View.VISIBLE;

            btn_AddMarker.setVisibility(View.GONE);

            hiddenPanel.startAnimation(animatelayout);
            hiddenPanel.setVisibility(visibility);

        }else{
            animatelayout  = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
            animatelayout.setDuration(200);

            visibility = View.GONE;
            btn_AddMarker.setVisibility(View.VISIBLE);

            if (hiddenPanel.getVisibility() == View.VISIBLE){
                hiddenPanel.startAnimation(animatelayout);
                hiddenPanel.setVisibility(visibility);
            }

        }





    }


    private void showSycnState() {
        int unpostedcount =  db.getNotPosted_ClientInfo(activity).getCount() + db.getNotPosted_Updates(activity).getCount();
        Log.d("sync", "getting sync coundt: " + unpostedcount);
        if (unpostedcount > 0){
            txtTop.setText("You have (" + unpostedcount + ") unsynced data!");
            txtTop.setVisibility(View.VISIBLE);
            Animation anim = new AlphaAnimation(0.7f, 1.0f);
            anim.setDuration(600); //You can manage the blinking time with this parameter
            anim.setStartOffset(500);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            txtTop.startAnimation(anim);
        }else{
            txtTop.clearAnimation();
            txtTop.setVisibility(View.GONE);
        }
    }

    private void showAllMarker(GoogleMap googleMap) {
        db.open();
        Cursor cur = db.getClientInfoByUserID(Helper.variables.getGlobalVar_currentUserID(activity)+"");
        if (cur.getCount() > 0 ) {
            customerList = new ArrayList<>();
            while (cur.moveToNext()) {
                LatLng latLng = new LatLng(
                        Double.parseDouble(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_LAT))),
                        Double.parseDouble(cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_LNG))));

                String address = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ADDRESS));
                String clientName = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_CLIENT_NAME));
                String id = cur.getString(cur.getColumnIndex(DB_Helper_PetOneCRM.CL_CLIENTINFO_ID));

                googleMap.setInfoWindowAdapter(new CustomerInfoWindow());
                Marker marks = Helper.map.addMarker(googleMap, latLng, R.drawable.ic_pet24, clientName, address, id);



                CustInfoObject singleCustomer = new CustInfoObject();
                singleCustomer.setId(Integer.parseInt(id));
                singleCustomer.setLatLng(latLng);
                singleCustomer.setCustomerName(clientName);

                customerList.add(singleCustomer);
            }

            if (adapterMapsActivity != null) {
                adapterMapsActivity.clear();
            }

            showCustomerList();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requestCODE_addMarker){
            if (data.hasExtra("fromAddClient")){
                mMap.clear();

                showAllMarker(mMap);
                showSycnState();

                String[] latlng = data.getStringArrayExtra("latlng");
                LatLng ltlg = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
                Helper.map.moveCameraAnimate(mMap, ltlg, 18);

            }
        }
    }

    private void closerAddingMarker() {
        if(mapcircle!=null){
            mapcircle.remove();
            mapcircle = null;
        }

        btn_closeAddMarker.setVisibility(View.GONE);
        btn_AddMarker.setEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        txtTop = (TextView) findViewById(R.id.txtTopTextView);
        showSycnState();
        if (Helper.nullcheck.isGlobalUserIDNull(activity)) {
            Dialog d = Helper.common.dialogThemedOkOnly(activity, "Session Expired",
                    "It seems that you have been inactive for too long. Please log in again, ", "OK", R.color.red);
            d.setCancelable(false);
            Button ok = (Button) d.findViewById(R.id.btn_dialog_okonly_OK);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.activityChooser.startActivityClearStack(activity, Activity_LoginScreen.class);
                }
            });
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }



    @Override
    public void onBackPressed() {
        exitApp();
    }



    class CustomerInfoWindow implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        CustomerInfoWindow() {
            myContentsView = getLayoutInflater().inflate(R.layout.infowindow_customer_address, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.address));
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            String splitter = "~";
            String[] splitted = marker.getSnippet().split(splitter);

            tvSnippet.setText(splitted[1]);
            tvTitle.setText( marker.getTitle());

            return myContentsView;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private void showCustomerList(){
        adapterMapsActivity =  new Adapter_MapsActivity(context, R.layout.item_lv_mapsactivity, customerList);
        lvcustomers.setAdapter(adapterMapsActivity);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }



    private void exitApp() {
        final Dialog d = Helper.common.dialogThemedYesNO(activity, "Do you wish to exit the app? You will have to login next time.", "EXIT", "YES", "NO", R.color.red);
        d.show();
        Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
        Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
                finishAffinity();
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
            }
        });
    }
}
