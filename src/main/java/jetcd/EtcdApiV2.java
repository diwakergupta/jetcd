package jetcd;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface EtcdApiV2 {
    @GET("/v2/keys/{key}")
    ResponseV2 get(@Path("key") String key);

    @FormUrlEncoded
    @PUT("/v2/keys/{key}")
    ResponseV2 set(@Path("key") String key, @Field("value") String value,
                 @Field("ttl") Integer ttl);

    @DELETE("/v2/keys/{key}")
    ResponseV2 delete(@Path("key") String key);

    @GET("/v2/keys/{key}/")
    ResponseV2 list(@Path("key") String key);

    @FormUrlEncoded
    @PUT("/v2/keys/{key}")
    ResponseV2 testAndSet(@Path("key") String key,
                          @Field("prevValue") String prevValue,
                          @Field("prevIndex") Integer prevIndex,
                          @Field("prevExist") Boolean prevExist,
                          @Field("value") String value);
}
