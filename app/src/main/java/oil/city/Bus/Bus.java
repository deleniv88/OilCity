package oil.city.Bus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import io.paperdb.Paper;
import oil.city.Administrations.Buildings;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.Interface.BusItem;
import oil.city.MainActivity;
import oil.city.Model.BusModel;
import oil.city.News.NewsActivity;
import oil.city.R;
import oil.city.Relax.RelaxActivity;
import oil.city.ViewHolder.BusViewHolder;

public class Bus extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<BusModel> bus = new ArrayList<>();
    FirebaseRecyclerAdapter<BusModel, BusViewHolder> adapter;
    Dialog sendDialog;
    FirebaseAuth mAuth; //facebook
    TextView userName, userEmail, userPhone;//for profile view

    SparseBooleanArray expandedState = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        mAuth = FirebaseAuth.getInstance(); //facebook

        recyclerView = findViewById(R.id.lst_bus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        retrieveData();

        setData();

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

    private void setData() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Bus");
        FirebaseRecyclerOptions<BusModel> options = new FirebaseRecyclerOptions.Builder<BusModel>()
                .setQuery(query, BusModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<BusModel, BusViewHolder>(options) {

            @Override
            public int getItemViewType(int position) {
                if (bus.get(position).isExpandable()){
                    return 1;
                }else
                    return 0;
            }

            @Override
            protected void onBindViewHolder(@NonNull BusViewHolder holder, final int position, @NonNull final BusModel model) {
                switch (holder.getItemViewType())
                {
                    case 0://without
                    {
                        BusViewHolder viewHolder = holder;
                        viewHolder.setIsRecyclable(false);
                        viewHolder.txt_bus_item.setText(model.getNameOfBus());

                        viewHolder.setBusItem(new BusItem() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(Bus.this, "Without"+bus.get(position).getNameOfBus(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                    case 1://with
                    {
                        final BusViewHolder viewHolder = holder;
                        viewHolder.setIsRecyclable(false);
                        viewHolder.txt_bus_item.setText(model.getNameOfBus());

                        viewHolder.expandableLinearLayout.setInRecyclerView(true);
                        viewHolder.expandableLinearLayout.setExpanded(expandedState.get(position));
                        viewHolder.expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter() {

                            @Override
                            public void onPreOpen() {
                                changeRotate(viewHolder.button,0f,180f).start();
                                expandedState.put(position,true);
                            }

                            @Override
                            public void onPreClose() {
                                changeRotate(viewHolder.button,180f,0f).start();
                                expandedState.put(position,false);
                            }
                        });
                        viewHolder.button.setRotation(expandedState.get(position)?180f:0f);
                        viewHolder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewHolder.expandableLinearLayout.toggle();
                            }
                        });

                        viewHolder.txt_bus_child_item.setText(model.getTextOneBus());
                        viewHolder.txt_bus_child_item2.setText(model.getTextTwoBus());
                        viewHolder.txt_bus_child_item3.setText(model.getTextThreeBus());
                        viewHolder.txt_bus_child_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Bus.this, ""+viewHolder.txt_bus_item.getText(),Toast.LENGTH_SHORT).show();
                            }
                        });

                        viewHolder.setBusItem(new BusItem() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(Bus.this, ""+model.getNameOfBus(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                    default:
                        break;
                }
            }

            @NonNull
            @Override
            public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bus_without_child,parent,false);
                    return new BusViewHolder(itemView,viewType == 1);//false
                }else {
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bus_with_child,parent,false);
                    return new BusViewHolder(itemView,viewType == 1);
                }
            }
        };

        expandedState.clear();
        for (int i=0;i<bus.size();i++)
            expandedState.append(i,false);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        if (adapter !=null)
            adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (adapter !=null)
            adapter.stopListening();
        super.onStop();
    }

    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void retrieveData() {
        bus.clear();

        DatabaseReference db = FirebaseDatabase.getInstance()
                .getReference()
                .child("Bus");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapShot:dataSnapshot.getChildren()){
                    BusModel buses = itemSnapShot.getValue(BusModel.class);

                    bus.add(buses);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", "" + databaseError.getMessage());
            }
        });
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





