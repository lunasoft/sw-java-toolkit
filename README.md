# sw-java-toolkit
Toolkit para manipulación de certificados y creación de comprobante CFDI 3.3



Compatibilidad
-------------
* CFDI 3.3
* Java 1.8  

Dependencias
------------

[Apache commons SSL](http://www.java2s.com/Code/Jar/c/Downloadcommonsssljar.htm)

[Apache Xerces](https://xerces.apache.org/xerces-j/)

Documentación
------------
* [Inicio Rápido](http://developers.sw.com.mx/knowledge-base/cfdi-33/)
* [Documentación Oficial Servicios](http://developers.sw.com.mx)

Implementación
---------
La librería contara con dos servicios principales los que son la generación del archivo XML y el sellado necesario para timbrar este documento.

#### Generar archivo XML #####

Lo primero a realizar es crear una instancia del documento y añadir al primer nodo, el cual será *Comprobante* y crear un *XmlCursor*, el cual nos servirá para navegar dentro de nuestro archivo XML. Esto lo hará nuestra librería, puesto bastará con preocuparse por añadir los nodos necesarios y ponerle sus correspondientes atributos. Esto lo logramos codificando de la siguiente manera:}

```java
ComprobanteDocument comprobanteDocument = ComprobanteDocument.Factory.newInstance();
ComprobanteDocument.Comprobante comprobante = comprobanteDocument.addNewComprobante();
XmlCursor cursor = comprobante.newCursor();
```

Lo siguiente será crear un *QName* para añadir un nombre calificado como está definido en las especificaciones de un XML.

```java
QName location = new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
cursor.setAttributeText(location, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd");
```

Como siguiente paso, sería construir la fecha en como describe el siguiente formato, **yyyy-MM-ddTHH:mm:ss**, donde tenemos y = año, M = mes, d = día, una letra T, H = hora, m = minuto, s = segundo.

Una vez teniendo la fecha en este formato, tendríamos que guardarla en un String, o enviarla directamente como dato a nuestro *comprobante*.

> Durante el llenado de nuestro documento, algunos datos son extraídos de los catálogos. Estos catálogos son los siguientes:   **CTipoDeComprobante, CFormaPago, CMetodoPago, CMoneda, CTipoFactor, CImpuesto**

Ahora tendremos que definir por lo menos los atributos obligatorios del nodo *comprobante*.

```java
comprobante.setVersion("3.3");
comprobante.setSerie("HDS");
comprobante.setFolio("3");
comprobante.setFecha("2018-06-29T10:00:37");
comprobante.setLugarExpedicion("06300");
comprobante.setTipoDeComprobante(CTipoDeComprobante.I);
comprobante.setFormaPago(CFormaPago.X_01);
comprobante.setMetodoPago(CMetodoPago.PUE);
comprobante.setMoneda(CMoneda.MXN);
comprobante.setSubTotal(new BigDecimal(200.00).setScale(2, BigDecimal.ROUND_UP));
comprobante.setTotal(new BigDecimal(603.20).setScale(2, BigDecimal.ROUND_DOWN));
```

Lo siguiente a crear sería el nodo *emisor*, para eso debemos de hacerlo salir de "comprobante" que anteriormente ya había sido creada.

```java
ComprobanteDocument.Comprobante.Emisor emisor = comprobante.addNewEmisor();
```

Como siguiente paso tenemos que llenar los datos correspondientes al *emisor*.

```java
emisor.setRegimenFiscal("601");
```

Así mismo crear el nodo *receptor*.

```java
ComprobanteDocument.Comprobante.Receptor receptor = comprobante.addNewReceptor();
```

Y también como hicimos con los nodos anteriores, llenar sus datos.

```java
receptor.setNombre("SW SMARTERWEB");
receptor.setRfc("AAA010101AAA");
receptor.setUsoCFDI("G03");
```
Para el nodo de *conceptos*, lo creamos al igual que los anteriores.

```java
ComprobanteDocument.Comprobante.Conceptos conceptos = comprobante.addNewConceptos();
```

Ahora el subnodo de *concepto*.

```java
Concepto concepto = conceptos.addNewConcepto();
```

Y llenamos los datos que creamos correspondientes. Para los números utilizamos *BigDecimal*. Para que nos guarde la cantidad especificada de decimales usamos *.setScale()*.

```java
concepto.setCantidad(new BigDecimal(1).setScale(0));//Número 1
concepto.setClaveProdServ("50211503");
concepto.setClaveUnidad("H87");
concepto.setDescripcion("Cigarros");
concepto.setNoIdentificacion("UT421511");
concepto.setUnidad("Pieza");
concepto.setValorUnitario(new BigDecimal(200).setScale(2));//Número 200.00, de no usar .setScale tendríamos sólo 200
concepto.setImporte(new BigDecimal(200).setScale(2));
```

Para continuar tenemos que definir el nodo *impuestos* dentro de *concepto*.

```java
Concepto.Impuestos impuestos =  concepto.addNewImpuestos();
```

Así como el nodo de *traslados* dentro de *impuestos*.

```java
Traslados traslados = impuestos.addNewTraslados();
```

Y dentro de *traslados*, tendremos que definir un *traslado*.

```java
Traslados.Traslado traslado = traslados.addNewTraslado();
```

Ahora llenamos la información correspondiente del traslado.

```java
traslado.setTipoFactor(CTipoFactor.TASA);
traslado.setTasaOCuota(new BigDecimal(0.160000).setScale(6, BigDecimal.ROUND_DOWN));
traslado.setBase(new BigDecimal(200.00).setScale(2));
traslado.setImporte(new BigDecimal(32.00).setScale(2, BigDecimal.ROUND_DOWN));
traslado.setImpuesto(CImpuesto.X_002);
```

Procedemos y añadiremos otro nodo de tipo *traslado* con sus correspondientes datos.

```java
//Creación del nodo
Traslados.Traslado traslado1 = traslados.addNewTraslado();
//Datos del nodo
traslado1.setTipoFactor(CTipoFactor.TASA);
traslado1.setBase(new BigDecimal(232.00).setScale(2));
traslado1.setImporte(new BigDecimal(371.20).setScale(2, BigDecimal.ROUND_DOWN));
traslado1.setImpuesto(CImpuesto.X_003);
traslado1.setTasaOCuota(new BigDecimal(1.600000).setScale(6, BigDecimal.ROUND_DOWN));
traslado1.setTipoFactor(CTipoFactor.TASA);
```

Crearemos otro nodo de *impuestos*, atención, no confundir con el nodo *impuestos* que se encuentra dentro de *conceptos*, este nodo de *impuestos* se diferencia en que este contiene un valor en su etiqueta de XML llamada *TotalImpuestosTrasladados*. Así pues lo declaramos y estaría dentro de *comprobante* y le asignaremos el valor de su etiqueta.

```java
ComprobanteDocument.Comprobante.Impuestos Nodoimpuestos = comprobante.addNewImpuestos();
Nodoimpuestos.setTotalImpuestosTrasladados(new BigDecimal(403.20).setScale(2, BigDecimal.ROUND_DOWN));
```
Dentro llevará un nodo de tipo *traslado*, el cual también es diferente al que ya habíamos llenado correctamente.

```java
ComprobanteDocument.Comprobante.Impuestos.Traslados nodoTraslados =  Nodoimpuestos.addNewTraslados();
```

También necesitaremos un par de nodos de tipo *traslado*, los cuales también son diferentes a los ya declarados.

```java
//Nodo traslado
ComprobanteDocument.Comprobante.Impuestos.Traslados.Traslado NodoTraslado = nodoTraslados.addNewTraslado();
//Valores del nodo
NodoTraslado.setImporte(new BigDecimal(32.00).setScale(2));
NodoTraslado.setTasaOCuota(new BigDecimal(0.160000).setScale(6, BigDecimal.ROUND_DOWN));
NodoTraslado.setImpuesto(CImpuesto.X_002);
NodoTraslado.setTipoFactor(CTipoFactor.TASA);
//Nodo traslado
ComprobanteDocument.Comprobante.Impuestos.Traslados.Traslado NodoTraslado1 = nodoTraslados.addNewTraslado();
//Valores del nodo
NodoTraslado1.setImporte(new BigDecimal(371.20).setScale(2, BigDecimal.ROUND_DOWN));
NodoTraslado1.setImpuesto(CImpuesto.X_003);
NodoTraslado1.setTasaOCuota(new BigDecimal(1.600000).setScale(6, BigDecimal.ROUND_DOWN));
NodoTraslado1.setTipoFactor(CTipoFactor.TASA);
```

Ya para terminar, tendremos que añadir el prefijo **cfdi** para el archivo XML, de no hacerlo el prefijo será **ns**. Esto se logra a través de un objeto *xmlOptions*.

```java
org.apache.xmlbeans.XmlOptions xmlOptions = new XmlOptions();
HashMap<String, String> namespaces = new HashMap<>();
namespaces.put("http://www.sat.gob.mx/cfd/3", "cfdi");
```
Además de añadir el prefijo, especificaremos más opciones como son el encodeado.
```java
xmlOptions.setSaveNamespacesFirst();
xmlOptions.setSaveSuggestedPrefixes(namespaces);
xmlOptions.setCharacterEncoding("UTF-8");
xmlOptions.setSaveOuter();
```

#### Datos del certificado #####

Para obtener los datos correspondientes al certificado, como son razón social, RFC y N° de certificado, fecha desde cuando el certificado es válido y fecha de vigencia del certificado, necesitaremos nuestro certificado en un objeto X509.


La función a utilizar para lograr esto será *Sign.certificateGetX509(File XML)*. Esta función recibe como parámetro un objeto de tipo *File*, correspondiente a nuestro certificado. Retorna un objeto de tipo X509Certificate. Ejemplo de uso:

 ```java
X509Certificate x509Cer = Sign.certificateGetX509(new File("C:\\CSD_Pruebas_CFDI_LAN7008173R5.cer"));
```

Una vez teniendo nuestro certificado en un objeto X509Certificate, pasaremos a utilizar las funciones que corresponden al datos faltantes. Las funciones son: *Sign.bussinesNameGet(X509Certificate)*, *Sign.rfcGet(X509Certificate)*, *Sign.noCertificateGet(X509Certificate)*, *Sign.validityStartGet(X509Certificate)*, *Sign.validityEndGet(X509Certificate)*, *Sign.certificateBase64Get(X509Certificate)*. Estas funciones reciben como parámetro el certificado X509, y retornan un String correspondiente al dato solicitado. Ejemplo de uso:

 ```java
String razonSocial = Sign.bussinesNameGet(x509Cer);
String rfc = Sign.rfcGet(x509Cer);
String noCertificado = Sign.noCertificateGet(x509Cer);
String fechaEmision = Sign.validityStartGet(x509Cer);
String fechaCaducidad = Sign.validityEndGet(x509Cer);
String certB64 = Sign.certificateBase64Get(x509Cer);
```

**Cadena original**

Para obtener esta información, necesitamos utilizar la función *Sign.originalStringGet(StringDelXML)*. Esta función recibe como parámetro un String correspondiente a nuestro contenido del XML, el cual necesitamos sellar. Retorna un String separado por pipes ( | ). Ejemplo de uso:

 ```java
String cadenaOriginal = Sign.originalStringGet(new String (Files.readAllBytes(Paths.get("C:\\Out\\xml33.xml"))));
```

**.Key Base64**

Nuestro archivo .key será necesario para poder generar el sello correspondiente al archivo XML, pero éste será necesario codificado en Base64, esto se logra a través de la función *Sign.keyBase64get(StringRutaArchivoKey)*. Esta función recibe como parámetro un String con la ruta que lleva al archivo .key, y retorna un String con la interpretación binaria en Base64 del archivo .key. Ejemplo de uso:

 ```java
String keyB64 = Sign.keyBase64get("C:\\CSD_Pruebas_CFDI_LAN7008173R5.key");
 ```

**Sello**

Para generar el sello utilizaremos los datos generados anteriormente, para continuar utilizaremos la función *Sign.signGet(String CadenaOriginal, String keyB64, String passwordKey)*.
Esta función retornara un String con el sello, pero requiere de los parámetros *cadena original*, *archivo key en Base64* y la *password* del archivo .key, estos parámetros deben ir en formato String. Ejemplo de uso:

```java
String Sello = Sign.signGet(cadenaOriginal, keyB64, password);
```

Como paso final solo tendríamos que añadir los datos obtenidos a nuestro XML y guardarlo, por ejemplo:

```java
comprobante.setSello(Sello);
comprobante.setNoCertificado(noCertificado);
comprobante.setCertificado(certB64);
emisor.setNombre(razonSocial);
emisor.setRfc(rfc);
File xmlFile = new File("xml33.xml");
comprobante.save(xmlFile, xmlOptions);
```
