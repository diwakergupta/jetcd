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
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Map;

public class EtcdClientImpl implements EtcdClient {
    private final EtcdApi etcd;

    EtcdClientImpl(final String server) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(server)
                .build();
        etcd = restAdapter.create(EtcdApi.class);
    }

    EtcdClientImpl(final String server,
                   Client client) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(server)
                .setClient(client)
                .build();
        etcd = restAdapter.create(EtcdApi.class);
    }


    @Override
    public String get(String key) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        try {
            return etcd.get(key).value;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void set(String key, String value) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
        try {
            etcd.set(key, value, /*ttl=*/ null);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void set(String key, String value, int ttl) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
        Preconditions.checkArgument(ttl > 0);

        try {
            etcd.set(key, value, ttl);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public void delete(String key) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));

        try {
            etcd.delete(key);
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }

    @Override
    public Map<String, String> list(String path) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path));

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
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(oldValue));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(newValue));
        Preconditions.checkArgument(!oldValue.equals(newValue));

        try {
            Response response = etcd.testAndSet(key, oldValue, newValue);
            return response.prevValue;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }
}
