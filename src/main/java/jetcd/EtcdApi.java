package jetcd;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import java.util.List;

public interface EtcdApi {
    @GET("/v1/keys/{key}")
    Response getKey(@Path("key") String key);

    @FormUrlEncoded
    @POST("/v1/keys/{key}")
    Response setKey(@Path("key") String key, @Field("value") String value);

    @DELETE("/v1/keys/{key}")
    Response deleteKey(@Path("key") String key);

    @GET("/v1/keys/{key}/")
    List<Response> list(@Path("key") String key);

    @FormUrlEncoded
    @POST("/v1/keys/{key}")
    Response testAndSet(@Path("key") String key,
        @Field("prevValue") String prevValue, @Field("value") String value);
}
