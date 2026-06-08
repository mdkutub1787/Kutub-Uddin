package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRBarcode;
import com.logicsoftbd.lsl.databinding.GtmFinishFabricRecyclerLayoutBinding;
import com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.item_click_widget.Fff_Item_Controller;

import java.util.List;

public class Finish_fabric_recycler_adapter extends RecyclerView.Adapter<Finish_fabric_recycler_adapter.MyViewHolder>{

    @SuppressLint("StaticFieldLeak")
    static Context ffra_context;
    static List<FFRBarcode> listOfBarcode;

    public Finish_fabric_recycler_adapter(Context context, List<FFRBarcode> listOfBarcode) {
        ffra_context = context;
        Finish_fabric_recycler_adapter.listOfBarcode = listOfBarcode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ffra_context).inflate(R.layout.gtm_finish_fabric_recycler_layout, parent, false);
        return new Finish_fabric_recycler_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FFRBarcode data = listOfBarcode.get(position);
        holder.binding.serialTV.setText(String.valueOf(position+1));
        holder.binding.barcodeTV.setText(String.valueOf(data.getBarcodeNo()));
        holder.binding.weightTV.setText(String.valueOf(data.getQnty()));
        holder.binding.unSelectCheckBox.setChecked(data.getChecked());
        holder.binding.unSelectCheckBox.setEnabled(data.getChecked());
    }

    @Override
    public int getItemCount() {
        if (listOfBarcode.isEmpty()) {
            return 0;
        }
        return listOfBarcode.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        GtmFinishFabricRecyclerLayoutBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = GtmFinishFabricRecyclerLayoutBinding.bind(itemView);
            binding.unSelectCheckBox.setOnClickListener(view -> {
                if (binding.unSelectCheckBox.isEnabled()){
                    FFRBarcode ffrBarcode= listOfBarcode.get(getAdapterPosition());
                    Fff_Item_Controller.instance.getFff_click_interface().onItemClick(ffrBarcode);
                }else {
                    //Already Unchecked. So any operations doesn't work here.
                }
            });
        }
    }
}
