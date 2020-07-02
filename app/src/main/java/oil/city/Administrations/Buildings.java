package oil.city.Administrations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import oil.city.Administrations.Company.Company;
import oil.city.Administrations.Culture.Culture;
import oil.city.Administrations.Education.EducationDNZ;
import oil.city.Administrations.Education.EducationOther;
import oil.city.Administrations.Education.EducationProf;
import oil.city.Administrations.Education.EducationSchool;
import oil.city.Administrations.Government.Government;
import oil.city.Administrations.Medicine.Doctors;
import oil.city.Administrations.Medicine.HospitalDetail;
import oil.city.Administrations.Sport.SportAdmins;
import oil.city.Bus.Bus;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.MainActivity;
import oil.city.News.NewsActivity;
import oil.city.News.NewsDetail;
import oil.city.R;
import oil.city.Relax.RelaxActivity;
import oil.city.WebView;

public class Buildings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    ScrollView scrollBuild;
    TextView txt_education_child, txt_education_child2, txt_education_child3,txt_education_child4;
    TextView txt_medicine_online,txt_medicine_doctors;

    RelativeLayout buil_government, bild_education, bild_medicine, bild_sport,bild_culture,bild_company;

    RelativeLayout expand,expandMedicine,layout_doctors,layout_hospital;
    Button buttonMedecine,buttonEducation;

    FirebaseAuth mAuth; //facebook
    TextView userName, userEmail, userPhone;//for profile view
    Dialog sendDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings);

        mAuth = FirebaseAuth.getInstance(); //facebook

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();

        final FirebaseUser user = mAuth.getCurrentUser();
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

        expand = findViewById(R.id.expand);
        expandMedicine = findViewById(R.id.expandMedicine);


        buttonMedecine = findViewById(R.id.buttonMedicine);
        buttonEducation = findViewById(R.id.buttonEducation);

        layout_doctors = findViewById(R.id.layout_doctors);
        layout_hospital = findViewById(R.id.layout_hospital);

        buil_government = findViewById(R.id.buil_government);
        bild_education = findViewById(R.id.bild_education);
        bild_medicine = findViewById(R.id.bild_medicine);
        bild_sport = findViewById(R.id.bild_sport);
        bild_culture = findViewById(R.id.bild_culture);
        bild_company = findViewById(R.id.bild_company);

        scrollBuild = findViewById(R.id.scrollBuild);
        //education
        txt_education_child = findViewById(R.id.txt_education_child);
        txt_education_child2 = findViewById(R.id.txt_education_child2);
        txt_education_child3 = findViewById(R.id.txt_education_child3);
        txt_education_child4 = findViewById(R.id.txt_education_child4);
        //medicine
        txt_medicine_online = findViewById(R.id.txt_medicine_online);
        txt_medicine_doctors = findViewById(R.id.txt_medicine_doctors);
        //sport



        buil_government.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, Government.class);
                startActivity(intent);
            }
        });

        buttonEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(bild_education, new AutoTransition());
                    expand.setVisibility(View.VISIBLE);
                    buttonEducation.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                }else {
                    TransitionManager.beginDelayedTransition(bild_education, new AutoTransition());
                    expand.setVisibility(View.GONE);
                    buttonEducation.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        txt_education_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, EducationSchool.class);
                startActivity(intent);
            }
        });
        txt_education_child2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, EducationDNZ.class);
                startActivity(intent);
            }
        });

        txt_education_child3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, EducationProf.class);
                startActivity(intent);
            }
        });

        txt_education_child4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, EducationOther.class);
                startActivity(intent);
            }
        });
        //medicine
        buttonMedecine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandMedicine.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(bild_medicine, new AutoTransition());
                    expandMedicine.setVisibility(View.VISIBLE);
                    buttonMedecine.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                }else {
                    TransitionManager.beginDelayedTransition(bild_medicine, new AutoTransition());
                    expandMedicine.setVisibility(View.GONE);
                    buttonMedecine.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });

        txt_medicine_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, WebView.class);
                startActivity(intent);
            }
        });

        layout_doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, Doctors.class);
                startActivity(intent);
            }
        });

        layout_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, HospitalDetail.class);
                startActivity(intent);
            }
        });

        //sport
        bild_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, SportAdmins.class);
                startActivity(intent);
            }
        });
        bild_culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, Culture.class);
                startActivity(intent);
            }
        });
        bild_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildings.this, Company.class);
                startActivity(intent);
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

            Paper.book().destroy();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

//            com.facebook.login.LoginManager.getInstance().logOut();

            mAuth.signOut();
            sendToLogin();
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


