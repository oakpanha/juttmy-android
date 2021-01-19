package com.juttmy.chatapp.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.b44t.messenger.DcContext;
import com.b44t.messenger.DcMsg;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juttmy.chatapp.R;
import com.juttmy.chatapp.connect.DcHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener, SendingTask.OnMessageSentListener{

  private GoogleMap mMap;
  private AlertDialog dialogbox;

  public static final String CHAT_ID = "chat_id";
  public static final String MOD_LOCATION = "set_mod_location";
  public static final String SET_COORDINATE = "set_coordinate";

  public static final int PICK_LOCATION = 1;
  public static final int SHOW_LOCATION = 2;

  private int chatId;
  private int location_mod;

  private LocationManager locationManager;
  private Button sendMyLocation;
  private LatLng coordinate;

  private void markCurrentLocation(){
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    String mprovider = locationManager.getBestProvider(criteria, false);

    if (mprovider != null && !mprovider.equals("")) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
      }

      Location location = locationManager.getLastKnownLocation(mprovider);
      locationManager.requestLocationUpdates(mprovider, 15000, 10, this);

      if (location != null) onLocationChanged(location);
      else
        Toast.makeText(getBaseContext(), R.string.no_location_provider, Toast.LENGTH_SHORT).show();
    }
  }

  private void setNewMarker(Context context, LatLng latLng, boolean zoom){
    mMap.clear();
    mMap.addMarker(new MarkerOptions().position(latLng).title(context.getString(R.string.my_location)));
    if(zoom) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
  }

  @Override
  public void onMessageSent() {
    if(dialogbox != null) dialogbox.dismiss();
      finish();
  }

  private class myLocationClick implements View.OnClickListener{
    @Override
    public void onClick(View view) {
      markCurrentLocation();
    }
  }

  private class sendLocationClick implements View.OnClickListener{
    @Override
    public void onClick(View view) {
      if(coordinate == null){
        Toast.makeText(MapsActivity.this, R.string.map_market_no_found, Toast.LENGTH_SHORT).show();
        return;
      }

      dialogbox = new AlertDialog.Builder(MapsActivity.this)
        .setTitle(R.string.title_activity_maps)
        .setMessage(R.string.map_sending)
        .show();

      //--here we send map
      DcContext dcContext = DcHelper.getContext(MapsActivity.this);
      DcMsg msg = new DcMsg(dcContext, DcMsg.DC_MSG_TEXT);
      msg.setLocation((float) coordinate.latitude , (float) coordinate.longitude);
      String send_text = MapsActivity.this.getString(R.string.my_location) +
        "\n\nLatitude:" + coordinate.latitude +
        "\nLongitude:" + coordinate.longitude +
        "\n\n[CLICK HERE]";
      msg.setText(send_text);
      SendingTask.Model model = new SendingTask.Model(msg, chatId, MapsActivity.this);
      new SendingTask(MapsActivity.this).execute(model);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    ImageButton mylocationButton = (ImageButton) findViewById(R.id.imgMyLocation);
    sendMyLocation = (Button) findViewById(R.id.send_my_location);
    mylocationButton.setOnClickListener(new myLocationClick());
    sendMyLocation.setOnClickListener(new sendLocationClick());

    chatId = getIntent().getIntExtra(CHAT_ID, -1);
    location_mod = getIntent().getIntExtra(MOD_LOCATION, -1);

    if (chatId == -1 || location_mod == -1) {
      finish();
      return;
    }

    if(location_mod == SHOW_LOCATION){
      double[] codi = getIntent().getDoubleArrayExtra(SET_COORDINATE);
      coordinate = new LatLng(codi[0], codi[1]);
    }
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    //mMap.setMyLocationEnabled(true);
    mMap.getUiSettings().setMyLocationButtonEnabled(true);
    mMap.getUiSettings().setZoomControlsEnabled(true);
    mMap.setOnMapLongClickListener(this);
    mMap.setOnMyLocationButtonClickListener(this);

    //--when mod is SHOW_LOCATION
    if(location_mod == SHOW_LOCATION){
      sendMyLocation.setVisibility(View.GONE);
      setNewMarker(this, coordinate, true);
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    coordinate = latLng;
    setNewMarker(this, latLng, true);
    //--this line to disable real-time LagLng
    locationManager.removeUpdates(this);
  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {
  }

  @Override
  public void onProviderEnabled(String s) {
  }

  @Override
  public void onProviderDisabled(String s) {
  }

  @Override
  public void onMapLongClick(LatLng latLng) {
    coordinate = latLng;
    setNewMarker(this, latLng, false);
  }

  @Override
  public boolean onMyLocationButtonClick() {
    markCurrentLocation();
    return false;
  }
}
