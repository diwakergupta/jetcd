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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.Map;

public class EtcdClientImpl implements EtcdClient {
    private final EtcdApi etcd;

    EtcdClientImpl(final String server) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(server)
                .build();
        etcd = restAdapter.create(EtcdApi.class);
    }

    @Override
    public String get(String key) throws EtcdException {
        Preconditions.checkNotNull(key);
        try {
            return etcd.get(key).value;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void set(String key, String value) throws EtcdException {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        try {
            etcd.set(key, value, /*ttl=*/ null);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void set(String key, String value, int ttl) throws EtcdException {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        try {
            etcd.set(key, value, ttl);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void delete(String key) throws EtcdException {
        Preconditions.checkNotNull(key);
        try {
            etcd.delete(key);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public Map<String, String> list(String path) throws EtcdException {
        Preconditions.checkNotNull(path);
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        try {
            for (Response response : etcd.list(path)) {
                builder.put(response.key, response.value);
            }
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
        return builder.build();
    }

    @Override
    public String testAndSet(String key, String oldValue, String newValue)
            throws EtcdException {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(oldValue);
        Preconditions.checkNotNull(newValue);
        Preconditions.checkArgument(!oldValue.equals(newValue));
        try {
            Response response = etcd.testAndSet(key, oldValue, newValue);
            return response.prevValue;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }
}
