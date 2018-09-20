package br.com.bossini.exemplopermissesfateccarapicuibamanha;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private final int REQUEST_PERMISSION_GPS_CODE = 1001;

    private MeuObservadorDeLocalizacao locationObserver
                            = new MeuObservadorDeLocalizacao();
    private LocationManager locationManager;
    private TextView locationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationTextView = findViewById(R.id.locationTextView);
    }

    private class MeuObservadorDeLocalizacao
                implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String exibir =
                    String.format(Locale.getDefault(), "lat: %f, lon: %f", lat, lon);
            locationTextView.setText(exibir);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.
                checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            //pedir permissão
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_GPS_CODE);

        }
        else{
            //aqui já tem permissão
            locationManager.
                    requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationObserver);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_GPS_CODE){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.
                        checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)
                    locationManager.
                        requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationObserver);
            }
            else{
                Toast.makeText(this,
                        getString(R.string.sem_gps_nao_rola), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
