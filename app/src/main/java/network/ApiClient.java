package network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by haseeb on 15/10/16.
 */
public class ApiClient {
    public static final String BASE_URL = "http://192.168.42.54:8088/";
    private static Retrofit retrofit = null;



    public static Retrofit getClient() {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxAdapter)
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }
}
