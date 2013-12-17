package jetcd.trusted;


import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import retrofit.client.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class TrustedApacheClient implements Client {
    public static final String FAKE_PASSWD = "1234";

    private final ApacheClient apacheClient;

    public TrustedApacheClient(Credentials credentials,
                               HttpParams httpParams) {
        initSecurityProvider();
        SchemeRegistry schemeRegistry = createSchemaRegistry(credentials);
        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        DefaultHttpClient httpClient = new DefaultHttpClient(connManager, httpParams);
        apacheClient = new ApacheClient(httpClient);
    }

    public TrustedApacheClient(Credentials credentials) {
        this(credentials, new BasicHttpParams());
    }


    public retrofit.client.Response execute(Request request) throws IOException {
        return apacheClient.execute(request);
    }


    private void initSecurityProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }


    private SchemeRegistry createSchemaRegistry(Credentials credentials) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        Scheme https = initializeHttpsSchema(credentials);
        schemeRegistry.register(https);
        return schemeRegistry;
    }

    private Scheme initializeHttpsSchema(Credentials credentials) {
        SSLSocketFactory socketFactory = createSocketFactory(credentials);
        return new Scheme("https", socketFactory, 443);
    }

    private SSLSocketFactory createSocketFactory(Credentials credentials) {
        KeyStore clientKeystore = createClientKeystoreSafe(
                credentials.getClientKey(),
                credentials.getClientCert());
        KeyStore trustedKeystore = createTrustedKeystoreSafe(
                credentials.getCaCert());
        return createSocketFactorySafe(clientKeystore, trustedKeystore);
    }

    private SSLSocketFactory createSocketFactorySafe(KeyStore clientKeystore, KeyStore trustedKeystore) {
        SSLSocketFactory socketFactory = null;
        try {
            socketFactory = new SSLSocketFactory(clientKeystore, FAKE_PASSWD, trustedKeystore);
        } catch (KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            throw new TrustedHttpException("Can not create socket factory: wrong key, certificate or keystore", e);
        } catch (NoSuchAlgorithmException e) {
            throw new TrustedHttpException("Wrong code or system configuration", e);
        }
        return socketFactory;
    }

    private KeyStore createTrustedKeystoreSafe(byte[] caCert) {
        KeyStore truststore;
        try {
            truststore = createTrustedKeystore(caCert);
        } catch (KeyStoreException | CertificateException e) {
            throw new TrustedHttpException("Invalid CA certificate", e);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new TrustedHttpException("Wrong code or system configuration", e);
        }
        return truststore;
    }

    private KeyStore createClientKeystoreSafe(byte[] clientKey, byte[] clientCert) {
        KeyStore keystore;
        try {
            keystore = createClientKeystore(clientKey, clientCert);
        } catch (CertificateException | KeyStoreException e) {
            throw new TrustedHttpException("Invalid certificate or key", e);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new TrustedHttpException("Wrong code or system configuration", e);
        }
        return keystore;
    }


    private KeyStore createClientKeystore(byte[] clientKey, byte[] clientCert)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        final KeyStore keystore = createBlankKeystore();
        java.security.cert.Certificate[] certChain = {createCertificate(clientCert)};
        KeyPair kp = readKeyPair(clientKey);
        keystore.setKeyEntry("clientKey", kp.getPrivate(), FAKE_PASSWD.toCharArray(), certChain);
        return keystore;
    }

    private KeyPair readKeyPair(byte[] clientKey) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(clientKey);
             InputStreamReader isr = new InputStreamReader(bis);
             PEMReader pemReader = new PEMReader(isr)) {
            return (KeyPair) pemReader.readObject();
        }
    }

    private KeyStore createTrustedKeystore(byte[] caCert)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore ks = createBlankKeystore();
        ks.setCertificateEntry("ca", createCertificate(caCert));
        return ks;
    }

    private KeyStore createBlankKeystore()
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(null, FAKE_PASSWD.toCharArray());
        return keyStore;
    }

    private java.security.cert.Certificate createCertificate(byte[] certBytes) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    public static class TrustedHttpException extends RuntimeException {
        public TrustedHttpException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
