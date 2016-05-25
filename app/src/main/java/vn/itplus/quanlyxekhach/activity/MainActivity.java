package vn.itplus.quanlyxekhach.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.adapter.NormalSeatAdapter;
import vn.itplus.quanlyxekhach.adapter.SeatsAdapter;
import vn.itplus.quanlyxekhach.adapter.SpecialSeatAdapter;
import vn.itplus.quanlyxekhach.fragment.FragmentDrawer;
import vn.itplus.quanlyxekhach.item.Seat;
import vn.itplus.quanlyxekhach.util.Helper;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SPECIAL = 2;
    public static final int BLANK = 1;  // Blank seat
    public static final int UNPAID = 2;  // Seat had people but unpaid
    public static final int PAID = 3;  // Seat paid

    private FragmentDrawer drawer;
    private String drawerTitles[] = null;

    private NormalSeatAdapter normalSeatAdapter;
    private SpecialSeatAdapter specialSeatAdapter;

    private AdapterView.OnItemClickListener onNormalSeatClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            seatUpdated(position, TYPE_NORMAL);
            normalSeatAdapter.notifyDataSetChanged();
        }
    };

    private AdapterView.OnItemClickListener onSpecialSeatClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            seatUpdated(position, TYPE_SPECIAL);
            specialSeatAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemLongClickListener onSpecialLongClickListner = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showLongToast("seat Long clicked");
            return true;
        }
    };


    @Override
    protected int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Initializing Drawer layout */
        drawer = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawer.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);
        drawer.setDrawerListener(this);
        drawerTitles = getResources().getStringArray(R.array.nav_drawer_titles);

        /* Initializing recycler view */

//        final GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
//
//        layoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                switch( seatsAdapter.getItemViewType(position) ) {
//                    case SeatsAdapter.TYPE_SPECIAL:
//                        return 5;
//                    case SeatsAdapter.TYPE_NORMAL:
//                        return 4;
//                    default:
//                        return 1;
//                }
//            }
//        });
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(seatsAdapter);

        /* Initializing gridview seats */
        ArrayList<Seat> normalSeats = getMapSeats().get(TYPE_NORMAL);
        normalSeatAdapter = new NormalSeatAdapter(this, normalSeats);
        gridViewNormal.setAdapter(normalSeatAdapter);
//
        ArrayList<Seat> specialSeats = getMapSeats().get(TYPE_SPECIAL);
        specialSeatAdapter = new SpecialSeatAdapter(this, specialSeats);
        gridViewSpecial.setAdapter(specialSeatAdapter);

        Helper.getGridViewSize(gridViewNormal);

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        gridViewNormal.setOnItemClickListener(onNormalSeatClickListener);
        gridViewSpecial.setOnItemClickListener(onSpecialSeatClickListener);

        gridViewSpecial.setOnItemLongClickListener(onSpecialLongClickListner);
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
                viewGoogleMapActivity();
                break;
        }
    }

    private void viewGoogleMapActivity() {
//        ConnectionDetector connection = new ConnectionDetector(this);
//        if (!connection.isConnectingToInternet()) {
//            showLongToast(getString(R.string.notif_no_network));
//            return;
//        }
        GoogleMapActivity.startActivity(this);
    }

    private void seatUpdated(int position, int seatType) {
        Seat seatClicked = getMapSeats().get(seatType).get(position);
        switch (seatClicked.getStatus()) {
            case BLANK:
                seatClicked.setBgColor(getResources().getDrawable(R.drawable.bg_seat_unpaid));
                seatClicked.setStatus(UNPAID);
                break;
            case UNPAID:
                seatClicked.setBgColor(getResources().getDrawable(R.drawable.bg_seat_paid));
                seatClicked.setStatus(PAID);
                seatClicked.setPrice(100000);
                break;
            case PAID:
                seatClicked.setBgColor(getResources().getDrawable(R.drawable.bg_seat_bank));
                seatClicked.setStatus(BLANK);
                break;

        }
        showLongToast("seat "+seatClicked.getStatus());
    }

    /**
     * @return seat Map
     */
    private Map<Integer, ArrayList<Seat>> getMapSeats() {
        Map<Integer, ArrayList<Seat>> seatMap = new HashMap<>();
        ArrayList<Seat> specialSeats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Seat seat = new Seat(i + 1, BLANK, getResources().getDrawable(R.drawable.bg_seat_bank),
                    SeatsAdapter.TYPE_SPECIAL, 0);
            specialSeats.add(seat);
        }
        seatMap.put(SeatsAdapter.TYPE_SPECIAL, specialSeats);
        //
        ArrayList<Seat> normalSeats = new ArrayList();
        for (int i = 0; i < 40; i++) {
            Seat seat = new Seat(i + 6, BLANK, getResources().getDrawable(R.drawable.bg_seat_bank),
                    SeatsAdapter.TYPE_NORMAL, 0);
            normalSeats.add(seat);
        }
        seatMap.put(SeatsAdapter.TYPE_NORMAL, normalSeats);
        return seatMap;
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.grid_view_normal)
    GridView gridViewNormal;
    @Bind(R.id.grid_view_special)
    GridView gridViewSpecial;
}
