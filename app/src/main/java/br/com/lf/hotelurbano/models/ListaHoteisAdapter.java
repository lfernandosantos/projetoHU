package br.com.lf.hotelurbano.models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.lf.hotelurbano.R;
import br.com.lf.hotelurbano.intefaces.RecyclerViewOnClickListenerHack;


/**
 * Created by ferna on 12/02/2017.
 */

public class ListaHoteisAdapter extends RecyclerView.Adapter<ListaHoteisAdapter.MyViewHolder> {
    private List<Hotel> mList;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public ListaHoteisAdapter(Context context, List<Hotel> list){
        mList = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_hotel_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.imageHotel.setImageResource(R.drawable.sem_foto_icone);
        holder.nomeHotel.setText(mList.get(position).nome);
        holder.cidadeHotel.setText(mList.get(position).cidade);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageHotel;
        public TextView nomeHotel;
        public TextView cidadeHotel;

        public MyViewHolder(View itemView){
            super(itemView);

           imageHotel = (ImageView) itemView.findViewById(R.id.img_hotel_lista);
           nomeHotel = (TextView) itemView.findViewById(R.id.text_hotel_lista);
           cidadeHotel = (TextView) itemView.findViewById(R.id.text_cidade_lista);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(view, getPosition());
            }
        }
    }


}
