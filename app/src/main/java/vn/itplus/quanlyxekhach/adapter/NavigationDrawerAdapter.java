package vn.itplus.quanlyxekhach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.item.NavDrawerItem;

/**
 * Created by AnhlaMrDuc on 17-May-16.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NavDrawerItem item = data.get(position);
        holder.rowTitle.setText(item.getTitle());
        holder.rowIcon.setImageResource(item.getIcon());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_icon)
        ImageView rowIcon;
        @Bind(R.id.row_title)
        TextView rowTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
