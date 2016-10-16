package network;

import org.json.JSONObject;

import java.util.List;

import models.Message;
import models.MessageContent;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by haseeb on 15/10/16.
 */
public interface ApiInterface {


    @GET("api/message/")
    Observable<List<Message>> getMessages();


    @GET("api/message/{id}")
    Observable<MessageContent> getMessageContent(@Path("id") Integer id);

    @DELETE("api/message/{id}")
    Observable<JSONObject> deleteMessage(@Path("id") Integer id);
}
