package com.tugcenurdaglar.marvel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tugcenurdaglar.marvel.R;
import com.tugcenurdaglar.marvel.controller.CharacterInterface;
import com.tugcenurdaglar.marvel.model.characters.Result;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterHolder> {
    private Context mContext;
    private List<Result> resultList;
    private CharacterInterface characterInterface;

    public CharacterAdapter(Context mContext, List<Result> resultList, CharacterInterface characterInterface) {
        this.mContext = mContext;
        this.resultList = resultList;
        this.characterInterface = characterInterface;
    }

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_character, parent, false);

        return new CharacterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        holder.setData(position);
    }
    public void updateList(List<Result> newResultList) {
        resultList = newResultList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void addAll(List<Result> results) {
        resultList.addAll(results);
        notifyDataSetChanged();
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_character_img)
        ImageView imgCharacter;

        @BindView(R.id.card_character_txt)
        TextView txtCharacter;

        @BindView(R.id.card_character_available)
        TextView txtAvailable;


        public CharacterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {

            Picasso.get().load(resultList.get(position).getThumbnail().getPath() + "." + resultList.get(position).getThumbnail().getExtension()).into(imgCharacter);
            txtCharacter.setText(resultList.get(position).getName());
            txtAvailable.setText(resultList.get(position).getSeries().getAvailable().toString());


        }

        @OnClick(R.id.card_character_img)
        public void onClickImg() {
            characterInterface.didSelectCharacter(resultList.get(getAdapterPosition()));
        }
    }
}
