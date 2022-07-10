package com.lux.bookstore_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager=getSupportFragmentManager();

        fragments.add(new ListFragment());
        fragments.add(null);
        fragments.add(null);

        fragmentManager.beginTransaction().add(R.id.container,fragments.get(0)).commit();

        bottomNavigationView=findViewById(R.id.bnv);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction=fragmentManager.beginTransaction();

                if(fragments.get(0)!=null) transaction.hide(fragments.get(0));
                if (fragments.get(1)!=null) transaction.hide(fragments.get(1));
                if (fragments.get(2)!=null) transaction.hide(fragments.get(2));

                switch (item.getItemId()){
                    case R.id.menu_list:
                        transaction.show(fragments.get(0));
                        break;
                    case R.id.menu_location:
                        if (fragments.get(1)==null){
                            fragments.set(1,new LocationFragment());
                            transaction.add(R.id.container, fragments.get(1));
                        }
                        transaction.show(fragments.get(1));
                        break;
                    case R.id.menu_favorite:
                        if (fragments.get(2)==null){
                            fragments.set(2,new FavoriteFragment());
                            transaction.add(R.id.container, fragments.get(2));
                        }
                        transaction.show(fragments.get(2));
                        break;
                }
                transaction.commit();

                return true;
            }
        });

        SQLiteDatabase db=openOrCreateDatabase("bookstore",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS favor(num integer PRIMARY KEY AUTOINCREMENT,title text,storeid text)");



    }
}