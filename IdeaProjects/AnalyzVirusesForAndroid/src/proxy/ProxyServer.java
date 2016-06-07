package proxy;

import javafx.scene.control.TextArea;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
import org.littleshoot.proxy.mitm.RootCertificateException;

/**
 * Created by alex on 06.06.16.
 */
public class ProxyServer {

    private static volatile ProxyServer proxyServer;

    private HttpProxyServer proxy;

    public static ProxyServer getInstance() {
        ProxyServer localProxyServer = proxyServer;
        if (localProxyServer == null) {
            synchronized (ProxyServer.class) {
                localProxyServer = proxyServer;
                if (localProxyServer == null) {
                    proxyServer = localProxyServer = new ProxyServer();
                }
            }
        }
        return proxyServer;
    }

    public void runServer(TextArea textArea) {
        try {
            BasicConfigurationProxy.configure(textArea);
            proxy = DefaultHttpProxyServer.bootstrap()
                    .withPort(9090)
                    .withManInTheMiddle(new CertificateSniffingMitmManager())
                    .start();
        } catch (RootCertificateException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if (proxy != null) {
            proxy.stop();
        }
    }
}
