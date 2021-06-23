package com.tugcenurdaglar.marvel.retrofit;

import com.tugcenurdaglar.marvel.model.characters.GetCharactersResponseModel;

import androidx.appcompat.app.AppCompatDelegate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MarvelService {

    @GET(ApiUtils.CHARACTERS_URL)
    Call<GetCharactersResponseModel> getCharacters(@Query("limit") int limit, @Query("offset") int offset);

    @GET(ApiUtils.CHARACTERS_URL)
    Call<GetCharactersResponseModel> getSearchData(@Query("nameStartsWith") String nameStartsWith);

}
