import Sign.*;

import java.io.IOException;

public class SW_Toolkit_Java_UT {


    public SW_Toolkit_Java_UT() throws IOException {

        String KeyBase64Val = Sign.getKeyBase64("//Resources//CSD_Pruebas_CFDI_LAN7008173R5.key");
        System.out.println(KeyBase64Val);
    }

}
