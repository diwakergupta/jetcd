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

import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.HashMap;
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
    public String getKey(String key) throws EtcdException {
        try {
            return etcd.getKey(key).value;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void setKey(String key, String value) throws EtcdException {
        try {
            etcd.setKey(key, value);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void deleteKey(String key) throws EtcdException {
        try {
            etcd.deleteKey(key);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public Map<String, String> list(String path) throws EtcdException {
        Map<String, String> entries = new HashMap<>();
        try {
            for (Response response : etcd.list(path)) {
                entries.put(response.key, response.value);
            }
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
        return entries;
    }

    @Override
    public String testAndSet(String key, String oldValue, String newValue)
            throws EtcdException {
        try {
            Response response = etcd.testAndSet(key, oldValue, newValue);
            return response.prevValue;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }
}
