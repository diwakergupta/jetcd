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

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

interface EtcdApi {
  @FormUrlEncoded
  @PUT("/v2/keys/{key}")
  EtcdResponse set(@Path("key") String key,
           @Field("value") String value,
           @Field("ttl") Integer ttl,
           @Field("dir") Boolean dir,
           @Query("prevValue") String prevValue) throws EtcdException;

  @GET("/version")
  Response version();

  @GET("/v2/keys/{key}")
  EtcdResponse get(@Path("key") String key) throws EtcdException;

  @DELETE("/v2/keys/{key}")
  EtcdResponse delete(@Path("key") String key) throws EtcdException;
}
