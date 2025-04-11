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


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> scheduleList;
    private Context context;
    private DatabaseHelper dbHelper;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        // Lấy thông tin tuyến đường từ database (giả sử có phương thức getRouteInfo)
        String routeInfo = "Hà Nội - Sài Gòn"; // Cần thay bằng logic thực tế
        holder.tvRoute.setText(routeInfo);
        holder.tvDepartureTime.setText("Khởi hành: " + schedule.getDepartureTime());
        holder.tvArrivalTime.setText("Đến: " + schedule.getArrivalTime());

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void updateList(List<Schedule> newList) {
        this.scheduleList = newList;
        notifyDataSetChanged();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoute, tvDepartureTime, tvArrivalTime;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);

        }
    }
}