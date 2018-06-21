package Sign;

import org.apache.commons.ssl.PKCS8Key;
import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javax.xml.bind.DatatypeConverter;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class Sign {

    public static String signGet(String cadenaOriginal, String KeyBase64, String passwordLlave) throws GeneralSecurityException, IOException {
        InputStream myInputStream = new ByteArrayInputStream(parseBase64Binary(KeyBase64));
        PKCS8Key pkcs8 = new PKCS8Key(myInputStream, passwordLlave.toCharArray());
        java.security.PrivateKey pk = pkcs8.getPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(pk);
        signature.update(cadenaOriginal.getBytes("UTF-8"));
        return printBase64Binary(signature.sign());
    }

    public String originalStringGet(String xml) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("src/test/java/Tests/assets/cadenaoriginal_3_3.xslt"));
            Transformer transformer = transformerFactory.newTransformer(xslt);
            Source xmlSource = new StreamSource(new StringReader(xml));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Result out = new StreamResult(baos);
            transformer.transform(xmlSource, out);
            byte[] cadenaOriginalArray = baos.toByteArray();
            String cadOrig = new String(cadenaOriginalArray, "UTF-8");
            return cadOrig;
        } catch (TransformerException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static X509Certificate certificateGetX509(final File certificateFile) throws CertificateException, IOException, java.security.cert.CertificateException{
        try (FileInputStream is = new FileInputStream(certificateFile)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(is);
        }
    }

    public static String certificateBase64Get(final X509Certificate cert) throws CertificateEncodingException {
      return Base64.encode(cert.getEncoded());
    }
    
    public static String keyBase64get(final String key) throws IOException {
        return DatatypeConverter.printBase64Binary(new String(Files.readAllBytes( Paths.get(key))).getBytes());
    }

    public static X509Certificate attributesCertificateGet(final X509Certificate cert){
        return cert;
    }

    public static String noCertificateGet(final X509Certificate cert){
        BigInteger Serial = cert.getSerialNumber();
        return new String(Serial.toByteArray());
    }

    public static String validityStartGet(final X509Certificate cert) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cert.getNotBefore());
    }

    public static String validityEndGet(final X509Certificate cert) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cert.getNotAfter());
    }

    public static String bussinesNameGet(final X509Certificate cert){
        String razonSocial;
        String certif = attributesCertificateGet(cert).toString();
        String[] parser = certif.trim().split(",");
        razonSocial = parser[3];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }

    public static String rfcGet(final X509Certificate cert){
        String razonSocial;
        String certif = attributesCertificateGet(cert).toString();
        String[] parser = certif.split(",");
        razonSocial = parser[2];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }
}
