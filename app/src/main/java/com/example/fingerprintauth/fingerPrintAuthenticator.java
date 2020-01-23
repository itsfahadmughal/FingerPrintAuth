package com.example.fingerprintauth;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class fingerPrintAuthenticator {

    private static final String KEY_NAME = "android.fingerprint";
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private static fingerPrintAuthenticator authenticator;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fingerPrintAuthenticator(){
        initAuthentication();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initAuthentication(){
        try{
            keyStore=KeyStore.getInstance("AndroidKeyStore");
        }catch (KeyStoreException e){
            e.printStackTrace();
        }

        try {
            keyGenerator=KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public boolean cipherInit(){
        try {
           cipher=Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);

            SecretKey secretKey= (SecretKey) keyStore.getKey(KEY_NAME,null );
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);

            return true;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static fingerPrintAuthenticator getInstance(){
        if (authenticator==null)
            authenticator=new fingerPrintAuthenticator();

            return authenticator;
    }

    public Cipher getCipher() {
        return cipher;
    }
}
