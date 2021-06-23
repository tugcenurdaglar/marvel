package com.tugcenurdaglar.marvel.retrofit;

public class ApiUtils {
    public static String BASE_URL = "http://gateway.marvel.com/v1/public/";

    public static final String CHARACTERS_URL = "characters";


    public static MarvelService getMarvelService() {
        return RetrofitClient.getClient(BASE_URL).create(MarvelService.class);
    }
}
