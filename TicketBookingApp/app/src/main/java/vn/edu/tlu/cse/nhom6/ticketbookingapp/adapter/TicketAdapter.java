package vn.edu.tlu.cse.nhom6.ticketbookingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Schedule;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Ticket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> ticketList;
    private Context context;
    private OnTicketActionListener listener;
    private final boolean isStaff;

    public interface OnTicketActionListener {
        void onCancel(Ticket ticket);
        void onConfirm(Ticket ticket);
        void onDelete(Ticket ticket);
    }

    public TicketAdapter(Context context, List<Ticket> ticketList, boolean isStaff, OnTicketActionListener listener) {
        this.context = context;
        this.isStaff = isStaff;
        this.ticketList = ticketList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return isStaff ? 1 : 0;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isStaff ? R.layout.item_ticket_staff : R.layout.item_ticket;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        String route = ticket.getStartLocation() + " -> " + ticket.getEndLocation();
        holder.tvTicketId.setText("Mã vé: " + ticket.getTicket_id());
        holder.tvRoute.setText("Tuyến: " + route);
        holder.tvDepartureTime.setText("Thời gian khởi hành: " + ticket.getDepartureTime());
        holder.tvSeatNumber.setText("Số ghế: " + ticket.getSeat_number());
        holder.tvPrice.setText("Giá vé: " + ticket.getPrice() + " VNĐ");
        holder.tvStatus.setText("Trạng thái: " + ticket.getStatus());





        if (!isStaff) {
            if (ticket.getStatus().equalsIgnoreCase("Hủy")) {
                holder.btnCancel.setEnabled(false);
                holder.btnCancel.setAlpha(0.5f);
            } else {
                holder.btnCancel.setEnabled(true);
                holder.btnCancel.setAlpha(1f);
                holder.btnCancel.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onCancel(ticket);
                    }
                });
            }
        }

        if (isStaff) {
            // tvStatus equal "Đã xác nhận" thị btnConfirm sẽ bị ẩn đi
            if (holder.btnConfirm != null && holder.tvStatus != null) {
                if(holder.tvStatus.getText().toString().equals("Trạng thái: Đã xác nhận")) {
                    holder.btnConfirm.setVisibility(View.GONE);
                } else {
                    holder.btnConfirm.setVisibility(View.VISIBLE);
                }
            }
            holder.btnConfirm.setOnClickListener(v -> listener.onConfirm(ticket));
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(ticket));
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public void updateList(List<Ticket> newList) {
        ticketList = newList;
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketId, tvRoute, tvCarNumber, tvDepartureTime, tvSeatNumber, tvPrice, tvStatus;
        Button btnCancel, btnConfirm, btnDelete;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketId = itemView.findViewById(R.id.tvTicketId);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvCarNumber = itemView.findViewById(R.id.tvCarNumber);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvSeatNumber = itemView.findViewById(R.id.tvSeatNumber);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}