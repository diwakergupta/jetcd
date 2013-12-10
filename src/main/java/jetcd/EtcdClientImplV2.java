package jetcd;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.Map;

class EtcdClientImplV2 implements EtcdClient {
    private final EtcdApiV2 etcd;

    EtcdClientImplV2(final String server) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(server)
                .build();
        etcd = restAdapter.create(EtcdApiV2.class);
    }

    @Override
    public String get(String key) throws EtcdException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key));
        try {
            return etcd.get(key).node.value;
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
            ResponseV2 response = etcd.list(path);
            Preconditions.checkNotNull(response.node.nodes);
            for (ResponseV2.Node node : response.node.nodes) {
                builder.put(node.key, node.value);
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
            ResponseV2 response = etcd.testAndSet(key, oldValue, null, null, newValue);
            return response.node.prevValue;
        } catch (RetrofitError e) {
            throw new EtcdException(e);
        }
    }
}
