package com.alexgames.powarclicker;

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
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Сообщить о багах: alexgamesby@gmail.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String text = "Помощь:\n" +
                "\n" +
                "Описание разделов:\n" +
                "1. Банк: тут можно кликать и майнить повар-коины. \n" +
                "Затем переводить их в повар-доллары для дальнейших транзакций\n" +
                "2. Биржа: обмен валют для дальнейших действий\n" +
                "3. Транзакции: переводы повар-долларов между устройствами\n" +
                "4. Настройки: настройки звука и \"настройки разработчика\"\n" +
                "\n" +
                "Подробное описание раздела \"транзакции\"\n" +
                "\n" +
                "Если вы ПРИНИМАЕТЕ повар-доллары:\n" +
                "1. Нажмите кнопку \"Я принимаю\"\n" +
                "2. Ждите\n" +
                "\n" +
                "Если вы ОТПРАВЛЯЕТЕ повар-доллары:\n" +
                "1. Введите сумму перевода и IP адрес получателя \n" +
                "(отображается при нажатии кнопки \"Я принимаю\")\n" +
                "2. Нажмите кнопку \"Отправить\"\n" +
                "3. Ждите подтверждения";
        TextView textView = findViewById(R.id.text_info);
        textView.setText(text);
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
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
            startActivity(intent);

            InfoActivity.this.finish();
        } else if (id == R.id.nav_change) {
            Intent intent = new Intent(InfoActivity.this, ChangeActivity.class);
            startActivity(intent);

            InfoActivity.this.finish();
        } else if (id == R.id.nav_payment) {

            Intent intent = new Intent(InfoActivity.this, PaymentActivity.class);
            startActivity(intent);

            InfoActivity.this.finish();

        } else if (id == R.id.nav_shop) {
            Intent intent = new Intent(InfoActivity.this, ShopActivity.class);
            startActivity(intent);

            InfoActivity.this.finish();
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(InfoActivity.this, SettingActivity.class);
            startActivity(intent);

            InfoActivity.this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
