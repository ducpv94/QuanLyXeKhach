package vn.itplus.quanlyxekhach.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.fragment.FragmentDrawer;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.txt_hello)
    TextView txtHello;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private FragmentDrawer drawer;
    private String drawerTitles[] = null;

    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*initializing Drawer layout*/
        drawer = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawer.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);
        drawer.setDrawerListener(this);
        drawerTitles = getResources().getStringArray(R.array.nav_drawer_titles);
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @OnClick(R.id.toolbar)
    public void onClick() {
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        switch (position) {
            case 0:
                GoogleMapActivity.startActivity(this);
                break;
        }
    }
}
