package vn.itplus.quanlyxekhach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.itplus.quanlyxekhach.R;
import vn.itplus.quanlyxekhach.item.Seat;

/**
 * Created by AnhlaMrDuc on 18-May-16.
 */
public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.SeatViewHolder> {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SPECIAL = 2;

    private Context context;
    private Map<Integer, ArrayList<Seat>> seatMap;
    private LayoutInflater inflater;

    public SeatsAdapter(Context context, Map<Integer, ArrayList<Seat>> seatMap) {
        this.seatMap = seatMap;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public SeatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_NORMAL) {
            view = inflater.inflate(R.layout.layout_seat, parent, false);
        } else if (viewType == TYPE_SPECIAL) {
            view = inflater.inflate(R.layout.layout_seat_special, parent, false);
        }
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeatViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SPECIAL) {
            int seatNumber = position + 1;
            holder.txtSeat.setText(seatNumber + "");
        } else {
            int seatNumber = position + 5;
            holder.txtSeat.setText(seatNumber + "");
        }

    }

    @Override
    public int getItemCount() {
        return seatMap.get(TYPE_NORMAL).size() + 5;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_SPECIAL : TYPE_NORMAL;
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_seat)
        TextView txtSeat;

        public SeatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}