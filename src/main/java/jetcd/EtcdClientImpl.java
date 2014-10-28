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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.converter.JacksonConverter;


final class EtcdClientImpl implements EtcdClient {
  private static final ObjectMapper objectMapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  private final EtcdApi etcd;

  EtcdClientImpl(final Optional<Client> client, final String server) {
    RestAdapter.Builder builder = new RestAdapter.Builder()
      .setConverter(new JacksonConverter(objectMapper))
      .setEndpoint(server)
      .setErrorHandler(new EtcdErrorHandler());
    if (client.isPresent()) {
      builder.setClient(client.get());
    }
    etcd = builder.build().create(EtcdApi.class);
  }

  @Override
  public String version() throws EtcdException {
    try (InputStreamReader reader = new InputStreamReader(
        etcd.version().getBody().in(), Charsets.UTF_8)) {
      return CharStreams.toString(reader);
    } catch (IOException e) {
      return "ERROR: " + e.getMessage();
    }
  }

  @Override
  public String get(final String key) throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
    return etcd.get(key).getNode().getValue();
  }

  @Override
  public void set(final String key, final String value) throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
    etcd.set(key, value, null, null, null);
  }

  @Override
  public void set(final String key, final String value, final int ttl)
      throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
    Preconditions.checkArgument(ttl > 0);
    etcd.set(key, value, ttl, null, null);
  }

  @Override
  public void delete(final String key) throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
    etcd.delete(key);
  }

  @Override
  public Map<String, String> list(final String path) throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(path));
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    for (EtcdResponse.Node node : etcd.get(path).getNode().getNodes()) {
      builder.put(node.getKey(), node.getValue());
    }
    return builder.build();
  }

  @Override
  public void compareAndSwap(final String key, final String oldValue,
                             final String newValue) throws EtcdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(oldValue));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(newValue));
    Preconditions.checkArgument(!oldValue.equals(newValue));

    EtcdResponse response = etcd.set(key, newValue, null, null, oldValue);
    response.getNode().getValue();
  }

  private static final class EtcdErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(final RetrofitError cause) {
        if (cause.getResponse() == null) {
            return cause;
        }
        return (EtcdException) cause.getBodyAs(EtcdException.class);
    }
  }
}
