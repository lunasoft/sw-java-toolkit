
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.sat.cfd33.CMetodoPago;
import mx.sat.cfd33.CMoneda;
import mx.sat.cfd33.CTipoDeComprobante;
import mx.sat.cfd33.CTipoFactor;
import mx.sat.cfd33.CUsoCFDI;
import mx.sat.cfd33.Comprobante;
import mx.sat.cfd33.DefaultNamespacePrefixMapper;
import mx.sat.cfd33.ObjectFactory;
import mx.sat.cfd33.Sello;
import mx.sat.cfd33.implocal.ImpuestosLocales;
import org.bouncycastle.util.encoders.Base64;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author sistemas_2
 */
public class CreaXml {
    
    /**
     * @param args the command line arguments
     */
    public void crearComprobante() throws Exception {
        XMLGregorianCalendar fecha = null;

        try {
            fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar("2018-06-12T14:34:58");
        } catch (DatatypeConfigurationException ex) {
            System.out.println(ex.getMessage());

        }

        ObjectFactory of = new ObjectFactory();
        Comprobante xml = of.createComprobante();

        xml.setVersion("3.3");
        xml.setSerie("S");
        xml.setFolio("123");
        xml.setFecha(fecha);
        xml.setFormaPago("01");
        xml.setCondicionesDePago("En una sóla exhibición");

        xml.setSubTotal(new BigDecimal("500.00"));
        //  xml.setDescuento(new BigDecimal("0.00"));
        xml.setMoneda(CMoneda.MXN);
        xml.setTipoCambio(new BigDecimal("1"));
        xml.setTotal(new BigDecimal("580.00"));
        xml.setTipoDeComprobante(CTipoDeComprobante.I);
        xml.setMetodoPago(CMetodoPago.PUE);
        xml.setLugarExpedicion("71200");

        //Este bloque es para crear el emisor
        xml.setEmisor(createEmisor(of));

        //Receptor
        xml.setReceptor(createReceptor(of));

        //Conceptos
        xml.setConceptos(createConceptos(of));

        //Impuestos totales
        xml.setImpuestos(createImpuestos(of));

        
                Comprobante.Complemento com = of.createComprobanteComplemento();
                
              mx.sat.cfd33.implocal.ObjectFactory oflocal = new mx.sat.cfd33.implocal.ObjectFactory();
                
                com.getAny().add( ImpuestoLocal(oflocal)      );
                
                
                
                List<Comprobante.Complemento> comple = xml.getComplemento();
                comple.add(  com );
                
                
        xml.setComplemento( comple );
        

        //Extraer archivos .cer y .key
        // Para este ejemplo se usaran los archivo de prueba de facturacion inteligente
        // http://www.facturainteligenteblog.com/so/4Ln-c6jK#/main
        File cer = new File("C:/FactuFacil/SAT/CertificadoFirmadoPF.cer");

        File key = new File("C:/FactuFacil/SAT/LlavePkcs8PF.key");

        //Agregar certificado y no. de certificado al comprobante, por medio del archivo .cer del contribuyente.
        X509Certificate x509Certificado = getX509Certificate(cer);
        String certificado = getCertificadoBase64(x509Certificado);
        String noCertificado = getNoCertificado(x509Certificado);
        xml.setCertificado(certificado);
        xml.setNoCertificado(noCertificado);

        //Despues de asignar los valores al xml, guardar el comprobante y realizar el sellado digital.
        String cadxml = jaxbObjectToXML(xml);

        String cadenaoriginal = "";
        PrivateKey llavePrivada = null;
        String selloDigital = "";

        try {
            cadenaoriginal = generarCadenaOriginal(cadxml);
        } catch (TransformerException ex) {
            System.out.println(ex.getMessage());

        }

        //Utilizar el archivo .key del contribuyente, ademas de la contraseña correspondiente
        llavePrivada = getPrivateKey(key, "12345678a");

        //Asignar el sello digital como texto
        selloDigital = generarSelloDigital(llavePrivada, cadenaoriginal);

        System.out.println("------YOU---------");
        System.out.println(selloDigital);
        System.out.println("---------YOU-------");
        //Agregar el sello digital al xml
        xml.setSello(selloDigital);

        String COMPROBANTE_XML = "C:\\FactuFacil\\Comprobantes\\SIN SELLO\\cfdi.xml";

        JAXBContext context = JAXBContext.newInstance(Comprobante.class);
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd");
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new DefaultNamespacePrefixMapper());
        

        
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        // Compilar, marcara un error en las clases generadas del cfdi por la codificacion, lo que hay que hacer es quitar la vocal con el acento.
        //Asi con cada archivo, hasta que no alla ningun error.
        // Despues de compilar y que no se genere ningun error vamos a crear un formulario.
        //Olvide una linea XD
        m.marshal(xml, new File(COMPROBANTE_XML));
//        
//       //Convertir el xml a string
//       
        StringWriter sw = new StringWriter();
        m.marshal(xml, sw);
        String XMLEnviar = sw.toString();

        Sello ut = new Sello();
        ut.signXML(XMLEnviar);

//       
//       //Conexion con el pack, que valida nuestro xml y si es correcto lo timbra
//       WSCFDI33 service = new WSCFDI33();
//       IWSCFDI33 port = service.getSoapHttpEndpoint();
//       RespuestaTFD33 Respuesta = port.timbrarCFDI("CFDI010233001", "Pruebas1a$", XMLEnviar,"001");
//       
//            
//       if(Respuesta.isOperacionExitosa())
//       {
//           System.out.println("Operación exitosa.");
//           String xmlResultado = Respuesta.getXMLResultado().getValue();
//           String estado = Respuesta.getTimbre().getValue().getEstado().getValue();
//           
//           //Guardar el xmlResultado en nuestro disco duro
//           saveXML(xmlResultado,"xmlTimbrado.xml","C:\\prueba\\");
//                      
//       }else
//       {
//           System.out.println("-Hubo un error en la operación.");
//           System.out.println(Respuesta.getCodigoRespuesta().getValue());
//           System.out.println(Respuesta.getMensajeErrorDetallado().getValue());        
//           
//       }
    }

    //Metodos
    private ImpuestosLocales ImpuestoLocal(mx.sat.cfd33.implocal.ObjectFactory of) {

        ImpuestosLocales ofi = of.createImpuestosLocales();
        ofi.setVersion("1.0");
        ofi.setTotaldeRetenciones(new BigDecimal("343.00"));
        ofi.setTotaldeTraslados(new BigDecimal("500.00"));
       
        
        ImpuestosLocales.RetencionesLocales rete = of.createImpuestosLocalesRetencionesLocales();

        rete.setImpLocRetenido("5 millar");
        rete.setTasadeRetencion(new BigDecimal("2.00"));
        rete.setImporte(new BigDecimal("343.00"));

        ofi.getRetencionesLocalesAndTrasladosLocales().add(rete);
        
        
         ImpuestosLocales.TrasladosLocales tras = of.createImpuestosLocalesTrasladosLocales();

        tras.setImpLocTrasladado("ISH");
        tras.setTasadeTraslado(new BigDecimal("2.00"));
        tras.setImporte(new BigDecimal("500.00"));

        ofi.getRetencionesLocalesAndTrasladosLocales().add(tras);
        
        
        
        
        
      return ofi;
                
                
                
    }

    
    private ImpuestosLocales ImpuestoLocal(ObjectFactory of) {

        ImpuestosLocales ofi = of.createImpuestosLocales();
        ofi.setVersion("1.0");
        ofi.setTotaldeRetenciones(new BigDecimal("343.00"));
        ofi.setTotaldeTraslados(new BigDecimal("500.00"));
       
        
        ImpuestosLocales.RetencionesLocales rete = of.createImpuestosLocalesRetencionesLocales();

        rete.setImpLocRetenido("5 millar");
        rete.setTasadeRetencion(new BigDecimal("2.00"));
        rete.setImporte(new BigDecimal("343.00"));

        ofi.getRetencionesLocalesAndTrasladosLocales().add(rete);
        
        
         ImpuestosLocales.TrasladosLocales tras = of.createImpuestosLocalesTrasladosLocales();

        tras.setImpLocTrasladado("ISH");
        tras.setTasadeTraslado(new BigDecimal("2.00"));
        tras.setImporte(new BigDecimal("500.00"));

        ofi.getRetencionesLocalesAndTrasladosLocales().add(tras);
        
        
        
        
      return ofi;
                
                
                
    }
    
    
    private Comprobante.Emisor createEmisor(ObjectFactory of) {
        Comprobante.Emisor emisor = of.createComprobanteEmisor();
        emisor.setRfc("TEST010203001");
        emisor.setNombre("Marcos López Esperanza");
        emisor.setRegimenFiscal("621");
        return emisor;
    }

    private Comprobante.Receptor createReceptor(ObjectFactory of) {
        Comprobante.Receptor receptor = of.createComprobanteReceptor();
        receptor.setRfc("TEST010203001");
        receptor.setNombre("Publico en General");
        receptor.setUsoCFDI(CUsoCFDI.G_01);
        return receptor;
    }

    private Comprobante.Conceptos createConceptos(ObjectFactory of) {
        Comprobante.Conceptos cps = of.createComprobanteConceptos();
        List<Comprobante.Conceptos.Concepto> list = cps.getConcepto();
        Comprobante.Conceptos.Concepto c1 = of.createComprobanteConceptosConcepto();
        c1.setImporte(new BigDecimal("500.00"));
        c1.setDescuento(new BigDecimal("0.00"));
        c1.setValorUnitario(new BigDecimal("500.00"));
        c1.setUnidad("Kilogramo");
        c1.setClaveUnidad("KGM"); //Clave de unidad proporcionada por el SAT
        c1.setCantidad(new BigDecimal("1.00"));
        c1.setClaveProdServ("50111515"); // Clave de producto y servicio del catalogo del SAT
        c1.setDescripcion("Pollo en pieza");
        c1.setNoIdentificacion("12345");
        c1.setImpuestos(createImpuestosConcepto(of));
        list.add(c1);

        return cps;

    }

    private Comprobante.Conceptos.Concepto.Impuestos createImpuestosConcepto(ObjectFactory of) {
        Comprobante.Conceptos.Concepto.Impuestos imps = of.createComprobanteConceptosConceptoImpuestos();

        //Bloque para los impuestos trasladados.
        Comprobante.Conceptos.Concepto.Impuestos.Traslados trs = of.createComprobanteConceptosConceptoImpuestosTraslados();
        List<Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado> list = trs.getTraslado();
        Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado t1 = of.createComprobanteConceptosConceptoImpuestosTrasladosTraslado();
        t1.setImporte(new BigDecimal("80.00"));
        t1.setTasaOCuota(new BigDecimal("0.160000"));
        t1.setTipoFactor(CTipoFactor.TASA);
        t1.setImpuesto("002");
        t1.setBase(new BigDecimal("500.00"));
        list.add(t1);

        //Linea olvidada =(
        imps.setTraslados(trs);
        return imps;
    }

    private Comprobante.Impuestos createImpuestos(ObjectFactory of) {
        Comprobante.Impuestos impus = of.createComprobanteImpuestos();

        impus.setTotalImpuestosTrasladados(new BigDecimal("80.00"));

        // Bloque para los impuestos transladados
        Comprobante.Impuestos.Traslados tras = of.createComprobanteImpuestosTraslados();
        List<Comprobante.Impuestos.Traslados.Traslado> list = tras.getTraslado();
        Comprobante.Impuestos.Traslados.Traslado t1 = of.createComprobanteImpuestosTrasladosTraslado();
        t1.setImporte(new BigDecimal("80.00"));
        t1.setTasaOCuota(new BigDecimal("0.160000"));
        t1.setTipoFactor(CTipoFactor.TASA);
        t1.setImpuesto("002");
        list.add(t1);

        impus.setTraslados(tras);
        return impus;
    }

    // Metodos de sellado
    private X509Certificate getX509Certificate(final File certificateFile) throws CertificateException, IOException {
        FileInputStream is = null;
        try {
            is = new FileInputStream(certificateFile);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String getCertificadoBase64(final X509Certificate cert) throws CertificateEncodingException {
        return new String(Base64.encode(cert.getEncoded()));
    }

    private String getNoCertificado(final X509Certificate cert) {
        BigInteger serial = cert.getSerialNumber();
        byte[] sArr = serial.toByteArray();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < sArr.length; i++) {
            buffer.append((char) sArr[i]);
        }
        return buffer.toString();
    }

    private PrivateKey getPrivateKey(final File keyFile, final String password) throws GeneralSecurityException, IOException {
        FileInputStream in = new FileInputStream(keyFile);
        org.apache.commons.ssl.PKCS8Key pkcs8 = new org.apache.commons.ssl.PKCS8Key(in, password.toCharArray());

        byte[] decrypted = pkcs8.getDecryptedBytes();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decrypted);
        PrivateKey pk = null;

        if (pkcs8.isDSA()) {
            pk = KeyFactory.getInstance("DSA").generatePrivate(spec);
        } else if (pkcs8.isRSA()) {
            pk = KeyFactory.getInstance("RSA").generatePrivate(spec);
        }

        pk = pkcs8.getPrivateKey();
        return pk;
    }

    private String generarSelloDigital(final PrivateKey key, String cadenaOriginal)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        cadenaOriginal = cadenaOriginal.replace("\r\n", "");
        System.out.println("------YOU- cadena original --------");
        System.out.println(cadenaOriginal);
        System.out.println("---------YOU cadena original -------");

        Signature sing = Signature.getInstance("SHA256withRSA");
        sing.initSign(key, new SecureRandom());

        try {
            sing.update(cadenaOriginal.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());

        }

        byte[] signature = sing.sign();
        return new String(Base64.encode(signature));
    }

    private String jaxbObjectToXML(Comprobante xml) throws Exception {
        String xmlString = "";

        try {
            JAXBContext context = JAXBContext.newInstance(Comprobante.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();
            m.marshal(xml, sw);
            xmlString = sw.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"------------------");
            throw new Exception(e.getMessage());
            
        }

        return xmlString;
    }

    private String generarCadenaOriginal(final String xml) throws TransformerException {
        StreamSource streamSource = new StreamSource("C:/FactuFacil/SAT/33.xslt");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer xlsTransformer = transformerFactory.newTransformer(streamSource);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        xlsTransformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(output));

        String resultado = "";

        try {
            resultado = output.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    // Metodos para el tratamiento del XML
    private String saveXML(String cadenaXML, String nombreXML, String ruta) {
        org.jdom2.Document doc = convertirCadenaADocument(cadenaXML);
        XMLOutputter xmloutput = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        xmloutput.setFormat(format);

        ruta = ruta + nombreXML;

        try {
            FileOutputStream out = new FileOutputStream(ruta);
            xmloutput.output(doc, out);
            out.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return ruta;
    }

    private org.jdom2.Document convertirCadenaADocument(String cadenaXML) {
        org.jdom2.Document documento = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            documento = builder.build(new StringReader(cadenaXML));
        } catch (JDOMException e) {
            System.out.println("Cadena XML no valida");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Cadena XML no valida");
            e.printStackTrace();
        }

        return documento;
    }

    
    
    
    
}