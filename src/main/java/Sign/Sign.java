package Sign;

import org.apache.commons.ssl.PKCS8Key;
import org.apache.xmlbeans.impl.util.Base64;
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

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class Sign {

    public static String getSello(String cadenaOriginal, String KeyBase64, String passwordLlave) throws GeneralSecurityException, IOException {
        InputStream myInputStream = new ByteArrayInputStream(parseBase64Binary(KeyBase64));
        PKCS8Key pkcs8 = new PKCS8Key(myInputStream, passwordLlave.toCharArray());
        java.security.PrivateKey pk = pkcs8.getPrivateKey();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(pk);
        signature.update(cadenaOriginal.getBytes("UTF-8"));
        return printBase64Binary(signature.sign());
    }

    public String getCadena(String xml) {
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

    public static X509Certificate getX509Certificate(final File certificateFile) throws CertificateException, IOException, java.security.cert.CertificateException{
        try (FileInputStream is = new FileInputStream(certificateFile)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(is);
        }
    }

    public static String getCertificadoBase64(final X509Certificate cert) throws CertificateEncodingException {
        return Base64.encode(cert.getEncoded()).toString();
    }

    public static String getKeyBase64(final String key) throws IOException {
            return Base64.encode(Files.readAllBytes(Paths.get(key))).toString();
    }

    public static X509Certificate getAtributosCertificado(final X509Certificate cert){
        return cert;
    }

    public static String getNoCertificado(final X509Certificate cert){
        BigInteger Serial = cert.getSerialNumber();
        byte[] sArr = Serial.toByteArray();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < sArr.length; i++){
            buffer.append((char) sArr[i]);
        }
        return buffer.toString();
    }

    public static String getVigenciaInicio(final X509Certificate cert) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cert.getNotBefore());
    }

    public static String getVigenciaFinal(final X509Certificate cert) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cert.getNotAfter());
    }

    public static String getRazonSocial(final X509Certificate cert){
        String razonSocial;
        String certif = getAtributosCertificado(cert).toString();
        String[] parser = certif.trim().split(",");
        razonSocial = parser[3];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }

    public static String getRFC(final X509Certificate cert){
        String razonSocial;
        String certif = getAtributosCertificado(cert).toString();
        String[] parser = certif.split(",");
        razonSocial = parser[2];
        parser = razonSocial.split("=");
        razonSocial = parser[1];
        parser = razonSocial.split("/");
        return parser[0].trim();
    }
}
