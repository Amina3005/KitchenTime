package project.android.recipeapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL ="https://api.spoonacular.com/";
    public static Retrofit retrofit;
    public static FoodApi foodApi = null;

    public static Retrofit getRetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
                    .addHeader("apiKey","apiKey=0b04dac1a42848bfa0e68732c13df794");
            Request request1 = requestBuilder.build();
            return chain.proceed(request1);
        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static FoodApi getFoodApi() {
        if (foodApi == null) {
            foodApi = getRetrofitClient().create(FoodApi.class);
        }
        return foodApi;
    }

}
