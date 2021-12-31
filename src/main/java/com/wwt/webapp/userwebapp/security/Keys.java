/* Copyright 2018-2021 Wehe Web Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            byte[] privKeyBytes = loadPEM( ConfigProvider.getConfigValue("pathToPrivKey"));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            InputStream is = new FileInputStream(ConfigProvider.getConfigValue("pathToCert"));
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


}
