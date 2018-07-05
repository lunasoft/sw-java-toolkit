package Test.Toolkit;

import Sign.Sign;
import mx.gob.sat.cfd.x3.ComprobanteDocument;
import mx.gob.sat.sitioInternet.cfd.catalogos.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.tool.XSTCTester;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class testToolkit extends XSTCTester.TestCase {

    public String password_csd = "12345678a";
    public String b64Cer = "MIIFxTCCA62gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMjI4MTUwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNjEwMjUyMTUyMTFaFw0yMDEwMjUyMTUyMTFaMIGxMRowGAYDVQQDExFDSU5ERU1FWCBTQSBERSBDVjEaMBgGA1UEKRMRQ0lOREVNRVggU0EgREUgQ1YxGjAYBgNVBAoTEUNJTkRFTUVYIFNBIERFIENWMSUwIwYDVQQtExxMQU43MDA4MTczUjUgLyBGVUFCNzcwMTE3QlhBMR4wHAYDVQQFExUgLyBGVUFCNzcwMTE3TURGUk5OMDkxFDASBgNVBAsUC1BydWViYV9DRkRJMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvvCiCFDFVaYX7xdVRhp/38ULWto/LKDSZy1yrXKpaqFXqERJWF78YHKf3N5GBoXgzwFPuDX+5kvY5wtYNxx/Owu2shNZqFFh6EKsysQMeP5rz6kE1gFYenaPEUP9zj+h0bL3xR5aqoTsqGF24mKBLoiaK44pXBzGzgsxZishVJVM6XbzNJVonEUNbI25DhgWAd86f2aU3BmOH2K1RZx41dtTT56UsszJls4tPFODr/caWuZEuUvLp1M3nj7Dyu88mhD2f+1fA/g7kzcU/1tcpFXF/rIy93APvkU72jwvkrnprzs+SnG81+/F16ahuGsb2EZ88dKHwqxEkwzhMyTbQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAJ/xkL8I+fpilZP+9aO8n93+20XxVomLJjeSL+Ng2ErL2GgatpLuN5JknFBkZAhxVIgMaTS23zzk1RLtRaYvH83lBH5E+M+kEjFGp14Fne1iV2Pm3vL4jeLmzHgY1Kf5HmeVrrp4PU7WQg16VpyHaJ/eonPNiEBUjcyQ1iFfkzJmnSJvDGtfQK2TiEolDJApYv0OWdm4is9Bsfi9j6lI9/T6MNZ+/LM2L/t72Vau4r7m94JDEzaO3A0wHAtQ97fjBfBiO5M8AEISAV7eZidIl3iaJJHkQbBYiiW2gikreUZKPUX0HmlnIqqQcBJhWKRu6Nqk6aZBTETLLpGrvF9OArV1JSsbdw/ZH+P88RAt5em5/gjwwtFlNHyiKG5w+UFpaZOK3gZP0su0sa6dlPeQ9EL4JlFkGqQCgSQ+NOsXqaOavgoP5VLykLwuGnwIUnuhBTVeDbzpgrg9LuF5dYp/zs+Y9ScJqe5VMAagLSYTShNtN8luV7LvxF9pgWwZdcM7lUwqJmUddCiZqdngg3vzTactMToG16gZA4CWnMgbU4E+r541+FNMpgAZNvs2CiW/eApfaaQojsZEAHDsDv4L5n3M1CC7fYjE/d61aSng1LaO6T1mh+dEfPvLzp7zyzz+UgWMhi5Cs4pcXx1eic5r7uxPoBwcCTt3YI1jKVVnV7/w=";
    public String b64Key = "MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS9AgEAMASCBMh4EHl7aNSCaMDA1VlRoXCZ5UUmqErAbucRBAKNQXH8t1GNfLDIQejtcocS39VvWnpNXjZJeCg65Y2wI36UGn78gvnU0NOmyUkXksPVrkz7hqNtAVojPUtN65l+MVAsIRVD6OLJeKZ2bLx5z78zrx6Tp1zCGT/NpxL+CJSy5iY6TKqbJcK/9198noOvT2p8rKVqUUF3wLRvD6R/b3BC5wCon/exp3BUTZeiWJqGRRgaW4rn49ZbJPVIcDmUO8mojPesFHjJDSnA0nBnWaUvTYXi0srT+dLZOewsBR8d5GdSWh9ZkM29wJbjYHCMsXkObZjaap3YM8fU29zRyZ8KAqaCnBHCfYjbib56m+Lmnk+ScqMkQQ+S/+2pzn2LzauvBI4p/OjQgBDeblo22X7sX9OA9YaqB3q6CCjQ5tkDNrz3HOgTm+amh/kI8TEn9rcKf4Ru7mC1T7VMaFgBqpIS8YJNbcgegF0IF1FpCS05wjdU5CktYAnPnvC+Pj+MFDeH+184kIHBWqPNG6dAzALxRgtKTlGdJ1l5Do+4EWI+0mvKojREnKoDczFnDeCFnM51u3I9Vce3rkf0djRQKFomPVUnPDqxlR5lDAssYAYNcECAkvGxKcBDbjWi/6NHlwjS1r28+0Jhvfxjx9O6hi4AW82Q2/kBE5P/eOwln/jKSbLgi7Iyim1FFHxkQH1FY5kcKhAzFcIq85rGFlzHRfPF9OIQSmONI9kcWQCxkk8aG1u1zwbjZRYLTxlwmZvynOgaWRpTN8Y4ReBDIG1klhva7nqqoM416oXBG71IKaCtjAwRlE6pgaqnIz/WQAb2FR541pqynX6dB6DB1nIWnatsWZJZlu+Bnhf9DBlUsO9ZSAf9Fa9nJAzwFCzaKIsvGJIeKSZ/h+vInkjaO/rxswErVROTfZy1lO2CJ/xnAgzFGrpDxNJPliv3McO9TGwYy/zHhE6Pdo8Xu6NsMisNU6TB8Bc26uLNv/7kWhNmNnBA1qt5akln6hOHrPBXGBiTNUL0IoFVPNdCbS0834zAYXfgtZLDzVpeLqmeMpqXbIYK0/NXe9etxuOcN40O+B/fTHHmO7dMgBZ4vAApVQUPr7ilumVHsWSMRP/0p5R9q4qr1bDm9S5YCPevdyYWTSceGSrXHmjYzJLBtpc/s77mynNqZEYjhnKk2XRNp6kp/FYRu+QdsX9vaDJbLKR2EnSC4fU6UOTO03IZU15j3wOsg30QrXoKntSJ/beF99cvFHuPrQPWxCtws0lLwkkHNVOm6XNO948Moy1w1pL4i68CwmceYZaYrYhmHGdLuescFQrZQaULDWhpK2Stys8Vs/XwwxNi9MHAFSXpdy/b+Aro5n87w+0MHRcllF8ZKbtQ/ym4oG7aREuo7o71JXJQPjZKTOtVM1EQx/FLM/5brnDSoyvLtoYtv9/tTnIC+8gR6eErkzaGmn8pftPhGNuz6yzx8JeLFoMD7VWbGTefj46KS+yMweFJnpReHEqwnukXpEYq19EWVyQa/Sb7navtKt80y/vRs0aNZp3iL23AOs0u1kQ1CFNY2y12Gor1koaH2FUd5jAQnaSKmgarLy0H/QVvR2g8B3+Fh49QhKYrd8N6LvvI80cwbEoqYWn5DWA=";
    public String rfc = "LAN7008173R5";
    public String razonSocial = "CINDEMEX SA DE CV";
    public String noCertificado = "20001000000300022815";
    public String cadenaOriginal = "||3.3|HDS|3|2018-06-21T11:58:15|01|20001000000300022815|200.00|MXN|603.20|I|PUE|06300|LAN7008173R5|CINDEMEX SA DE CV|601|AAA010101AAA|SW SMARTERWEB|G03|50211503|UT421511|1|H87|Pieza|Cigarros|200.00|200.00|200.00|002|Tasa|0.160000|32.00|232.00|003|Tasa|1.600000|371.20|002|Tasa|0.160000|32.00|003|Tasa|1.600000|371.20|403.20||";
    public String sello = "RDp3wKRFxtBuIpxh1ZtunMNhYP77WiMHys7ZhWuKx7ywuntUrBnhTfAc2o7h7nRYr7zxS9GawxQLG01e3U7aTbdQOkrLRYqKa3ZyebHp8/chHcQCjYbBUzaVVMrQRgq2Z9fK9dVeoqIvWgYCL8bX1BQbRQsv5wV/WiW/41eliaxx4hGBCFr3PSIwmI0HO+VLsgFLn9hH2px11eusWXG++8rp2sPPfAEIoyO7JIVAx9GxEdAaJMUZrS41uemkTVoU3Ih2dNDMNQXNTfnlUDUptuIK1h1KSktJVrF09YS9NiansSdgj2D5eK6RXuMD7rvqSsqdMab1p3QCuhpbr1G+2g==";
    public String xmlTest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" Certificado=\"MIIFxTCCA62gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMjI4MTUwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNjEwMjUyMTUyMTFaFw0yMDEwMjUyMTUyMTFaMIGxMRowGAYDVQQDExFDSU5ERU1FWCBTQSBERSBDVjEaMBgGA1UEKRMRQ0lOREVNRVggU0EgREUgQ1YxGjAYBgNVBAoTEUNJTkRFTUVYIFNBIERFIENWMSUwIwYDVQQtExxMQU43MDA4MTczUjUgLyBGVUFCNzcwMTE3QlhBMR4wHAYDVQQFExUgLyBGVUFCNzcwMTE3TURGUk5OMDkxFDASBgNVBAsUC1BydWViYV9DRkRJMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvvCiCFDFVaYX7xdVRhp/38ULWto/LKDSZy1yrXKpaqFXqERJWF78YHKf3N5GBoXgzwFPuDX+5kvY5wtYNxx/Owu2shNZqFFh6EKsysQMeP5rz6kE1gFYenaPEUP9zj+h0bL3xR5aqoTsqGF24mKBLoiaK44pXBzGzgsxZishVJVM6XbzNJVonEUNbI25DhgWAd86f2aU3BmOH2K1RZx41dtTT56UsszJls4tPFODr/caWuZEuUvLp1M3nj7Dyu88mhD2f+1fA/g7kzcU/1tcpFXF/rIy93APvkU72jwvkrnprzs+SnG81+/F16ahuGsb2EZ88dKHwqxEkwzhMyTbQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAJ/xkL8I+fpilZP+9aO8n93+20XxVomLJjeSL+Ng2ErL2GgatpLuN5JknFBkZAhxVIgMaTS23zzk1RLtRaYvH83lBH5E+M+kEjFGp14Fne1iV2Pm3vL4jeLmzHgY1Kf5HmeVrrp4PU7WQg16VpyHaJ/eonPNiEBUjcyQ1iFfkzJmnSJvDGtfQK2TiEolDJApYv0OWdm4is9Bsfi9j6lI9/T6MNZ+/LM2L/t72Vau4r7m94JDEzaO3A0wHAtQ97fjBfBiO5M8AEISAV7eZidIl3iaJJHkQbBYiiW2gikreUZKPUX0HmlnIqqQcBJhWKRu6Nqk6aZBTETLLpGrvF9OArV1JSsbdw/ZH+P88RAt5em5/gjwwtFlNHyiKG5w+UFpaZOK3gZP0su0sa6dlPeQ9EL4JlFkGqQCgSQ+NOsXqaOavgoP5VLykLwuGnwIUnuhBTVeDbzpgrg9LuF5dYp/zs+Y9ScJqe5VMAagLSYTShNtN8luV7LvxF9pgWwZdcM7lUwqJmUddCiZqdngg3vzTactMToG16gZA4CWnMgbU4E+r541+FNMpgAZNvs2CiW/eApfaaQojsZEAHDsDv4L5n3M1CC7fYjE/d61aSng1LaO6T1mh+dEfPvLzp7zyzz+UgWMhi5Cs4pcXx1eic5r7uxPoBwcCTt3YI1jKVVnV7/w=\" Fecha=\"2018-06-21T11:58:15\" Folio=\"3\" FormaPago=\"01\" LugarExpedicion=\"06300\" MetodoPago=\"PUE\" Moneda=\"MXN\" NoCertificado=\"20001000000300022815\" Sello=\"RDp3wKRFxtBuIpxh1ZtunMNhYP77WiMHys7ZhWuKx7ywuntUrBnhTfAc2o7h7nRYr7zxS9GawxQLG01e3U7aTbdQOkrLRYqKa3ZyebHp8/chHcQCjYbBUzaVVMrQRgq2Z9fK9dVeoqIvWgYCL8bX1BQbRQsv5wV/WiW/41eliaxx4hGBCFr3PSIwmI0HO+VLsgFLn9hH2px11eusWXG++8rp2sPPfAEIoyO7JIVAx9GxEdAaJMUZrS41uemkTVoU3Ih2dNDMNQXNTfnlUDUptuIK1h1KSktJVrF09YS9NiansSdgj2D5eK6RXuMD7rvqSsqdMab1p3QCuhpbr1G+2g==\" Serie=\"HDS\" SubTotal=\"200.00\" TipoDeComprobante=\"I\" Total=\"603.20\" Version=\"3.3\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\">  <cfdi:Emisor Nombre=\"CINDEMEX SA DE CV\" RegimenFiscal=\"601\" Rfc=\"LAN7008173R5\"/>  <cfdi:Receptor Nombre=\"SW SMARTERWEB\" Rfc=\"AAA010101AAA\" UsoCFDI=\"G03\"/>  <cfdi:Conceptos>    <cfdi:Concepto Cantidad=\"1\" ClaveProdServ=\"50211503\" ClaveUnidad=\"H87\" Descripcion=\"Cigarros\" Importe=\"200.00\" NoIdentificacion=\"UT421511\" Unidad=\"Pieza\" ValorUnitario=\"200.00\">      <cfdi:Impuestos>        <cfdi:Traslados>          <cfdi:Traslado Base=\"200.00\" Importe=\"32.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\"/>          <cfdi:Traslado Base=\"232.00\" Importe=\"371.20\" Impuesto=\"003\" TasaOCuota=\"1.600000\" TipoFactor=\"Tasa\"/>        </cfdi:Traslados>      </cfdi:Impuestos>    </cfdi:Concepto>  </cfdi:Conceptos>  <cfdi:Impuestos TotalImpuestosTrasladados=\"403.20\">    <cfdi:Traslados>      <cfdi:Traslado Importe=\"32.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\"/>      <cfdi:Traslado Importe=\"371.20\" Impuesto=\"003\" TasaOCuota=\"1.600000\" TipoFactor=\"Tasa\"/>    </cfdi:Traslados>  </cfdi:Impuestos></cfdi:Comprobante>";

    public String removeWhiteSpaces(String input) {
        return input.replace("\\s+", "").replaceAll("\n", "").replaceAll("\r", "");
    }

    @Test
    public void testJavaVersion() {
        System.out.println("V");

        System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation());
        String jVersion = "1.8";
        String javaVersionFunction = Sign.JAVA_VERSION;
        Assert.assertEquals(jVersion, javaVersionFunction);
    }

    @Test
    public void testValidity_start_Cert_Get() throws Exception {

        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        Assert.assertEquals(Sign.validityStartGet(Sign.certificateGetX509(fileBytes)),"2016-10-25 16:52:11");
    }

    @Test
    public void testValidity_end_Cert_Get() throws Exception {
        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        Assert.assertEquals(Sign.validityEndGet(Sign.certificateGetX509(fileBytes)),("2020-10-25 15:52:11"));
    }

    @Test
    public void testBusiness_Name_Get() throws CertificateException, IOException {
        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        String businessName = Sign.bussinesNameGet(Sign.certificateGetX509(fileBytes));
        Assert.assertEquals(businessName,razonSocial);
    }

    @Test
    public void testNoCertificate() throws CertificateException, IOException {
        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        String certificateNumber = Sign.noCertificateGet(Sign.certificateGetX509(fileBytes));
        Assert.assertEquals(certificateNumber,noCertificado);
    }

    @Test
    public void testRfc_get() throws CertificateException, IOException {
        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        String RfcGet = Sign.rfcGet(Sign.certificateGetX509(fileBytes));
        Assert.assertEquals(RfcGet, rfc);
    }

    @Test
    public void testgetOriginalString() throws IOException, TransformerException {
        String content = new String(Files.readAllBytes(Paths.get("xml33.xml")));
        String Cadena_Original = Sign.originalStringGet(content);
        Assert.assertEquals(Cadena_Original,cadenaOriginal);
    }

    @Test
    public void testGetSign() throws GeneralSecurityException, IOException{

        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.key");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }
        String Nuevo_Sello = Sign.signGet(cadenaOriginal, fileBytes, password_csd);
        Assert.assertEquals(Nuevo_Sello,sello);
    }

    @Test
    public void testCfdiBuilder() throws IOException, GeneralSecurityException, TransformerException, ParserConfigurationException, SAXException {

        // Convertimos nuestro archivo *.cer a un arreglo de bytes (byte[])
        String filePath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        byte[] fileBytes;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
        }

        // x509
        X509Certificate x509Certificate = Sign.certificateGetX509(fileBytes);

        // Obtenemos el número del certificado
        String certificateNumber = Sign.noCertificateGet(x509Certificate);

        // Obtenemos el certificado encodeado en base64
        String Cert = javax.xml.bind.DatatypeConverter.printBase64Binary(fileBytes);

        // Obtenemos el RFC
        String RfcGet = Sign.rfcGet(Sign.certificateGetX509(fileBytes));

        // Obtenemos el nombre de la razón social
        String businessName = Sign.bussinesNameGet(Sign.certificateGetX509(fileBytes));

        ComprobanteDocument comprobanteDocument = ComprobanteDocument.Factory.newInstance();
        ComprobanteDocument.Comprobante comprobante = comprobanteDocument.addNewComprobante();
        XmlCursor cursor = comprobante.newCursor();
        QName location = new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
        cursor.setAttributeText(location, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd");

        // Setear Fecha
        SimpleDateFormat customDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String fecha = "2018-06-21T11:58:15";

        // Datos generales del comprobante
        comprobante.setVersion("3.3");
        comprobante.setSerie("HDS");
        comprobante.setFolio("3");
        comprobante.setSello("@");
        comprobante.setFecha(fecha);
        comprobante.setNoCertificado(certificateNumber);
        comprobante.setCertificado(Cert);
        comprobante.setLugarExpedicion("06300");
        comprobante.setTipoDeComprobante(CTipoDeComprobante.I);
        comprobante.setFormaPago(CFormaPago.X_01);
        comprobante.setMetodoPago(CMetodoPago.PUE);
        comprobante.setMoneda(CMoneda.MXN);
        comprobante.setSubTotal(new BigDecimal(200.00).setScale(2, BigDecimal.ROUND_UP));
        comprobante.setTotal(new BigDecimal(603.20).setScale(2, BigDecimal.ROUND_DOWN));

        //Datos del emisor
        ComprobanteDocument.Comprobante.Emisor emisor = comprobante.addNewEmisor();

        emisor.setRegimenFiscal("601");
        emisor.setNombre(businessName);
        emisor.setRfc(RfcGet);

        ComprobanteDocument.Comprobante.Receptor receptor = comprobante.addNewReceptor();
        receptor.setNombre("SW SMARTERWEB");
        receptor.setRfc("AAA010101AAA");
        receptor.setUsoCFDI("G03");

        // Creamos el nodo Conceptos
        ComprobanteDocument.Comprobante.Conceptos conceptos = comprobante.addNewConceptos();
        // creamos un Concepto
        ComprobanteDocument.Comprobante.Conceptos.Concepto concepto = conceptos.insertNewConcepto(0); // Especificamos la posición del concepto
        concepto.setCantidad(new BigDecimal(1).setScale(0, BigDecimal.ROUND_UP));
        concepto.setClaveProdServ("50211503");
        concepto.setClaveUnidad("H87");
        concepto.setDescripcion("Cigarros");
        concepto.setNoIdentificacion("UT421511");
        concepto.setUnidad("Pieza");
        concepto.setValorUnitario(new BigDecimal(200.00).setScale(2, BigDecimal.ROUND_UP));
        concepto.setImporte(new BigDecimal(200.00).setScale(2, BigDecimal.ROUND_UP));

        // Creamos el nodo Impuestos en el concepto
        ComprobanteDocument.Comprobante.Conceptos.Concepto.Impuestos impuestos = concepto.addNewImpuestos();
        // agregamos el nodo traslados
        ComprobanteDocument.Comprobante.Conceptos.Concepto.Impuestos.Traslados traslados = impuestos.addNewTraslados();
        // Agregamos el nodo traslado con sus valores
        ComprobanteDocument.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado = traslados.addNewTraslado();
        traslado.setTipoFactor(CTipoFactor.TASA);
        traslado.setTasaOCuota(String.valueOf(new BigDecimal(0.160000).setScale(6, BigDecimal.ROUND_DOWN)));
        traslado.setBase(String.valueOf(new BigDecimal(200.00).setScale(2, BigDecimal.ROUND_UP)));
        traslado.setImporte(new BigDecimal(32.00).setScale(2, BigDecimal.ROUND_UP));
        traslado.setImpuesto(CImpuesto.X_002);
        // Agregaremos el segundo impuesto
        ComprobanteDocument.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado1 = traslados.addNewTraslado();
        traslado1.setTipoFactor(CTipoFactor.TASA);
        traslado1.setBase(String.valueOf("232.00"));
        traslado1.setImporte(new BigDecimal(371.20).setScale(2, BigDecimal.ROUND_UP));
        traslado1.setImpuesto(CImpuesto.X_003);
        traslado1.setTasaOCuota(String.valueOf("1.600000"));
        traslado1.setTipoFactor(CTipoFactor.TASA);

        // Creamos el nodo Traslados
        ComprobanteDocument.Comprobante.Impuestos Nodoimpuestos = comprobante.addNewImpuestos();
        Nodoimpuestos.setTotalImpuestosTrasladados(new BigDecimal(403.20).setScale(2, BigDecimal.ROUND_UP));
        ComprobanteDocument.Comprobante.Impuestos.Traslados nodoTraslados = Nodoimpuestos.addNewTraslados();
        // Agregamos nuestro impuesto de tipo IVA
        ComprobanteDocument.Comprobante.Impuestos.Traslados.Traslado NodoTraslado = nodoTraslados.addNewTraslado();
        NodoTraslado.setImporte(new BigDecimal(32.00).setScale(2, BigDecimal.ROUND_UP));
        NodoTraslado.setTasaOCuota(String.valueOf("0.160000"));
        NodoTraslado.setImpuesto(CImpuesto.X_002);
        NodoTraslado.setTipoFactor(CTipoFactor.TASA);
        // Agregamos nuestro acumulado de impuestos de tipo IETU
        ComprobanteDocument.Comprobante.Impuestos.Traslados.Traslado NodoTraslado1 = nodoTraslados.addNewTraslado();
        NodoTraslado1.setImporte(new BigDecimal(371.20).setScale(2, BigDecimal.ROUND_UP));
        NodoTraslado1.setImpuesto(CImpuesto.X_003);
        NodoTraslado1.setTasaOCuota(String.valueOf("1.600000"));
        NodoTraslado1.setTipoFactor(CTipoFactor.TASA);

        // Agregamos el prefijo cfdi, en caso de no hacerlo nos saldrá ns
        XmlOptions xmlOptions = new XmlOptions();
        HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("http://www.sat.gob.mx/cfd/3", "cfdi");

        // Especificamos todas las opciones para salvar el XML
        xmlOptions.setSaveNamespacesFirst();
        xmlOptions.setSaveSuggestedPrefixes(namespaces);
        xmlOptions.setSavePrettyPrint();
        xmlOptions.setCharacterEncoding("UTF-8");
        xmlOptions.setSaveOuter();

        // Agregamos las opciones a nuestro XML y lo representamos por un string
        String cfdiXml = comprobante.xmlText(xmlOptions);

        String fileKeyPath = new File("").getAbsolutePath().concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.key");
        byte[] fileKeyBytes;
        try (FileInputStream fileInputStream = new FileInputStream(fileKeyPath)) {
            fileKeyBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileKeyBytes);
        }

        String Cadena_Original = Sign.originalStringGet(cfdiXml);
        String Nuevo_Sello = Sign.signGet(Cadena_Original, fileKeyBytes, password_csd);

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(cfdiXml));
            Document doc = builder.parse(is);

            Element root = doc.getDocumentElement();
            root.setAttribute("Sello", Nuevo_Sello);
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            // Leemos el XML final
            Assert.assertEquals(removeWhiteSpaces(sw.toString()), removeWhiteSpaces(xmlTest));

        } catch ( SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }

    }
    
}
