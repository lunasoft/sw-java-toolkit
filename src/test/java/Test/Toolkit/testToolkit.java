package Test.Toolkit;
import Sign.Sign;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;

public class testToolkit extends TestCase{

    public String uuid = "06a46e4b-b154-4c12-bb77-f9a63ed55ff2";
    public String password_csd = "12345678a";
    public String b64Cer = "MIIFxTCCA62gAwIBAgIUMjAwMDEwMDAwMDAzMDAwMjI4MTUwDQYJKoZIhvcNAQELBQAwggFmMSAwHgYDVQQDDBdBLkMuIDIgZGUgcHJ1ZWJhcyg0MDk2KTEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMRUwEwYDVQQtEwxTQVQ5NzA3MDFOTjMxITAfBgkqhkiG9w0BCQIMElJlc3BvbnNhYmxlOiBBQ0RNQTAeFw0xNjEwMjUyMTUyMTFaFw0yMDEwMjUyMTUyMTFaMIGxMRowGAYDVQQDExFDSU5ERU1FWCBTQSBERSBDVjEaMBgGA1UEKRMRQ0lOREVNRVggU0EgREUgQ1YxGjAYBgNVBAoTEUNJTkRFTUVYIFNBIERFIENWMSUwIwYDVQQtExxMQU43MDA4MTczUjUgLyBGVUFCNzcwMTE3QlhBMR4wHAYDVQQFExUgLyBGVUFCNzcwMTE3TURGUk5OMDkxFDASBgNVBAsUC1BydWViYV9DRkRJMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvvCiCFDFVaYX7xdVRhp/38ULWto/LKDSZy1yrXKpaqFXqERJWF78YHKf3N5GBoXgzwFPuDX+5kvY5wtYNxx/Owu2shNZqFFh6EKsysQMeP5rz6kE1gFYenaPEUP9zj+h0bL3xR5aqoTsqGF24mKBLoiaK44pXBzGzgsxZishVJVM6XbzNJVonEUNbI25DhgWAd86f2aU3BmOH2K1RZx41dtTT56UsszJls4tPFODr/caWuZEuUvLp1M3nj7Dyu88mhD2f+1fA/g7kzcU/1tcpFXF/rIy93APvkU72jwvkrnprzs+SnG81+/F16ahuGsb2EZ88dKHwqxEkwzhMyTbQIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQsFAAOCAgEAJ/xkL8I+fpilZP+9aO8n93+20XxVomLJjeSL+Ng2ErL2GgatpLuN5JknFBkZAhxVIgMaTS23zzk1RLtRaYvH83lBH5E+M+kEjFGp14Fne1iV2Pm3vL4jeLmzHgY1Kf5HmeVrrp4PU7WQg16VpyHaJ/eonPNiEBUjcyQ1iFfkzJmnSJvDGtfQK2TiEolDJApYv0OWdm4is9Bsfi9j6lI9/T6MNZ+/LM2L/t72Vau4r7m94JDEzaO3A0wHAtQ97fjBfBiO5M8AEISAV7eZidIl3iaJJHkQbBYiiW2gikreUZKPUX0HmlnIqqQcBJhWKRu6Nqk6aZBTETLLpGrvF9OArV1JSsbdw/ZH+P88RAt5em5/gjwwtFlNHyiKG5w+UFpaZOK3gZP0su0sa6dlPeQ9EL4JlFkGqQCgSQ+NOsXqaOavgoP5VLykLwuGnwIUnuhBTVeDbzpgrg9LuF5dYp/zs+Y9ScJqe5VMAagLSYTShNtN8luV7LvxF9pgWwZdcM7lUwqJmUddCiZqdngg3vzTactMToG16gZA4CWnMgbU4E+r541+FNMpgAZNvs2CiW/eApfaaQojsZEAHDsDv4L5n3M1CC7fYjE/d61aSng1LaO6T1mh+dEfPvLzp7zyzz+UgWMhi5Cs4pcXx1eic5r7uxPoBwcCTt3YI1jKVVnV7/w=";
    public String b64Key = "MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS9AgEAMASCBMh4EHl7aNSCaMDA1VlRoXCZ5UUmqErAbucRBAI/QXH8t1E/fLDIQejtcocS39VvWnpNXjZJeCg65T+wI36UGn78gvnU0NOmyUkXksPVrkz7hqNtAVojPUtN65l+MVAsIRVD6OLJeKZ2bLx5z78zrx6Tp1zCGT/NpxL+CJSy5iY6TKqbJcK/9198noOvT2p8rKVqUUF3wLRvD6R/b3BC5wCon/exp3BUTZeiWJqGRRgaW4rn49ZbJPVIcDmUO8mojPesFHjJDSnA0nBnWaUvTYXi0srT+dLZOewsBR8d5GdSWh9ZP829wJbjYHCMsXkObZjaaj/YM8fU29zRyZ8KAqaCnBHCfYjbib56m+Lmnk+ScqMkQQ+S/+2pzn2LzauvBI4p/OjQgBDeblo22X7sX9OA9YaqB3q6CCjQ5tkDNrz3HOgTm+amh/kI8TEn9rcKf4Ru7mC1T7VMaFgBqpIS8YJNbcgegF0IF1FpCS05wjdU5CktYAnPnvC+Pj+MFDeH+184Pz/BWqPNG6dAzALxRgtKTlE/J1l5Dj+4EWI+0mvKojREnKoDczFnDeCFnM51u3I9Vce3rkf0djRQKFomPVUnPDqxlR5lDAssYAYNcECAkvGxKcBDbjWi/6NHlwjS1r28+0Jhvfxjx9O6hi4AW80/2/kBE5P/eOwln/jKSbLgi7Iyim1FFHxkQH1FY5kcKhAzFcIq85rGFlzHRfPF9OIQSmM/I9kcWQCxkk8aG1u1zwbjZRYLTxlwmZvynOgaWRpTN8Y4ReBDIG1klhva7nqqoM416oXBG71IKaCtjAwRlE6pP6qnIz/WQAb2FR541pqyP34/B6DB1nIWP6tsWZJZlu8/nhf9DBlUsO9ZSAf9Fa9nJAzwFCzaKIsvGJIeKSZ/h+vInkjaO/rxswErVROTfZy1lO2CJ/xnAgzFGrpDxNJPliv3McO9TGwYy/zHhE4/dj8Xu6NsMisNU6TB8Bc26uLNv/7kWhNmNnBA1qt5akln6hOHrPBXGBiTNUL0Ij9VPNdCbS0834zAYXfgtZLDzVpeLqmeMpqXbIYK0/NXe9etxuOcNz8O+B/fTHHmO7dMgBZ4vAApVQUPr7ilumVHsWSMRP/0p5R9q4qr1bDm9S5YCPevdyYWTSceGSrXHmjYzJLBtpc/s77mynNqZEYjhnKk2XRNp6kp/FYRu+QdsX9vaDJbLKR2EnSC4fU6UOTO03IZU15j3wOsg30QrXoKntSJ/beF99cvFHs/rQPWxCtws0lLwkkHNVOm6XNO9z8Moy1w1pL4i68CwmceYZaYrYhmHGdLuescFQrZQaULDWhpK2Stys8Vs/XwwxNi9MHAFSXpdy/b+Aro5n87w+0MHRcllF8ZKbtQ/ym4oG7aREuo7o71JXJQPjZKTOtVM1EQx/FLM/5brnDSoyvLtoYtv9/tTnIC+8gR6eErkzaGmn8pftPhGNuz6yzx8JeLFoMD7VWbGTefP46KS+yMweFJnpReHEqwnukXpEYq19EWVyQa/Sb7P6vtKt80y/vRs0Y/Zj/iL23AOs0u1kQ1CFNY2y12Gor1koaH2FUd5jAQP6SKmgarLy0H/QVvR2g8B3+Fhz9QhKYrd8N6LvvI80cwbEoqYWn5DWA=";
    public String rfc = "LAN7008173R5";
    
    
    public void testBase64_Key_Encode() throws Exception {

        String filePath = new File("").getAbsolutePath();
        String concatKey = filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.key");
        String Key = Sign.keyBase64get(concatKey);
        Assert.assertEquals(Key, b64Key);
    }
    
    public void testBase64_Cer_Encode() throws Exception {

        String filePath = new File("").getAbsolutePath();
        String concatCert = filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer");
        String Cert = Sign.certificateBase64Get(Sign.certificateGetX509(new File(concatCert)));
        Assert.assertEquals(Cert,b64Cer);       
    }
    
    public void testValidity_start_Cert_Get() throws Exception {

        String filePath = new File("").getAbsolutePath();
        Assert.assertEquals(Sign.validityStartGet(Sign.certificateGetX509(new File(filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer")))),("2016-10-25 16:52:11"));
    }

    public void testValidity_end_Cert_Get() throws Exception {

        String filePath = new File("").getAbsolutePath();
        Assert.assertEquals(Sign.validityEndGet(Sign.certificateGetX509(new File(filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer")))),("2020-10-25 15:52:11"));
    }
     
    public void testBusiness_Name_Get() throws CertificateException, IOException {
        String filePath = new File("").getAbsolutePath();
        String businessName = Sign.bussinesNameGet(Sign.certificateGetX509(new File(filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer"))));
        Assert.assertEquals(businessName,("CINDEMEX SA DE CV"));
    }
    
    public void testNoCertificate() throws CertificateException, IOException {
        String filePath = new File("").getAbsolutePath();
        String businessName = Sign.noCertificateGet(Sign.certificateGetX509(new File(filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer"))));
        Assert.assertEquals(businessName,("20001000000300022815"));
    }
    
    public void testRfc_get() throws CertificateException, CertificateException, IOException {
        String filePath = new File("").getAbsolutePath();
        String rfc = Sign.rfcGet(Sign.certificateGetX509(new File(filePath.concat("\\src\\test\\Resources\\CSD_Pruebas_CFDI_LAN7008173R5.cer"))));
        Assert.assertEquals(rfc,"LAN7008173R5");
    }
    
    
    
}
