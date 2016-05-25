package vn.itplus.quanlyxekhach.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by AnhlaMrDuc on 24-May-16.
 */
public class Helper {
    public static void getGridViewSize(GridView gridView) {
        ListAdapter myListAdapter = gridView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount()/4; size++) {
            View listItem = myListAdapter.getView(size, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + gridView.getVerticalSpacing() + 2;
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + 20;
        gridView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

}
