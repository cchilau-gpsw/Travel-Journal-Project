package com.example.traveljournalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyTrips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        DatabaseInitializer.populateAsync(FavoriteDestinationsRoomDatabase.getDatabase(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTrips.this, ManageTrip.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFirebase();
        addFragment(new TravelDestinationsFragment());
    }


    private void clearLocalDatabase() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FavoriteDestinationsRoomDatabase.getDatabase(MyTrips.this).itemDao().deleteAll();
            }
        });
    }

    private void populateLocalDatabaseAndDisplayFavorites() {
        String currentUserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection(TravelDestinationsFragment.DESTINATIONS_COLLECTION + "_" + currentUserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    FavoriteDestinationsRoomDatabase.getDatabase(MyTrips.this).itemDao().deleteAll();
                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getBoolean("isFavorite") == true) {

                                            FavoriteDestinationsRoomDatabase.getDatabase(MyTrips.this).itemDao().insertItem(new FavoriteDestination(document.getString("season"), document.getString("location"),
                                                    document.getString("imageLocation"), document.getLong("price").intValue(),
                                                    (float) document.getLong("rating")));
                                        }
                                    }
                                    DatabaseInitializer.populateAsync(FavoriteDestinationsRoomDatabase.getDatabase(MyTrips.this));
                                    addFragment(new FavoriteDestinationsFragment());
                                }
                            });

                        }
                    }
                });
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
        getMenuInflater().inflate(R.menu.my_trips, menu);
        TextView userNameHeader = findViewById(R.id.text_view_header_username);
        TextView emailHeader = findViewById(R.id.text_view_header_email);
        String loggedInUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            userNameHeader.setText(loggedInUser);
        }
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEmail != null && !userEmail.isEmpty()) {
            emailHeader.setText(userEmail);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, SignInActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            addFragment(new TravelDestinationsFragment());
        } else if (id == R.id.nav_favourite) {
            populateLocalDatabaseAndDisplayFavorites();

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, fragment)
                .commit();
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
    }
}
