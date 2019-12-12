package com.alexgames.powarclicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChangeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public long coins = 0L;
    public long dollars = 0L;
    public long auto_click = 0L;
    public long cost = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        auto_click = coins_buffer.getLong("auto_click", 0L);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        load();
        updateCource();
        sell();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
        coins_buffer_editor.putLong("coins", coins);

        Date date = new Date();
        long millis = date.getTime();
        coins_buffer_editor.putLong("time", millis);

        coins_buffer_editor.commit();
        coins_buffer_editor.apply();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
        coins_buffer_editor.putLong("coins", coins);

        Date date = new Date();
        long millis = date.getTime();
        coins_buffer_editor.putLong("time", millis);

        coins_buffer_editor.commit();
        coins_buffer_editor.apply();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        auto_click = coins_buffer.getLong("auto_click", 0L);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);

        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        auto_click = coins_buffer.getLong("auto_click", 0L);
    }

    void msgbox(String caption, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeActivity.this);
        builder.setTitle(caption)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    void sell()
    {
        Button buy_coins_button = findViewById(R.id.buy_coins_button);
        Button buy_dollars_button = findViewById(R.id.buy_dollars_button);
        Button sell_all_coins_button = findViewById(R.id.sell_coins);
        Button sell_all_dollars_button = findViewById(R.id.sell_dollars);
        load();
        buy_coins_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText coins_value_edit = findViewById(R.id.coins_value_edit);
                long coins_value = Long.parseLong(String.valueOf(coins_value_edit.getText()));
                long dollars_value = dollars;
                long cm = coins_value / cost;
                if(cm == 0) cm = 1;
                dollars_value -= cm;
                if(dollars_value < 0)
                {
                    msgbox("Внимание!", "Не хватает повар-долларов!");
                } else {
                    dollars = dollars_value;
                    coins += coins_value;
                    SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                    SharedPreferences.Editor editor = coins_buffer.edit();

                    editor.putLong("coins", coins);
                    editor.putLong("dollars", dollars);

                    editor.commit();
                    editor.commit();
                    load();
                }
            }
        });

        buy_dollars_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dollars_value_edit = findViewById(R.id.dollars_value_edit);
                long dollars_value = Long.parseLong(String.valueOf(dollars_value_edit.getText()));
                long coins_value = coins;
                coins_value -= dollars_value * cost;
                if(coins_value < 0)
                {
                    msgbox("Внимание!", "Не хватает повар-коинов!");
                }
                else {
                    dollars += dollars_value;
                    coins = coins_value;
                    SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                    SharedPreferences.Editor editor = coins_buffer.edit();

                    editor.putLong("coins", coins);
                    editor.putLong("dollars", dollars);

                    editor.commit();
                    editor.commit();
                    load();
                }
            }
        });

        sell_all_coins_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long coins_value = coins;
                long dollars_value = coins_value / cost;
                dollars += dollars_value;
                coins = 0L;
                SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                SharedPreferences.Editor editor = coins_buffer.edit();

                editor.putLong("coins", 0L);
                editor.putLong("dollars", dollars);

                editor.commit();
                editor.commit();
                load();
            }
        });

        sell_all_dollars_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long dollars_value = dollars;
                long coins_value = dollars_value * cost;
                dollars_value = 0L;
                dollars = dollars_value;
                coins += coins_value;
                SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
                SharedPreferences.Editor editor = coins_buffer.edit();

                editor.putLong("coins", coins);
                editor.putLong("dollars", dollars);

                editor.commit();
                editor.commit();
                load();
            }
        });

    }

    void load()
    {
        SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
        coins = coins_buffer.getLong("coins", 0L);
        dollars = coins_buffer.getLong("dollars", 0L);
        TextView text = findViewById(R.id.coins);
        text.setText(String.valueOf(coins));
        text = findViewById(R.id.dollars);
        text.setText(String.valueOf(dollars));

        SharedPreferences ch_buffer = getSharedPreferences("change_buff", 0);
        String price_text = ch_buffer.getString("label_price", "Загрузка данных...");
        String stats_text = ch_buffer.getString("label_stats", "Загрузка данных...");
        TextView course = findViewById(R.id.price);
        TextView stats = findViewById(R.id.stats);
        course.setText(price_text);
        stats.setText(stats_text);
        cost = ch_buffer.getLong("cost", 1L);
    }

    void updateCource()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences ch_buffer = getSharedPreferences("change_buff", 0);
                        SharedPreferences.Editor buffer_editor = ch_buffer.edit();
                        long prev_min = ch_buffer.getLong("min", 0L);
                        Date date = new Date();
                        long millis = date.getTime();
                        long current_minutes = millis / (60 * 1000);
                        if(current_minutes != prev_min)
                        {
                            TextView course = findViewById(R.id.price);
                            TextView stats = findViewById(R.id.stats);
                            Random r = new Random();
                            int course_coin = r.nextInt(2);
                            int data = 0;
                            cost = ch_buffer.getLong("cost", 0L);
                            if(course_coin == 0){
                                if(cost < 150) data = r.nextInt(31);
                                else data = r.nextInt(101);
                                stats.setText("Упал в цене на " + String.valueOf(data));
                                cost -= data;
                            }
                            else {
                                data = r.nextInt(31);
                                stats.setText("Поднялся в цене на " + String.valueOf(data));
                                cost += data;
                            }
                            if(cost <= 0) cost = 1;
                            course.setText("Сейчас повар-доллар стоит " + String.valueOf(cost));
                            buffer_editor.putLong("min", current_minutes);
                            buffer_editor.putLong("cost", cost);
                            buffer_editor.putString("label_price", String.valueOf(course.getText()));
                            buffer_editor.putString("label_stats", String.valueOf(stats.getText()));
                            buffer_editor.commit();
                            buffer_editor.commit();
                        }
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
        getMenuInflater().inflate(R.menu.change, menu);
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
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(ChangeActivity.this, InfoActivity.class);
            startActivity(intent);

            ChangeActivity.this.finish();

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

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(ChangeActivity.this, MainActivity.class);
            startActivity(intent);

            ChangeActivity.this.finish();
        } else if (id == R.id.nav_change) {

        } else if (id == R.id.nav_payment) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(ChangeActivity.this, PaymentActivity.class);
            startActivity(intent);

            ChangeActivity.this.finish();

        } else if (id == R.id.nav_shop) {

            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(ChangeActivity.this, ShopActivity.class);
            startActivity(intent);

            ChangeActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            SharedPreferences coins_buffer = getSharedPreferences("cbuffer", 0);
            SharedPreferences.Editor coins_buffer_editor = coins_buffer.edit();
            coins_buffer_editor.putLong("coins", coins);
            coins_buffer_editor.putLong("dollars", dollars);
            coins_buffer_editor.commit();
            coins_buffer_editor.commit();
            Intent intent = new Intent(ChangeActivity.this, SettingActivity.class);
            startActivity(intent);

            ChangeActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
