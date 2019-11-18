package com.webdealsoft.Tiara.Activity.Salesadapter;

import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.webdealsoft.Tiara.Activity.Modelclass.ClientModel;
import com.webdealsoft.Tiara.Activity.Modelclass.Modelclass;
import com.webdealsoft.Tiara.Activity.SalesActivity;
import com.webdealsoft.Tiara.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SalesAdapterAutoname extends RecyclerView.Adapter<SalesAdapterAutoname.ViewHolder> {
    ArrayList<ClientModel> result;
    Context context;
    Dialog dialog;
    String PREFS_NAME = "auth_info";

    public SalesAdapterAutoname(ArrayList<ClientModel> result, Context context, Dialog dialog) {
        this.result = result;
        this.context = context;
        this.dialog=dialog;
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
        holder.email.setText(result.get(position).getEmail());
        holder.mobile.setText(result.get(position).getMobile());

        holder.relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  String data = result.get(position).getPincode() + "- "+result.get(position).getAddress() ;
                String pincode = result.get(position).getName();
                String cid = result.get(position).getCid();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("cc",result.get(position).getCid());
                editor.commit();
                SalesActivity groceryMainHomeFragment = new SalesActivity();
                SalesActivity.PincodeSet( pincode,cid);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile,email;
        RelativeLayout relay;
        public ViewHolder(View itemView) {
            super(itemView);
            relay = itemView.findViewById(R.id.relay);
            name = itemView.findViewById(R.id.name);
            mobile=itemView.findViewById(R.id.mobile);
            email=itemView.findViewById(R.id.email);
        }
    }
}
