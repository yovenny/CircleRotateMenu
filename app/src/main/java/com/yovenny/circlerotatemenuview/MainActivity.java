package com.yovenny.circlerotatemenuview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yovenny.library.CircleRotateView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        TextView tv = new TextView(MainActivity.this);
        tv.setText("999");
        tv.setTextSize(18);
        tv.setTextColor(Color.WHITE);



        cvMainCircle = (CircleRotateView) findViewById(R.id.cv_main_circle);
        cvMainCircle.setClickable(true);
        cvMainCircle.setFocusable(true);
        cvMainCircle.setMenuResource(sBtnArray)
                .setMenuClickListener(sClickArray)
                .setCircleHandleView(tv)
                //min write
                .setCircleBgResource(R.drawable.main_circle)
                .setMenuHandleStartDegree(90)
                .setMenuIntervalDegree(30)
                .setRotateHandleStartDegree(-43)
                .setMenuItemWidth(120)

                .setRotateDegree(180,-180)
                .setOnRotateListener(new CircleRotateView.OnRotateListener() {
                    @Override
                    public void onRotateMin() {
                        Toast.makeText(MainActivity.this, "min", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRotateMax() {
                        Toast.makeText(MainActivity.this, "max", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRotate(float degree) {

                    }
                });

    }

    CircleRotateView cvMainCircle;


    private View.OnClickListener[] sClickArray = new View.OnClickListener[]{
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cvMainCircle.toggleMenu();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click2", Toast.LENGTH_SHORT).show();
                }
            }
            ,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click3", Toast.LENGTH_SHORT).show();
                }
            }
            ,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click4", Toast.LENGTH_SHORT).show();
                }
            }
            ,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click5", Toast.LENGTH_SHORT).show();
                }
            }
            ,
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "click6", Toast.LENGTH_SHORT).show();
                }
            }

    };


    private static int[] sBtnArray = new int[]{R.drawable.btn_more_selector
            , R.drawable.btn_graph_selector
            , R.drawable.btn_timer_selector
            , R.drawable.btn_power_selector
            , R.drawable.btn_hot_selector
            ,R.drawable.btn_cold_selector
    };


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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
