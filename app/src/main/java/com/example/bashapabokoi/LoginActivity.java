
package com.example.bashapabokoi;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {

    private  long backPressedTime;
    private Toast backToast;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    //public FloatingActionButton fb, google, outlook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        /*fb = findViewById(R.id.fab_fb);
        google = findViewById(R.id.fab_google);
        outlook = findViewById(R.id.fab_outlook);*/

        tabLayout.addTab(tabLayout.newTab().setText("Log In"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        /*fb.setTranslationY(300);
        google.setTranslationY(300);
        outlook.setTranslationY(300);
        //tabLayout.setTranslationY(300);


        fb.setAlpha(0f);
        google.setAlpha(0f);
        outlook.setAlpha(0f);
        //tabLayout.setAlpha(0f);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        outlook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();*/
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){

            backToast.cancel();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        } else{

            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
        
    }

    private static class LoginAdapter extends FragmentPagerAdapter {

        int totalTabs;

        public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {

            super(fm);
            this.totalTabs = totalTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position){

            switch (position){

                case 0:
                    return new LoginTabFragment();

                case 1:
                    return new SignupTabFragment();
            }

            return null;
        }

        @Override
        public int getCount(){

            return totalTabs;
        }
    }
}

