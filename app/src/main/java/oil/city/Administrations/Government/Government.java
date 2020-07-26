package oil.city.Administrations.Government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import oil.city.Administrations.Buildings;
import oil.city.Bus.Bus;
import oil.city.Common.Common;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.Interface.ItemClickListener;
import oil.city.MainActivity;
import oil.city.Model.AdministrationModel;
import oil.city.News.NewsActivity;
import oil.city.R;
import oil.city.Relax.RelaxActivity;
import oil.city.ViewHolder.AdministrationViewHolder;

public class Government extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference adminList;

    String adminId = "";

    FirebaseRecyclerAdapter<AdministrationModel, AdministrationViewHolder> adapter;

    Dialog sendDialog;
    FirebaseAuth mAuth; //facebook
    TextView userName, userEmail, userPhone;//for profile view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government);

        database = FirebaseDatabase.getInstance();
        adminList = database.getReference("Admin");
        mAuth = FirebaseAuth.getInstance(); //facebook

        recyclerView = findViewById(R.id.recycler_medenychi);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext()))
            loadAdmin(adminId);
        else {
            Toast.makeText(Government.this, "Немає з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
            return ;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();

            View header = navigationView.getHeaderView(0);
            userPhone = header.findViewById(R.id.userPhone);
//        userPhone.setText(phone1);
            userEmail = header.findViewById(R.id.userEmail);
            userEmail.setText(email);
            userName = header.findViewById(R.id.userName);
            userName.setText(name);
        }

    }

    private void loadAdmin(String adminId) {
        FirebaseRecyclerOptions<AdministrationModel> adminOptions = new FirebaseRecyclerOptions.Builder<AdministrationModel>()
                .setQuery(adminList, AdministrationModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<AdministrationModel, AdministrationViewHolder>(adminOptions) {
            @NonNull
            @Override
            public AdministrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.administration_layout, parent, false);
                return new AdministrationViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final AdministrationViewHolder holder, final int position, @NonNull final AdministrationModel model) {
                holder.name.setText(model.getName());
//                holder.time.setText(model.getTime());
////              holder.adress.setText(model.getAdress());
                holder.phone.setText(model.getPhone());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.admin_image);

                final AdministrationModel local = model;

                holder.setListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent event = new Intent(Government.this, GovernmentDetail.class);
                        event.putExtra("AdminId", adapter.getRef(position).getKey());
                        startActivity(event);
                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_sign_in){
            if (mAuth.getCurrentUser() == null) {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            }else {
                Toast.makeText(this, "Ви вже увійшли", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.nav_home){
            Intent menu = new Intent(this, Home.class);
            startActivity(menu);
        }

        if (id == R.id.nav_admin){
            Intent admin = new Intent(this, Buildings.class);
            startActivity(admin);
        }

        if (id == R.id.nav_news){
            Intent news = new Intent(this, NewsActivity.class);
            startActivity(news);
        }

        if (id == R.id.nav_events){
            Intent events= new Intent(this, EventActivity.class);
            startActivity(events);
        }

        if (id == R.id.nav_relax){
            Intent relax = new Intent(this, RelaxActivity.class);
            startActivity(relax);
        }

        if (id == R.id.nav_bus){
            Intent bus = new Intent(this, Bus.class);
            startActivity(bus);
        }

        if (id == R.id.nav_flat){
            Intent flat = new Intent(this, Flat.class);
            startActivity(flat);
        }


        if (id == R.id.nav_share){ //share button
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=oil.city";
            String shareOb = "Oil City Boryslav";

            share.putExtra(Intent.EXTRA_TEXT, shareBody);
            share.putExtra(Intent.EXTRA_SUBJECT, shareOb);

            startActivity(Intent.createChooser(share, "Share Using"));

        }

        if (id == R.id.nav_send){
            sendDialog = new Dialog(this);
            showPopMenu();
        }

        if (id == R.id.nav_exit) {

            if (mAuth.getCurrentUser() != null) {
                Paper.book().destroy();

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//            com.facebook.login.LoginManager.getInstance().logOut();

                mAuth.signOut();
                sendToLogin();
            } else {
                Toast.makeText(this, "Ви не зареєстровані", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPopMenu() {
        final EditText etTo, etSubject, etMessage;
        Button btnSend;
        sendDialog.setContentView(R.layout.activity_email);
        etMessage = sendDialog.findViewById(R.id.et_message);
        etSubject = sendDialog.findViewById(R.id.et_subject);
        etTo = sendDialog.findViewById(R.id.et_to);
        btnSend = sendDialog.findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + etTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                startActivity(intent);
            }
        });
        sendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendDialog.show();
    }

    private void sendToLogin() {//facebook
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
