package Sign;

import org.apache.commons.ssl.PKCS8Key;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class Sign {
           
    public String sello;
    public String certificadoBase64;
    public String keyBase64;
    public String noCertificado;
    public String certificadoFechaInicio;
    public String certificadoFechaVigencia;
    public String certificadoRazonSocial;
    public String certificadoRFC;
    
    public static String JAVA_VERSION = getVersion();
    
    static String getVersion() {
        String version = System.getProperty("java.version");
        return version.substring (0,version.indexOf( '.', version.indexOf('.')+1));
    }

    public static String signGet(String originalString, byte[] keyBase64, String passwordKey) throws GeneralSecurityException, IOException {
        PKCS8Key pkcs8 = new PKCS8Key(keyBase64, passwordKey.toCharArray());
        java.security.PrivateKey pk = pkcs8.getPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(pk);
        signature.update(originalString.getBytes("UTF-8"));
        return printBase64Binary(signature.sign());      
    }

    public static String originalStringGet(String xml) throws UnsupportedEncodingException, TransformerException {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            String filePath = new File("").getAbsolutePath();
            Source xslt = new StreamSource(new File(filePath.concat("/src/main/Resources/assets/cadenaoriginal_3_3.xslt")));
            Transformer transformer = transformerFactory.newTransformer(xslt);
            Source xmlSource = new StreamSource(new StringReader(xml));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Result out = new StreamResult(baos);
            transformer.transform(xmlSource, out);
            byte[] cadenaOriginalArray = baos.toByteArray();
            String cadOrig = new String(cadenaOriginalArray, "UTF-8");
            return cadOrig;
    }

    public static X509Certificate certificateGetX509(final byte[] certificateBytes) throws java.security.cert.CertificateException{
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateBytes));
    }

    public static X509Certificate attributesCertificateGet(final X509Certificate certificate){
        return certificate;
    }

    public static String noCertificateGet(final X509Certificate certificate){
        BigInteger Serial = certificate.getSerialNumber();
        return new String(Serial.toByteArray());
    }

    public static String validityStartGet(final X509Certificate certificate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(certificate.getNotBefore());
    }

    public static String validityEndGet(final X509Certificate certificate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(certificate.getNotAfter());
    }

    public static String bussinesNameGet(final X509Certificate certificate){
        String razonSocial;
        String certif = attributesCertificateGet(certificate).toString();
        String[] parser = certif.trim().split(",");
        razonSocial = parser[3];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }

    public static String rfcGet(final X509Certificate certificate){
        String razonSocial;
        String certif = attributesCertificateGet(certificate).toString();
        String[] parser = certif.split(",");
        razonSocial = parser[2];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }
}
