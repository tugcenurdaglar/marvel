package com.tugcenurdaglar.marvel.ui.character;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tugcenurdaglar.marvel.ui.detail.Detail;
import com.tugcenurdaglar.marvel.R;
import com.tugcenurdaglar.marvel.adapter.CharacterAdapter;
import com.tugcenurdaglar.marvel.controller.CharacterInterface;
import com.tugcenurdaglar.marvel.model.characters.GetCharactersResponseModel;
import com.tugcenurdaglar.marvel.model.characters.Result;
import com.tugcenurdaglar.marvel.retrofit.ApiUtils;
import com.tugcenurdaglar.marvel.utility.Const;

import java.util.ArrayList;
import java.util.List;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;
import static butterknife.OnTextChanged.Callback.BEFORE_TEXT_CHANGED;

public class MainActivity extends AppCompatActivity implements CharacterInterface {

    @BindView(R.id.activity_main_rcy)
    RecyclerView rcyCharacter;

    @BindView(R.id.activity_main_change_view_button)
    ImageView changeButton;

    @BindView(R.id.activity_main_editText)
    EditText editText;
    @BindView(R.id.activity_main_progress_bar)
    View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadData();

    }


    boolean isGridLayoutVisible = false;

    @OnClick(R.id.activity_main_change_view_button)
    public void onClickChangeLayout() {
        if (isGridLayoutVisible) {
            rcyCharacter.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            isGridLayoutVisible = false;
            changeButton.setImageResource(R.drawable.ic_baseline_grid_view_24);
        } else {
            rcyCharacter.setLayoutManager(new GridLayoutManager(rcyCharacter.getContext(), 2));
            isGridLayoutVisible = true;
            changeButton.setImageResource(R.drawable.ic_baseline_auto_awesome_motion_24);
        }
    }


    @OnTextChanged(value = R.id.activity_main_editText, callback = AFTER_TEXT_CHANGED)
    public void onChangeEdittext(CharSequence text) {
        if (text.length() > 0) {
            filter(text.toString());
        } else {
            loadOldData();
        }
    }

    public void filter(String text) {
        showProgress();
        List<Result> temp = new ArrayList();
        ApiUtils.getMarvelService().getSearchData(text).enqueue(new Callback<GetCharactersResponseModel>() {
            @Override
            public void onResponse(Call<GetCharactersResponseModel> call, Response<GetCharactersResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        if (response.body().getData().getResults() != null) {
                            for (Result r : response.body().getData().getResults()) {
                                if (r.getName().toLowerCase().contains(text.toLowerCase())) {
                                    temp.add(r);
                                }
                            }
                            characterAdapter.updateList(temp);
                            hideProgress();
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<GetCharactersResponseModel> call, Throwable t) {

            }
        });
    }

    private void loadOldData() {
        characterAdapter.updateList(resultList);
    }


    int limit = 10;
    int offset = 0;
    CharacterAdapter characterAdapter;
    List<Result> resultList;

    private void loadData() {
        showProgress();
        ApiUtils.getMarvelService().getCharacters(limit, offset).enqueue(new Callback<GetCharactersResponseModel>() {
            @Override
            public void onResponse(Call<GetCharactersResponseModel> call, Response<GetCharactersResponseModel> response) {
                resultList = response.body().getData().getResults();
                characterAdapter = new CharacterAdapter(MainActivity.this, resultList, MainActivity.this);

                rcyCharacter.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rcyCharacter.setAdapter(characterAdapter);
                rcyCharacter.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            loadPagination();
                        }
                    }
                });
                hideProgress();
            }

            @Override
            public void onFailure(Call<GetCharactersResponseModel> call, Throwable t) {

            }
        });
    }

    private void loadPagination() {
        showProgress();
        offset = offset + limit;
        ApiUtils.getMarvelService().getCharacters(limit, offset).enqueue(new Callback<GetCharactersResponseModel>() {
            @Override
            public void onResponse(Call<GetCharactersResponseModel> call, Response<GetCharactersResponseModel> response) {
                resultList.addAll(response.body().getData().getResults());
                characterAdapter.addAll(response.body().getData().getResults());
                hideProgress();
            }

            @Override
            public void onFailure(Call<GetCharactersResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void didSelectCharacter(Result result) {
        Intent intent = new Intent(MainActivity.this, Detail.class);
        intent.putExtra(Const.KEY_MARVEL_CHARACTER, result);
        startActivity(intent);

    }

    public void showProgress() {
        editText.clearFocus();
        progressBar.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


}