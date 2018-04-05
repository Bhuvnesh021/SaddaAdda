package com.sadda.adda.panchratan.saddaadda.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.fragment.AddContentFragement;
import com.sadda.adda.panchratan.saddaadda.fragment.CommentPostFragment;
import com.sadda.adda.panchratan.saddaadda.fragment.HomeFragment;
import com.sadda.adda.panchratan.saddaadda.fragment.ShowAllCommentFragment;
import com.sadda.adda.panchratan.saddaadda.fragment.ShowStatisticsFragment;
import com.sadda.adda.panchratan.saddaadda.util.CircleTransform;
import com.sadda.adda.panchratan.saddaadda.util.Constants;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

/**
 * Created by user on 03-07-2017.
 */
public class SaddaAddaLauncherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LauncherActivity";
    Toolbar toolbar;
    Handler mHandler;
    DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private TextView txtName, txtUserName;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    private ImageView imgProfile;
    private static int statusColor;
    FrameLayout frameLayout;
    float lastTranslate = 0.0f;
    private boolean isFirstClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saddaadda_launcher);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusColor = getWindow().getStatusBarColor();
        }
        mHandler = new Handler();
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (drawer.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    frameLayout.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    frameLayout.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(statusColor);
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navHeader =
                navigationView.inflateHeaderView(R.layout.nav_header_layout);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtUserName = (TextView) navHeader.findViewById(R.id.website);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
        toolbar.setTitle("Home");
        loadNavHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isFirstClicked) {
            if (drawer.isDrawerOpen(Gravity.LEFT)){
                drawer.closeDrawer(Gravity.LEFT);
            }else {
                drawer.openDrawer(Gravity.LEFT);
            }
            isFirstClicked = false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isFirstClicked = true;
                }
            }.start();
        } else {
            Utils.showDialod(this,"Warning","Are you sure you want to Exit from this app.","Yes","No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            drawer.closeDrawer(Gravity.LEFT);
        }
    }

    private void loadNavHeader() {
        // name, website
        SharedPreferences sharedPreferences = getSharedPreferences("userCredentials", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String userName = sharedPreferences.getString("user_name",null);
        String imageURL = sharedPreferences.getString("image_url",null);
        Log.i(TAG, "loadNavHeader: imageURL: "+imageURL);
        String formatedString = String.valueOf(Html.fromHtml("<b>" + name + "</B>"));
        txtName.setText(formatedString);
        txtUserName.setText(userName);
        // Loading profile image
        if(imageURL == null){
            imageURL = Constants.BHUVNESH_IMAGE_URL;
        }
        Glide.with(this).load(imageURL)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add_content:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new AddContentFragement()).commit();
                toolbar.setTitle("Add Items");
                drawer.closeDrawers();
                break;
            case R.id.post_comment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new CommentPostFragment()).commit();
                toolbar.setTitle("Post Comment");
                drawer.closeDrawers();
                break;
            case R.id.logout_action:
                Utils.showDialod(this,"Warning","Are you sure you want to logout from this app","Yes","Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences(getString(R.string.stay_signed_in), MODE_PRIVATE).edit()
                                .putBoolean(getString(R.string.isStaySignedInChecked), false)
                                .commit();
                        finish();
                    }
                });
                break;
            case R.id.show_all_comment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ShowAllCommentFragment()).commit();
                toolbar.setTitle("Show all Comments");
                drawer.closeDrawers();
                break;
            case R.id.home_fragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
                toolbar.setTitle("Home");
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.show_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ShowStatisticsFragment()).commit();
                toolbar.setTitle("Show Statistics");
                drawer.closeDrawer(Gravity.LEFT);
                break;
        }
        return false;
    }
}
