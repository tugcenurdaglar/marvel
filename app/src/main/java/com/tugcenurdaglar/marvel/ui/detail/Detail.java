package com.tugcenurdaglar.marvel.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tugcenurdaglar.marvel.R;
import com.tugcenurdaglar.marvel.model.characters.Result;
import com.tugcenurdaglar.marvel.utility.Const;

public class Detail extends AppCompatActivity {


    @BindView(R.id.activity_detail_character_img)
    ImageView imgCharacterDetail;

    @BindView(R.id.activity_detail_character_txt)
    TextView txtCharacterDetail;

    @BindView(R.id.activity_detail_series_txt)
    TextView txtDetailSeries;

    @BindView(R.id.activity_detail_stories_txt)
    TextView txtDetailStories;

    @BindView(R.id.activity_detail_character_events)
    TextView txtDetailEvents;

    @BindView(R.id.activity_detail_character_comics)
    TextView txtDetailComics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
//        Intent intent = this.getIntent();
//        Bundle  bundle = intent.getExtras();
        Result newResult = (Result) getIntent().getSerializableExtra(Const.KEY_MARVEL_CHARACTER);

        Picasso.get().load(newResult.getThumbnail().getPath() + "." + newResult.getThumbnail().getExtension()).into(imgCharacterDetail);

        txtCharacterDetail.setText(newResult.getName());
        StringBuilder data = new StringBuilder();

        for (int i = 0; i < newResult.getSeries().getItems().size(); i++) {
            data.append(newResult.getSeries().getItems().get(i).getName()).append(",");
        }
        txtDetailSeries.setText(data.toString().substring(0, data.length() - 1));

//        StringBuilder stories = new StringBuilder();
        for (int i = 0; i < newResult.getStories().getItems().size(); i++) {
            data.append(newResult.getStories().getItems().get(i).getName()).append(",");
        }
        txtDetailStories.setText(data.toString().substring(0,data.length() - 1));

        for (int i = 0; i < newResult.getEvents().getItems().size(); i++) {
            data.append(newResult.getEvents().getItems().get(i).getName()).append(",");
        }
        txtDetailEvents.setText(data.toString().substring(0,data.length() - 1));

        for (int i = 0; i < newResult.getComics().getItems().size(); i++) {
            data.append(newResult.getComics().getItems().get(i).getName()).append(",");
        }
        txtDetailComics.setText(data.toString().substring(0,data.length() - 1));


    }
}