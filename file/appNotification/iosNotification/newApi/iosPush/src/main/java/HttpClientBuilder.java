
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.CertificateUtils;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpClientBuilder {

    private String protocol = "TLSv1.2";
    private boolean useALPN;
    private String keyStorePassword;
    private Resource keyStoreResource;

    public HttpClientBuilder withProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    public HttpClientBuilder withUseALPN(final boolean useALPN) {
        this.useALPN = useALPN;
        return this;
    }

    public HttpClientBuilder withKeyStorePassword(final String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public HttpClientBuilder withKeyStoreResource(final Resource keyStoreResource) {
        this.keyStoreResource = keyStoreResource;
        return this;
    }

    public HttpClient build() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException, KeyManagementException {
        if (StringUtils.isBlank(this.keyStorePassword)) {
            throw new IllegalArgumentException("Missing keyStorePassword");
        }

        if (this.keyStoreResource == null) {
            throw new IllegalArgumentException("Missing keyStoreResource");
        }

        final HTTP2Client client = new HTTP2Client();
        final HttpClientTransportOverHTTP2 transport = new HttpClientTransportOverHTTP2(client);
        transport.setUseALPN(this.useALPN);
        //        SslContextFactory sslContextFactory1 = new SslContextFactory();
        final SslContextFactory sslContextFactory = new SslContextFactory.Client() {

            @Override
            protected KeyStore loadTrustStore(final Resource resource) throws Exception {
                return null;
            }

        };

        //        final KeyStore keyStore = KeyStore.getInstance("jks");
        //        Resource trustResource = Resource.newResource(new File("D:\\opt\\servers\\sslkeystore\\sslkeystore.jks"));
        //        keyStore.load(trustResource.getInputStream(), "abcd-1234".toCharArray());
        //        sslContextFactory.setProtocol(this.protocol);
        sslContextFactory.setKeyStoreType("PKCS12");
        sslContextFactory.setKeyStoreResource(this.keyStoreResource);
        //        sslContextFactory.setTrustStoreResource(resource);
        sslContextFactory.setKeyStorePassword(this.keyStorePassword);

        //        final char[] pwdChars = this.keyStorePassword.toCharArray();
        //        final KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //        keyStore.load(this.keyStoreResource.getInputStream(), pwdChars);
        //        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
        //        keyManagerFactory.init(keyStore, pwdChars);
        //        //init TrustManager
        //        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
        //        trustManagerFactory.init((KeyStore) null);
        //        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        //        //init SSLContext
        //        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        //        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
        //
        //        sslContextFactory1.setSslContext(sslContext);

        return new HttpClient(transport, sslContextFactory);
    }

}
