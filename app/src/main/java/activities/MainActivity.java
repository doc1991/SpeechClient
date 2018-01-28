package activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bill.Activities.R;

import activities.permission.PermissionActivity;
import activities.settings.SettingsActivity;
import services.Maestro;
import services.STT;
import services.TTS;
import services.WitResponse;
import utils.Constants;

/**
 * Created by bill on 11/20/17.
 */

@SuppressLint("Registered")
public class MainActivity extends PermissionActivity implements NavigationView.OnNavigationItemSelectedListener {

    // private ResponseReceiver receiver;
    private static final String TAG = "Broadcast";
    SharedPreferences sharedPref;
    private TextView response;
    private ToggleButton btnIput;
    private ProgressBar progressBar;
    private ProgressBar WaitAction;
    private Toolbar toolbar;
    private boolean exit, assistantBound;
    Maestro mService;
    boolean mBound = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        exit = sharedPref.getBoolean("exit", false);
        if (Build.VERSION.SDK_INT < 23) Init();

    }

    private void Init() {

        setButtons();
        setProgress();
        setText();
        setToolbar();
        setDrawerLayout();
        setNavigation();

    }

    //Activity Lifecycle
    @Override
    protected void onStart() {
        super.onStart();

        //Bind to Services
        Intent intent = new Intent(this,Maestro.class);
        bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    //Button Endpoints
    public void onBtnClick(View view){
        Intent btnint = new Intent(Constants.MaestroComm);
        btnint.putExtra("Sender","BTN");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(btnint);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;


        switch (item.getItemId()) {
            case R.id.action_settings:
                if (exit) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("exit", false);
                    editor.apply();
                    exit = false;
                    Toast.makeText(this, "Θα τερματίσει στη έξοδο", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Θα είναι πάντα ενεργό", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("exit", true);
                    editor.apply();
                    exit = true;
                }
                invalidateOptionsMenu();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        if (exit) {
            menu.findItem(R.id.action_settings)
                    .setIcon(R.mipmap.power_off);
        } else {
            menu.findItem(R.id.action_settings)
                    .setIcon(R.mipmap.power_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_action) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //set gui functions
    private void setNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setDrawerLayout() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setButtons() {
    }

    private void setProgress() {
    }

    private void setText() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            Init();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Maestro.LocalBinder binder = (Maestro.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
