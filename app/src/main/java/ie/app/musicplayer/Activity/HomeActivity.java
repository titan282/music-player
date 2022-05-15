package ie.app.musicplayer.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.material.tabs.TabLayout;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Adapter.ViewPagerAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.R;

public class HomeActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageButton ibSearchBtn;
    private ImageView ivSort;
    private DBManager dbManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(HomeActivity.this);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        ivSort = findViewById(R.id.sortBtn);
        ibSearchBtn=findViewById(R.id.search_btn);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText("Songs");
        mTabLayout.getTabAt(1).setText("Playlists");
        mTabLayout.getTabAt(2).setText("Albums");
        mTabLayout.getTabAt(3).setText("Artists");
        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.sort, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}