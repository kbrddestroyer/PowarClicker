package com.alexgames.powarclicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public long coins = 0L;
    public long dollars = 0L;
    public long auto_click = 0L;
    public long click = 1L;
    SoundPool soundPool;
    int soundID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        click = coins_buffer.getLong("click", 1L);
        auto_click = coins_buffer.getLong("auto_click", 0L);

        TextView click_info = findViewById(R.id.click_info);
        TextView autoclick_info = findViewById(R.id.autoclick_info);
        click_info.setText("Клик: " + String.valueOf(click));
        autoclick_info.setText("Коин/сек: " + String.valueOf(auto_click));

        TextView text = findViewById(R.id.coins_value_clicker);
        text.setText(String.valueOf(coins));
        text = findViewById(R.id.coins);
        text.setText(String.valueOf(coins));
        text = findViewById(R.id.dollars);
        text.setText(String.valueOf(dollars));

        click_listener();
        mining();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1); //Инициализируем SoundPool
        soundID = soundPool.load(MainActivity.this, R.raw.coin, 1); //Загружаем мелодию
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();


        Date date = new Date();
        long millis = date.getTime();
        coins_buffer_editor.putLong("time", millis);
        coins_buffer_editor.putLong("coins", coins);
        coins_buffer_editor.commit();
        coins_buffer_editor.commit();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();


        Date date = new Date();
        long millis = date.getTime();
        coins_buffer_editor.putLong("time", millis);
        coins_buffer_editor.putLong("coins", coins);
        coins_buffer_editor.commit();
        coins_buffer_editor.commit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        click = coins_buffer.getLong("click", 1L);
        auto_click = coins_buffer.getLong("auto_click", 0L);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        click = coins_buffer.getLong("click", 1L);
        auto_click = coins_buffer.getLong("auto_click", 0L);
    }


    void playSound()
    {
        SharedPreferences setting_buffer = getSharedPreferences("settings", 0);
        boolean isChecked = setting_buffer.getBoolean("sounds", true);
        if(isChecked) {
            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            float curVolume = 100;
            float maxVolume = 100;
            float leftVolume = curVolume / maxVolume;
            float rightVolume = curVolume / maxVolume;
            soundPool.play(soundID, leftVolume, rightVolume, 1, 0, 1); //Запускаем мелодию
            soundPool.stop(soundID);
        }
    }

    void click_listener()
    {
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
        coins_buffer_editor.putLong("coins", coins);
        coins_buffer_editor.commit();
        coins_buffer_editor.commit();
        ImageButton coin = findViewById(R.id.coin_button);
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClick();
            }
        });
        coin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton button = findViewById(R.id.coin_button);
                if (event.getAction()==MotionEvent.ACTION_DOWN)
                    button.setImageResource(R.mipmap.powarcoinsmall);
                else if (event.getAction()==MotionEvent.ACTION_UP)
                    button.setImageResource(R.mipmap.powarcoinbig);
                return false;
            }
        });
    }

    void performClick()
    {
        coins += click;
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
        coins_buffer_editor.putLong("coins", coins);
        coins_buffer_editor.commit();
        coins_buffer_editor.commit();
        TextView text = findViewById(R.id.coins_value_clicker);
        text.setText(String.valueOf(coins));
        text = findViewById(R.id.coins);
        text.setText(String.valueOf(coins));
        playSound();
    }

    void mining()
    {
        Date date = new Date();
        long millis = date.getTime();
        final SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        final SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
        coins = coins_buffer.getLong("coins", 0L);
        long prev_min = coins_buffer.getLong("time", 0L);
        if(auto_click != 0) {
            coins_buffer_editor.putLong("time", millis);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
        }
        if(prev_min != 0L && auto_click != 0)
        {
            long stime = millis - prev_min;
            long coins_value = stime * auto_click / 1000;
            coins += coins_value;
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.commit();
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coins += auto_click;
                        Date date = new Date();
                        long millis = date.getTime();
                        TextView text = findViewById(R.id.coins_value_clicker);
                        text.setText(String.valueOf(coins));
                        text = findViewById(R.id.coins);
                        text.setText(String.valueOf(coins));
                    }
                });
            }
        },1000, 1000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);

            Date date = new Date();
            long millis = date.getTime();
            coins_buffer_editor.putLong("time", millis);

            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);

            MainActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bank) {

        } else if (id == R.id.nav_change) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);

            Date date = new Date();
            long millis = date.getTime();
            coins_buffer_editor.putLong("time", millis);

            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(MainActivity.this, ChangeActivity.class);
            startActivity(intent);

            MainActivity.this.finish();
        } else if (id == R.id.nav_payment) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);

            Date date = new Date();
            long millis = date.getTime();
            coins_buffer_editor.putLong("time", millis);

            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);

            MainActivity.this.finish();
        } else if (id == R.id.nav_shop) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);

            Date date = new Date();
            long millis = date.getTime();
            coins_buffer_editor.putLong("time", millis);

            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(MainActivity.this, ShopActivity.class);
            startActivity(intent);

            MainActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);

            Date date = new Date();
            long millis = date.getTime();
            coins_buffer_editor.putLong("time", millis);

            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
