package com.rsa;

import java.io.PrintStream;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by angcyo on 2016-01-11.
 */
public class RsaClass {
    public static final PrintStream o = System.out;

    public static void demo(String string) {
        String PUBLIC_KEY_VALUE = "<RSAKeyValue><Modulus>wVwBKuePO3ZZbZ//gqaNuUNyaPHbS3e2v5iDHMFRfYHS/bFw+79GwNUiJ+wXgpA7SSBRhKdLhTuxMvCn1aZNlXaMXIOPG1AouUMMfr6kEpFf/V0wLv6NCHGvBUK0l7O+2fxn3bR1SkHM1jWvLPMzSMBZLCOBPRRZ5FjHAy8d378=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>";
        RSAPublicKey publicKey = (RSAPublicKey) RsaHelper.decodePublicKeyFromXml(PUBLIC_KEY_VALUE);

        String value = RsaHelper.encryptDataFromStr(string, publicKey);
        o.println(string + "-->" + value);
        o.println(string + "-->" + MD5Security.getMd5_32_UP(string));
    }
}
