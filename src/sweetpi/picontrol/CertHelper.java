package sweetpi.picontrol;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.util.Base64;

public class CertHelper {

	
	public static String serializeCert(X509Certificate cert) throws CertificateEncodingException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("-----BEGIN CERTIFICATE-----\n");
		buffer.append(Base64.encodeToString(cert.getEncoded(), Base64.DEFAULT));
		buffer.append("\n-----END CERTIFICATE-----");
		return buffer.toString();
	}

	public static X509Certificate unserializeCert(String cert) throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream in = new ByteArrayInputStream(cert.getBytes() );
		return (X509Certificate) cf.generateCertificate(in);
	}
	
}
	