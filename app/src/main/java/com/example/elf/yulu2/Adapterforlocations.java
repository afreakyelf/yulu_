package com.example.elf.yulu2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.elf.yulu2.MapsActivity.mMap;


public class Adapterforlocations extends RecyclerView.Adapter<Adapterforlocations.ViewHolder>{

    ArrayList<HashMap<String, Object>> al_details;
    MapsActivity mapsActivity;
    Context mContext;


    ImageView iv_image;
    TextView tv_title;


    public Adapterforlocations(ArrayList<HashMap<String,Object>> al_details, MapsActivity mapsActivity, Context context) {

        this.al_details = al_details;
        this.mapsActivity=mapsActivity;
        this.mContext=context;
    }


    @Override
    public Adapterforlocations.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insidecard, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);

        return viewHolder1;
    }



    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image ;
        public ImageView share ,bookmark;
        TextView tv_title,lat;
        RelativeLayout rl_click;



        public ViewHolder(View v) {

            super(v);

            iv_image = v.findViewById(R.id.imageview);
            tv_title = v.findViewById(R.id.tv1);
            lat = v.findViewById(R.id.tv2);
            rl_click = v.findViewById(R.id.relmarket);


        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder vholder, final int position) {



        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(26));

            Glide.with(mContext).load(al_details.get(position).get("image"))
                    .apply(requestOptions)
                    .into(vholder.iv_image);

        } catch (Exception e) {
            e.printStackTrace();

        }

        vholder.tv_title.setText(al_details.get(position).get("title").toString());
        vholder.lat.setText(al_details.get(position).get("latitude")+" , "+al_details.get(position).get("longitude"));

        vholder.rl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Double lat = Double.valueOf( al_details.get(position).get("latitude").toString());
                Double longt = Double.valueOf( al_details.get(position).get("longitude").toString());

                LatLng mylatlng = new LatLng(lat,longt);
                   CameraUpdate c = CameraUpdateFactory.newLatLngZoom(mylatlng,15);

                int height = 100;
                int width = 100;
                BitmapDrawable bitmapdraw=(BitmapDrawable)mContext.getResources().getDrawable(R.drawable.placeholder);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                mMap.addMarker(new MarkerOptions().title("You are here").position(mylatlng)
                        .title(al_details.get(position).get("title").toString())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .draggable(true)
                        .flat(true)
                        .alpha(0.5f));
                mMap.addCircle(new CircleOptions().radius(10).center(mylatlng).fillColor(Color.CYAN).visible(true));
                mMap.animateCamera(c);

            }
        });



    }

    private void abc(final String time) {

    }


    @Override
    public int getItemCount() {

        return al_details.size();
    }



}

