package jetcd.trusted;

/**
* Created by ahomyakov on 13.12.13.
*/
public class Credentials {
    private byte[] caCert;
    private byte[] clientKey;
    private byte[] clientCert;

    public Credentials(byte[] caCert, byte[] clientKey, byte[] clientCert) {
        this.caCert = caCert;
        this.clientKey = clientKey;
        this.clientCert = clientCert;
    }

    public byte[] getCaCert() {
        return caCert;
    }

    public byte[] getClientKey() {
        return clientKey;
    }

    public byte[] getClientCert() {
        return clientCert;
    }
}
