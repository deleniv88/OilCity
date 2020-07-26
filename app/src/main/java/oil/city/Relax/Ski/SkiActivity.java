package oil.city.Relax.Ski;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.paperdb.Paper;
import me.anwarshahriar.calligrapher.Calligrapher;
import oil.city.Administrations.Buildings;
import oil.city.Bus.Bus;
import oil.city.Common.Common;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.Interface.ItemClickListener;
import oil.city.MainActivity;
import oil.city.Model.Restoraunt;
import oil.city.Model.SkiModel;
import oil.city.News.NewsActivity;
import oil.city.R;
import oil.city.Relax.RelaxActivity;
import oil.city.ViewHolder.RestorauntViewHolder;
import oil.city.ViewHolder.SkiViewHolder;

public class SkiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference skiList;

    String skiId = "";

    FirebaseRecyclerAdapter<SkiModel, SkiViewHolder> adapter;
    Dialog sendDialog;
    FirebaseAuth mAuth; //facebook
    TextView userName, userEmail, userPhone;//for profile view

    //facebook share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ski);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "blogger.otf",true);

        database = FirebaseDatabase.getInstance();
        skiList = database.getReference("Ski");
        mAuth = FirebaseAuth.getInstance(); //facebook

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        recyclerView = findViewById(R.id.recycler_event);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext()))
            loadEvent();
        else {
            Toast.makeText(SkiActivity.this, "Немає з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
            return ;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);//бокове меню
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

    private void loadEvent() {
        FirebaseRecyclerOptions<SkiModel> restOptions = new FirebaseRecyclerOptions.Builder<SkiModel>()
                .setQuery(skiList, SkiModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<SkiModel, SkiViewHolder>(restOptions) {
            @NonNull
            @Override
            public SkiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ski_item, parent, false);
                return new SkiViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final SkiViewHolder holder, final int position, @NonNull final SkiModel model) {
                holder.nameOfSki.setText(model.getName());
                holder.textOfSki.setText(model.getTextOfRest());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.imageOfSki);

                final SkiModel local = model;

                holder.setListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent event = new Intent(SkiActivity.this, SkiDetail.class);
                        event.putExtra("SkiId", adapter.getRef(position).getKey());
                        startActivity(event);
                    }
                });

//                holder.imageShareRest.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(Intent.ACTION_SEND);
//                        i.setType("text/plan");
//                        i.putExtra(Intent.EXTRA_SUBJECT, model.getImage());
//                        i.putExtra(Intent.EXTRA_TITLE, model.getName());
//                        i.putExtra(Intent.EXTRA_TEXT,model.getTextOfRest());
//                        startActivity(Intent.createChooser(i,""));
//                        Picasso.with(getApplicationContext())
//                                .load(model.getImage())
//                                .into(target);
//                    }
//                });
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
    public void onBackPressed() {//бокове меню
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






