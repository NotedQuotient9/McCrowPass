package com.example.mccrowpass;

import android.Manifest;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mccrowpass.domain.LatLong;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private ImageView arrowImage;

    private LocationService locationService;

    private Location deviceLocation;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check for/request phone location permissions
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // if we have permission start listening for location
                                setUpLocationListener();
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        locationService = new LocationService();
        arrowImage = (ImageView) findViewById(R.id.arrow);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



    }

    private void setUpLocationListener() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            deviceLocation = location;
                        }
                    });
        } catch (SecurityException ex) {
            System.out.println("no permissions");
        }
    }

    // when device orientation changes
    @Override
    public void onSensorChanged(SensorEvent event) {
        // if we have coords for the phone
        if (deviceLocation != null) {

            float azimuth = event.values[0];

            GeomagneticField geoField = new GeomagneticField( Double
                    .valueOf( deviceLocation.getLatitude() ).floatValue(), Double
                    .valueOf( deviceLocation.getLongitude() ).floatValue(),
                    Double.valueOf( deviceLocation.getAltitude() ).floatValue(),
                    System.currentTimeMillis() );

            azimuth -= geoField.getDeclination(); // converts magnetic north into true north

            // get bearing angle to nearest location
            float bearing = locationService.getBearing(
                    new LatLong(deviceLocation.getLatitude(), deviceLocation.getLongitude()));

            // If the bearing is smaller than 0, add 360 to get the rotation clockwise.
            if (bearing < 0) {
                bearing = bearing + 360;
            }

            // adjust angle for phone's orientation
            float direction = bearing - azimuth;

            // If the direction is smaller than 0, add 360 to get the rotation clockwise.
            if (direction < 0) {
                direction = direction + 360;
            }
            arrowImage.setRotation(direction);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // pause sensor if app is paused
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // resume sensor if app is unpaused
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // needed for superclass
    }

}