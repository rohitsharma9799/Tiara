package com.webdealsoft.Tiara.Activity.Salesadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webdealsoft.Tiara.Activity.Modelclass.ClientModel;
import com.webdealsoft.Tiara.Activity.Receipt;
import com.webdealsoft.Tiara.Activity.SalesActivity;
import com.webdealsoft.Tiara.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SalesAdapterAutoname1 extends RecyclerView.Adapter<SalesAdapterAutoname1.ViewHolder> {
    ArrayList<ClientModel> result;
    Context context;
    Dialog dialog;
    String cid1;
    String PREFS_NAME = "auth_info";

    public SalesAdapterAutoname1(ArrayList<ClientModel> result, Context context, Dialog dialog,String cid1) {
        this.result = result;
        this.context = context;
        this.dialog=dialog;
        this.cid1=cid1;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.autoname,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.name.setText(result.get(position).getName());

        holder.relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  String data = result.get(position).getPincode() + "- "+result.get(position).getAddress() ;
                String pincode = result.get(position).getName();
                String cid = result.get(position).getCid();
                cid1=result.get(position).getCid();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("ccc",result.get(position).getCid());
                editor.commit();
                Receipt groceryMainHomeFragment = new Receipt();
                Receipt.PincodeSet( pincode,cid);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, pincode_address;
        RelativeLayout relay;
        public ViewHolder(View itemView) {
            super(itemView);
            relay = itemView.findViewById(R.id.relay);
            name = itemView.findViewById(R.id.name);
        }
    }
}
