package com.example.otech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Address;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;
    private ArrayList<Address> addresses;
    private OnAddressActionListener listener;

    public interface OnAddressActionListener {
        void onSetDefault(Address address);
        void onEdit(Address address);
        void onDelete(Address address);
        void onSelect(Address address);
    }

    public AddressAdapter(Context context, ArrayList<Address> addresses, OnAddressActionListener listener) {
        this.context = context;
        this.addresses = addresses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addresses.get(position);

        holder.tvAddressName.setText(address.getRecipientName());
        holder.tvAddressPhone.setText(address.getPhone());
        holder.tvAddressDetail.setText(address.getAddressDetail());

        // Show/hide default chip
        if (address.isDefault()) {
            holder.chipDefault.setVisibility(View.VISIBLE);
            holder.btnSetDefault.setEnabled(false);
            holder.btnSetDefault.setText("Mặc định");
        } else {
            holder.chipDefault.setVisibility(View.GONE);
            holder.btnSetDefault.setEnabled(true);
            holder.btnSetDefault.setText("Đặt mặc định");
        }

        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelect(address);
            }
        });

        holder.btnSetDefault.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSetDefault(address);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(address);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(address);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void updateAddresses(ArrayList<Address> newAddresses) {
        this.addresses = newAddresses;
        notifyDataSetChanged();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressName, tvAddressPhone, tvAddressDetail;
        Chip chipDefault;
        MaterialButton btnSetDefault, btnEdit, btnDelete;

        AddressViewHolder(View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.txt_address_name);
            tvAddressPhone = itemView.findViewById(R.id.txt_address_phone);
            tvAddressDetail = itemView.findViewById(R.id.txt_address_detail);
            chipDefault = itemView.findViewById(R.id.chip_default);
            btnSetDefault = itemView.findViewById(R.id.btn_set_default);
            btnEdit = itemView.findViewById(R.id.btn_edit_address);
            btnDelete = itemView.findViewById(R.id.btn_delete_address);
        }
    }
}
