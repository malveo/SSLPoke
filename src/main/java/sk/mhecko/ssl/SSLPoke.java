package sk.mhecko.ssl;

import java.security.cert.CertPath;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.SSLSession;

/**
 * Created by michalh on 21. 4. 2017.
 */
public class SSLPoke {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: "+SSLPoke.class.getName()+" <host> <port>");
            System.exit(1);
        }

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(args[0], Integer.parseInt(args[1]));

            SSLSession session = sslsocket.getSession();
            java.security.cert.Certificate[] servercerts = session.getPeerCertificates();

            List mylist = new ArrayList();
            for (int i = 0; i < servercerts.length; i++) {
                mylist.add(servercerts[i]);
            }

            InputStream in = sslsocket.getInputStream();
            OutputStream out = sslsocket.getOutputStream();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CertPath cp = cf.generateCertPath(mylist);

            // Write a test byte to get a reaction :)
            out.write(1);

            while (in.available() > 0) {
                System.out.print(in.read());
            }
            System.out.println(cp);
            System.out.println("Successfully connected");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
