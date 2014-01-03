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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.JacksonConverter;

import java.util.Map;

public class EtcdClientImpl implements EtcdClient {
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final EtcdApi etcd;

    EtcdClientImpl(final String server) {
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setConverter(new JacksonConverter(objectMapper))
            .setServer(server)
            .setErrorHandler(new EtcdErrorHandler())
            .build();
        etcd = restAdapter.create(EtcdApi.class);
    }

    @Override
    public String get(String key) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        return etcd.get(key).getNode().getValue();
    }

    @Override
    public void set(String key, String value) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
        etcd.set(key, value, null, null, null);
    }

    @Override
    public void set(String key, String value, int ttl) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
        Preconditions.checkArgument(ttl > 0);
        etcd.set(key, value, ttl, null, null);
    }

    @Override
    public void delete(String key) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        etcd.delete(key);
    }

    @Override
    public Map<String, String> list(String path) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path));
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        for (EtcdResponse.Node node : etcd.get(path).getNode().getNodes()) {
            builder.put(node.getKey(), node.getValue());
        }
        return builder.build();
    }

    @Override
    public void compareAndSwap(String key, String oldValue, String newValue) throws EtcdException {
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
            return (EtcdException) cause.getBodyAs(EtcdException.class);
        }
    }
}
