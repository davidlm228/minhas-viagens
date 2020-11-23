package br.usjt.ucsist.minhasviagens;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTitleTv, mDescriptionTv;
    View mView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //itemClick
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        //item Long click Listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //Inicializar views com Model_Layout.xml
        mTitleTv = itemView.findViewById(R.id.rTitleTv);
        mDescriptionTv = itemView.findViewById(R.id.rDescriptionTv);

    }


    private ViewHolder.ClickListener mClickListener;
    //Interface para o Click Listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }





}
