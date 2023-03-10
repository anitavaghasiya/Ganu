package com.ganak.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Context mContext;
    private PrefManager prefManager;
    private TextView tv_title;
    private TextView tv_no_internet_connection;
    private LinearLayout ll_refresh;
    private ImageView iv_refresh;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        mContext = this;

        // Log.e("pref", "onCreate: " + prefManager.);

        prefManager = new PrefManager (mContext);

     /*   iv_refresh = findViewById(R.id.iv_refresh);
        ll_refresh = findViewById(R.id.ll_refresh);
        tv_no_internet_connection = findViewById(R.id.tv_no_internet_connection);
*/

        // tv_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        getSupportActionBar ().setDisplayShowTitleEnabled (false);
        toolbar.setTitle ("DashBoard");

        drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener (toggle);
        toggle.syncState ();

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);

        FragmentTransaction ft = getSupportFragmentManager ().beginTransaction ();
        ft.replace (R.id.frameContainer, new HomeFragment ());
        ft.commit ();

        navigationView.setCheckedItem (R.id.nav_dashboard);
    }

    /* private void replace(Fragment fragment) {
         if (fragment == null) return;
         FragmentTransaction fragmentTransaction = getSupportFragmentManager ().beginTransaction ();
         fragmentTransaction.replace (R.id.frameContainer, fragment);
         fragmentTransaction.addToBackStack (fragment.toString ());
         fragmentTransaction.commit ();
         if (fragment instanceof HomeFragment)
             clearBackStack ();
     }

     private void clearBackStack() {
         if (getSupportFragmentManager ().getBackStackEntryCount () > 0) {
             FragmentManager.BackStackEntry first = getSupportFragmentManager ().getBackStackEntryAt (0);
             getSupportFragmentManager ().popBackStack (first.getId (), FragmentManager.POP_BACK_STACK_INCLUSIVE);
         }
     }
 */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
        fragmentTransaction.replace (R.id.frameContainer, fragment);
        fragmentTransaction.setTransition (FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit ();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        if (drawer.isDrawerOpen (GravityCompat.START)) {
            drawer.closeDrawer (GravityCompat.START);
        } else {
            super.onBackPressed ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected (item);
    }


    private void removeFragment() {
        FragmentManager fm = getSupportFragmentManager ();
        for (int i = 0; i < fm.getBackStackEntryCount (); ++i) {
            fm.popBackStack ();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId ();
        if (id == R.id.nav_dashboard) {
            removeFragment ();
            toolbar.setTitle ("DashBoard");
            FragmentTransaction ft = getSupportFragmentManager ().beginTransaction ();
            ft.replace (R.id.frameContainer, new HomeFragment ());
            ft.commit ();
            // Handle the camera action
        } else if (id == R.id.nav_edit_profile) {
            removeFragment ();
            Intent intent = new Intent (mContext, EditProfileActivity.class);
            startActivity (intent);
        } else if (id == R.id.nav_report) {
            removeFragment ();
            Intent intent = new Intent (mContext, ReportActivity.class);
            startActivity (intent);
        } else if (id == R.id.nav_add_master) {
            removeFragment ();
            if (prefManager.getPIN ().equals ("")) {
                String Pin = prefManager.getPIN ();
                Log.e ("pin", Pin);
                Intent intent = new Intent (mContext, PinActivity.class);
                startActivity (intent);
            } else {
                Log.e ("pin", "run: ");
                Intent intent = new Intent (mContext, EPinActivity.class);
                startActivity (intent);
            }
           /* Intent intent = new Intent(mContext, MasterGradeActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_add_products) {
            removeFragment ();
            Intent intent = new Intent (mContext, InwardDataActivity.class);
            intent.putExtra ("skip", false);
            startActivity (intent);
        } /*else if (id == R.id.nav_remove_products) {
            toolbar.setTitle("Remove Product");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameContainer, new GradesOutwardFragment());
            ft.commit();
        }*/ else if (id == R.id.nav_favourites) {
            removeFragment ();
            Intent intent = new Intent (mContext, FavoriteInwardProductActivity.class);
            startActivity (intent);

        } else if (id == R.id.nav_outward_favourites) {
            removeFragment ();
            Intent intent = new Intent (mContext, FavoriteOutwardProductActivity.class);
            startActivity (intent);
        } else if (id == R.id.nav_change__password) {
            removeFragment ();
            Intent intent = new Intent (mContext, ChangePasswordActivity.class);
            startActivity (intent);
        } else if (id == R.id.nav_logout) {
            logoutDialog ();
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent (mContext, AboutUsActivity.class);
            startActivity (intent);
        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent (Intent.ACTION_SEND);
                i.setType ("text/plain");
                String appName = mContext.getResources ().getString (R.string.app_name);
                String applink = appName + " - " + "https://play.google.com/store/apps/details?id=" + mContext.getPackageName ();
                i.putExtra (Intent.EXTRA_TEXT, applink);
                startActivity (Intent.createChooser (i, "Share Via"));
            } catch (Exception e) {
                Common.errorLog ("ShareError-HomeActivity", e.getMessage ());
            }
        } else if (id == R.id.nav_rate_us) {
            rateusDialog ();
        } else if (id == R.id.nav_exit) {
            exitDialog ();
        }
        drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

    /* For RateUs DialogBox */
    public void rateusDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder (mContext);
        View mView = getLayoutInflater ().inflate (R.layout.rateus_dialog, null);
        Button btn_Cancel = (Button) mView.findViewById (R.id.btn_Cancel);
        Button btn_Rateus = (Button) mView.findViewById (R.id.btn_Rateus);
        mBuilder.setView (mView);
        mBuilder.setCancelable (false);
        final AlertDialog dialog = mBuilder.create ();
        dialog.show ();
        btn_Cancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dialog.dismiss ();
            }
        });
        btn_Rateus.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dialog.dismiss ();
                try {
                    Intent intent = new Intent (Intent.ACTION_VIEW);
                    intent.setData (Uri.parse ("market://details?id=" + mContext.getPackageName ()));
                    startActivity (intent);
                } catch (Exception e) { //google play app is not installed
                    Intent intent = new Intent (Intent.ACTION_VIEW);
                    intent.setData (Uri.parse ("https://play.google.com/store/apps/details?id=" + mContext.getPackageName ()));
                    startActivity (intent);
                }
            }
        });

    }

    private void exitDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder (mContext);
        builder1.setMessage ("Are you sure you want to Exit ?");
        builder1.setCancelable (false);
        builder1.setNegativeButton ("NO", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss ();
            }
        });
        builder1.setPositiveButton ("EXIT",
                new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss ();
                        finish ();
                        moveTaskToBack (true);
                    }
                });

        AlertDialog alert11 = builder1.create ();
        alert11.show ();
        alert11.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (getResources ().getColor (R.color.light_blue));
        alert11.getButton (AlertDialog.BUTTON_NEGATIVE).setTextColor (getResources ().getColor (R.color.light_blue));
    }

    private void logoutDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder (mContext);
        builder1.setMessage ("Are you sure you want to Logout ?");
        builder1.setCancelable (false);
        builder1.setNegativeButton ("NO", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss ();
            }
        });
        builder1.setPositiveButton ("LOGOUT",
                new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss ();
                        prefManager.userLogout ();
                        Common.showToast (mContext, "Logout Successfully");
                        Intent homeIntent = new Intent (mContext, LoginActivity.class);
                        startActivity (homeIntent);
                        finishAffinity ();
                    }
                });

        AlertDialog alert11 = builder1.create ();
        alert11.show ();
        alert11.getButton (AlertDialog.BUTTON_POSITIVE).setTextColor (Color.BLUE);
        alert11.getButton (AlertDialog.BUTTON_NEGATIVE).setTextColor (Color.BLUE);
    }

    /*@Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
*/
}
