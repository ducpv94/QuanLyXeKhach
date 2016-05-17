package vn.itplus.quanlyxekhach.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.adapter.NavigationDrawerAdapter;
import vn.itplus.quanlyxekhach.item.NavDrawerItem;

/**
 * Created by AnhlaMrDuc on 17-May-16.
 */
public class FragmentDrawer extends Fragment {
    private static String TAG = "FragmentDrawer";

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;

    private static String[] titles = null;
    private static int[] menuIcons;

    public FragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    private ClickListener onClicklistener = new ClickListener() {
        @Override
        public void onClick(View view, int position) {
            drawerListener.onDrawerItemSelected(view, position);
            mDrawerLayout.closeDrawer(containerView);
        }

        @Override
        public void onLongClick(View view, int position) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_titles);
        menuIcons = new int[]{R.drawable.ic_route, R.drawable.ic_statistics, R.drawable.ic_logout};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_nav_drawer, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, onClicklistener));
        return view;
    }

    private List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcon(menuIcons[i]);
            data.add(navItem);
        }
        return data;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = showDrawer(drawerLayout, toolbar);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_humberger_small);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    private ActionBarDrawerToggle showDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        return new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        private RecyclerView recyclerView;

        private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        };

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final ClickListener clickListener) {
            this.clickListener = clickListener;
            this.recyclerView = recyclerView;
            gestureDetector = new GestureDetector(context, simpleOnGestureListener);

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

}
