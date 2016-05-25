package vn.itplus.quanlyxekhach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.item.Seat;

/**
 * Created by AnhlaMrDuc on 24-May-16.
 */
public class TestAdapter extends ArrayAdapter<Seat> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Seat> seats;

    public TestAdapter(Context context, ArrayList<Seat> seats) {
        super(context, -1, seats);
        this.context = context;
        this.seats = seats;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_seat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        viewHolder.txtSeat.setText(position + 1 + "");
        return view;
    }

    @Override
    public Seat getItem(int position) {
        return seats.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_seat)
        TextView txtSeat;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
