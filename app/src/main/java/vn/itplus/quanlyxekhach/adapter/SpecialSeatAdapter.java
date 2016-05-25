package vn.itplus.quanlyxekhach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.activity.MainActivity;
import vn.itplus.quanlyxekhach.item.Seat;

/**
 * Created by AnhlaMrDuc on 24-May-16.
 */
public class SpecialSeatAdapter extends ArrayAdapter<Seat> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Seat> seats;

    public SpecialSeatAdapter(Context context, ArrayList<Seat> list) {
        super(context, -1, list);
        this.context = context;
        this.seats = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Seat seat = getItem(position);
        convertView = inflater.inflate(R.layout.layout_seat, parent, false);
        SpecialViewHolder specialViewHolder = new SpecialViewHolder(convertView);
        convertView.setTag(specialViewHolder);

        if (seat.getStatus() == MainActivity.PAID) {
            specialViewHolder.txtSeat.setTextColor(context.getResources().getColor(R.color.white));
            specialViewHolder.txtAmount.setVisibility(View.VISIBLE);
            specialViewHolder.txtAmount.setText(seat.getPrice()+"");
        }
        specialViewHolder.txtSeat.setText(seat.getId() + "");
        specialViewHolder.rlSeat.setBackground(seat.getBgColor());
        return convertView;
    }

    @Override
    public Seat getItem(int position) {
        return seats.get(position);
    }

    static class SpecialViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_seat)
        TextView txtSeat;
        @Bind(R.id.rl_seat)
        RelativeLayout rlSeat;
        @Bind(R.id.txt_amount)
        TextView txtAmount;

        public SpecialViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
