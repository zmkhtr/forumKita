package web.id.azammukhtar.forumkita.UI;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import web.id.azammukhtar.forumkita.Model.User.DataUser;
import web.id.azammukhtar.forumkita.Model.User.User;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

import static web.id.azammukhtar.forumkita.Network.ApiNetwork.BASE_URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final String POST_KEY = "POST_KEY";

    private SessionManager sessionManager;
    private TextView navUsername, navEmail;
    private CircleImageView navImage;
    private FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        fragmentManager.beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddActivity.class)));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.textViewHeaderName);
        navEmail = headerView.findViewById(R.id.textViewHeaderEmail);
        navImage = headerView.findViewById(R.id.imageViewHeaderAvatar);

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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;


        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
            getSupportActionBar().setTitle("ForumKita");
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
        } else if (id == R.id.nav_thread) {
            fragmentClass = ThreadFragment.class;
            getSupportActionBar().setTitle("My Thread");
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
        } else if (id == R.id.nav_logout) {
            sessionManager.setLogin(false, null);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show();
        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserData(){
        ApiInterface apiInterface;
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getUser("Bearer " + sessionManager.getUserToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                DataUser dataUsers = response.body().getDataUser();
                navUsername.setText(dataUsers.getName());
                navEmail.setText(dataUsers.getEmail());
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(BASE_URL + "image/" + dataUsers.getImage())
                        .into(navImage);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();
    }
}
