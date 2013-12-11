package jetcd;


import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * There are all necessary keys and certificates in src/test/resources/sample_keys
 */
public class TrustedAuthTest {
    private EtcdClient etcdClient;

    @Before
    public void setUp() throws Exception {
        etcdClient = EtcdClientFactory.newTrustedInstance("https://localhost:4001/",
                read("sample_keys/ca.crt"),
                read("sample_keys/client.key.insecure"),
                read("sample_keys/client.crt"));
    }

    private byte[] read(String name) throws IOException {
        URL resource = getClass().getClassLoader().getResource(name);
        try (InputStream is = resource.openStream()) {
            return IOUtils.toByteArray(is);
        }
    }

    @Test
    public void testConnect() throws EtcdException {
        String keyName = "test" + System.currentTimeMillis();
        etcdClient.set(keyName, "3");
        Assert.assertEquals(etcdClient.get(keyName), "3");
    }

}
