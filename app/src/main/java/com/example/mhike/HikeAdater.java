package com.example.mhike;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class  HikeAdater extends  RecyclerView.Adapter<HikeAdater.HikeViewHodler> implements Filterable {

    private Context mContext;
    private List<Hike> mListHike, mListHikeOld;


    public HikeAdater(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Hike> list){
        this.mListHike = list;
        this.mListHikeOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HikeViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new HikeViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHodler holder, int position) {
        Hike hike = mListHike.get(position);
        if(hike == null){
            return;
        }

        holder.tvName.setText(hike.getName());
        holder.itemDate.setText(hike.getDate());
        holder.itemLength.setText(hike.getLength() + " km");
        File imageFile  = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),hike.getImage());
        String imagePath = imageFile.getAbsolutePath();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        holder.imgItem.setImageBitmap(bitmap);
        holder.hikeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("hike", hike);
                intent.putExtra("mode", 2);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListHike != null){
            return mListHike.size();
        }
        return 0;
    }
//tim kiem
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    mListHike = mListHikeOld;
                }
                else{
                    List<Hike> list = new ArrayList<>();
                    for (Hike hike : mListHikeOld){
                        if(hike.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(hike);
                        }
                    }

                    mListHike = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListHike;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListHike = (List<Hike>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class HikeViewHodler extends RecyclerView.ViewHolder{

        private ImageView imgItem;
        private TextView tvName, itemDate, itemLength;
        private FrameLayout hikeItem;

        LinearLayout layoutForeground;

        public HikeViewHodler(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.itemImg);
            tvName = itemView.findViewById(R.id.itemTitle);
            hikeItem = itemView.findViewById(R.id.hikeItem);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemLength = itemView.findViewById(R.id.itemLength);
            layoutForeground = itemView.findViewById(R.id.layout_foreground);
        }
    }

    public void removeItem(int index){
        mListHike.remove(index);
        notifyItemRemoved(index);
    }

    public void undoItem(Hike hike, int index){
        mListHike.add(index, hike);
        notifyItemInserted(index);
    }

}
