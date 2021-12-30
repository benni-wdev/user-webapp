package com.wwt.webapp.userwebapp.security;

import com.wwt.webapp.userwebapp.helper.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

class Keys {

    private static final Logger logger = LoggerFactory.getLogger(Keys.class);


    static boolean isTestContext = false;

    public static KeyPair getKeyPair() {
        if(keyPair == null) {
            initKeyPair();
        }
        return keyPair;
    }

    static void setKeyPair(KeyPair keyPair) {
        Keys.keyPair = keyPair;
    }

    private static KeyPair keyPair;

    private static void initKeyPair() {
        try {
            byte[] privKeyBytes;
            if(isTestContext) {
                privKeyBytes = loadPEMTest();
            }
            else {
                privKeyBytes = loadPEM( ConfigProvider.getConfigValue("pathToPrivKey"));
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            InputStream is;
            if(isTestContext) {
                is = loadCertTest();
            }
            else {
                is = new FileInputStream(ConfigProvider.getConfigValue("pathToCert"));
            }
            X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
            PublicKey pubKey = cer.getPublicKey();

            // decode private key
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
            RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
            keyPair = new KeyPair(pubKey,privKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | CertificateException e) {
            logger.error("",e);
        }
    }

    private static byte[] loadPEM(String resource) throws IOException {
        URL url = new File(resource).toURI().toURL();
        InputStream in = url.openStream();
        String pem = new String(readAllBytes(in));
        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        String encoded = parse.matcher(pem).replaceFirst("$1");
        return Base64.getMimeDecoder().decode(encoded);
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        @SuppressWarnings("SpellCheckingInspection") ByteArrayOutputStream baos= new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int read=0; read != -1; read = in.read(buf)) { baos.write(buf, 0, read); }
        return baos.toByteArray();
    }

    private static InputStream loadCertTest() {
        String cert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDoTCCAomgAwIBAgIJAJDpyzPJtLTEMA0GCSqGSIb3DQEBCwUAMGcxCzAJBgNV\n" +
                "BAYTAkNIMQswCQYDVQQIDAJDSDELMAkGA1UEBwwCWkgxDDAKBgNVBAoMA3d3dDEM\n" +
                "MAoGA1UECwwDd3d0MRAwDgYDVQQDDAd0ZXN0MTIzMRAwDgYJKoZIhvcNAQkBFgFk\n" +
                "MB4XDTE4MTAyMDE3MjI0NVoXDTI4MTAxNzE3MjI0NVowZzELMAkGA1UEBhMCQ0gx\n" +
                "CzAJBgNVBAgMAkNIMQswCQYDVQQHDAJaSDEMMAoGA1UECgwDd3d0MQwwCgYDVQQL\n" +
                "DAN3d3QxEDAOBgNVBAMMB3Rlc3QxMjMxEDAOBgkqhkiG9w0BCQEWAWQwggEiMA0G\n" +
                "CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDBtjm0ym/WPEkkeoqRofhNfv4wm3sh\n" +
                "yCYfCXauaWbz+Bad632fzaLOrF5KC8Wc+MD/VphZXcJlTOOcTDlr84Lkv3s3nMr4\n" +
                "zOkhV8+7uwc5vQcuLrSb0HSRfivLLCU4ygZ0Oqk21ujJEaA6dYIFylIZjoR5RQuy\n" +
                "Abj1n1xgJpZOQdKKPvoNuChD6E8McLQ8R3tI05G+IUtGQHq74A30rr7lcuT+HuSv\n" +
                "Pa7LNcimJ+5D2CYSYDXrEmc6t3BD2NImi2wRPlUTIgk8aADkfvg5AVfqd2RwPjrk\n" +
                "w1ruydtP81UeQaIfVK/psUGv2Ad+wd3niaMcK3Msam+M/QkMhswftxufAgMBAAGj\n" +
                "UDBOMB0GA1UdDgQWBBQ66ACopX67FaDOGySo1Pp4bioDqDAfBgNVHSMEGDAWgBQ6\n" +
                "6ACopX67FaDOGySo1Pp4bioDqDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUA\n" +
                "A4IBAQDA2XEhWuXXkAFtUK6YFeUbLXW2AzHktIzxh5vwH+sILOD/p3HhtLJlqjZe\n" +
                "sbl1ZdrCStAHk3jFxisU2n8kLv+L9xjsKGbSi+b8rKTq7EV2jcqwlBa/2FNlSrCX\n" +
                "7AwuchzEVhfwzZZ4UkslYeHSInUwhOLf+fQlOgXn3RijX2xs7jfY7nxpZ3+sTkf3\n" +
                "DbNuSTqTdekNM07KyspEk2f2nIn4cdEh5eF8OTwy4TQFIKQ42u5PnvRGKzEIZJ0D\n" +
                "tR+M/8MwZgv3dx3yvj2maKngK0BRCboj1S2SpkBDdUHTqLY8YC7VbfeXyivJN/pN\n" +
                "sP5nfQQScyrd34opTfyg7et9K5Cv\n" +
                "-----END CERTIFICATE-----";
        return new ByteArrayInputStream(cert.getBytes());
    }

    private static byte[] loadPEMTest() {
        String pem = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDBtjm0ym/WPEkk\n" +
                "eoqRofhNfv4wm3shyCYfCXauaWbz+Bad632fzaLOrF5KC8Wc+MD/VphZXcJlTOOc\n" +
                "TDlr84Lkv3s3nMr4zOkhV8+7uwc5vQcuLrSb0HSRfivLLCU4ygZ0Oqk21ujJEaA6\n" +
                "dYIFylIZjoR5RQuyAbj1n1xgJpZOQdKKPvoNuChD6E8McLQ8R3tI05G+IUtGQHq7\n" +
                "4A30rr7lcuT+HuSvPa7LNcimJ+5D2CYSYDXrEmc6t3BD2NImi2wRPlUTIgk8aADk\n" +
                "fvg5AVfqd2RwPjrkw1ruydtP81UeQaIfVK/psUGv2Ad+wd3niaMcK3Msam+M/QkM\n" +
                "hswftxufAgMBAAECggEBALWleiFlP1AmwfO6EVyVBLEu54+PDPzVoaGzDx5n6knW\n" +
                "lubJH8kNXKDvE4/d0BXtUZVej4glmpfBbSJwe9wVweVdBV9V4SNDi01f+YUx0Ym8\n" +
                "DxKoa2Y9KEISjWB86f6Bu2GcbHbnOezqYVTQQffKDAi5EtJhA6GNafKx2D61kHaa\n" +
                "Mvpbm0Z2Bgr3Obj2UnVMA5vuCD7zqgkIvRvUBIS+Igfbn/wqHpkpGqDtBXljeABP\n" +
                "J8Ac/KU0lb0moiw0ulppMfr5qFjoy9hI/TkzqbXpv8179ovrSMSqHbcTca5QlOPk\n" +
                "PqGt1B4TiM1ojpSuGRD7VVVyqRM6BUs8b185/vcAQDECgYEA4I+O78HCyswkQVVo\n" +
                "+4Vf2rkh5uyMup2OwO0n+3eZRR/TTLvJj1Umfsi3AhgkTqa0juz8A7H/aNbsZ3ke\n" +
                "rzVtoMTs4uQymnfTzTrBY1J0vyFtIf9dd0j5hPmTFCwXsZwntbIEvA54H+nFFiJn\n" +
                "1tvUk4LschE/CXQeatJmH8ls3JcCgYEA3NUD55R0yCq1YioV0PwxueWjFBONVnUv\n" +
                "2k43b33tQu1T4WzrF/pte+hi6gCx2aRqb3RivTz7CYvpMkuBIJM/4Deg/e8yMlLv\n" +
                "v4hC/sn7wiQSr7+BXRrIbAqP8MKUo3XdvpBCnF+5wVSyLdHrjjiB1anmgL8LuJaj\n" +
                "2LfmSWHPsjkCgYBuWz6EZvVikkYy8VVhXh9ILvkRMijKwQlZmHKwqwrgpI5DxHl+\n" +
                "ffsoDV2aouCc4mITZzVGX6BEFnMhOq2wrxIMExrTOhAAR2kID5rgQ7JqB3RJY+S7\n" +
                "7VnqVnhITo7qUvJU4YJgJMA19r1LbmcXePsT7yBhwkX3UGBuYK2da3CbSwKBgQCZ\n" +
                "jhmn90f5RBFaUA9ZgXQGYtKyDCJ0owoCT+8mPLc1mZeBId6bjsyAo4fcVV1WDIji\n" +
                "NVjPY87fxYUDLxN8FkgNXkE5MhGCgr8pPNPxHXxNdA32XUDT/+KrlfCfJn5652Oo\n" +
                "teFLoRimgYFgcR7Nhydo7jpU/gNleII4GTAtiLEGqQKBgQCXis5visXiHyI/CIvs\n" +
                "8JYgKvrA247BLoNcGt1gnJYJZMBlVrK1nwlujzkMuEAN0aSaJwBoSoNckqDwXHg/\n" +
                "aXFmhmaA0MUr2T3e97Juv44ChYhqzrKy/1NXFjUHWOR/Ma0f0sjHbjmOlvSTtbjq\n" +
                "AdWkefzlJjaPEb7zA6zfsRhXOg==\n" +
                "-----END PRIVATE KEY-----";
        Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        String encoded = parse.matcher(pem).replaceFirst("$1");
        return Base64.getMimeDecoder().decode(encoded);
    }
}
