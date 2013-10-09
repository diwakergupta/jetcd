/*
 * Copyright 2013 Diwaker Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    Response get(@Path("key") String key);

    @FormUrlEncoded
    @POST("/v1/keys/{key}")
    Response set(@Path("key") String key, @Field("value") String value);

    @DELETE("/v1/keys/{key}")
    Response delete(@Path("key") String key);

    @GET("/v1/keys/{key}/")
    List<Response> list(@Path("key") String key);

    @FormUrlEncoded
    @POST("/v1/keys/{key}")
    Response testAndSet(@Path("key") String key,
        @Field("prevValue") String prevValue, @Field("value") String value);
}
