package com.example.simon.androidnetworktesting;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Security;

import org.spongycastle.asn1.x509.BasicConstraints;
import org.spongycastle.asn1.x509.Extension;
import org.spongycastle.asn1.x509.X509Extension;
import org.spongycastle.cert.CertIOException;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.x509.extension.SubjectKeyIdentifierStructure;


public class SSLUtils {
    public static final String ADLCEAR_SIG_ALG = "SHA256withRSA";
    public static final String KP_ALG_RSA = "RSA";
    public static final int RSA_KEY_SIZE = 2048;

    private static final Logger LOG = Logger.getLogger(SSLUtils.class);

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
    public static KeyPair generateKeyPair()  {
        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance(KP_ALG_RSA, "SC");
            kpGen.initialize(RSA_KEY_SIZE);

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            KeyPair keyPair = kpGen.generateKeyPair();

            byte[] public_key = messageDigest.digest(keyPair.getPublic().getEncoded());
            byte[] private_key = messageDigest.digest(keyPair.getPrivate().getEncoded());

            LOG.debug("get public key md5:" + new String(public_key));
            LOG.debug("get private key md5:" + new String(private_key));

            return keyPair;
        }catch (Exception e){
            LOG.error("generateKeyPair with error",e);
        }


        return null;
    }
}
