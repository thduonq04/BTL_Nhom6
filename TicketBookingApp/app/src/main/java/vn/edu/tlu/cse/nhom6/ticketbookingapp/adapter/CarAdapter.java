package vn.edu.tlu.cse.nhom6.ticketbookingapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList;
    private Context context;
    private OnCarClickListener listener;

    public interface OnCarClickListener {
        void onEditClick(Car car);
        void onDeleteClick(Car car);
    }

    public CarAdapter(Context context, List<Car> carList, OnCarClickListener listener) {
        this.context = context;
        this.carList = carList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvCarNumber.setText(car.getCarNumber());
        holder.tvSeatCount.setText("Số ghế: " + car.getSeatCount());
        holder.tvPhoneNumber.setText("SĐT: " + car.getPhoneNumber());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(car));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(car));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void updateList(List<Car> newList) {
        carList = newList;
        notifyDataSetChanged();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarNumber, tvSeatCount, tvPhoneNumber;
        ImageButton btnEdit, btnDelete;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarNumber = itemView.findViewById(R.id.tvCarNumber);
            tvSeatCount = itemView.findViewById(R.id.tvSeatCount);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
