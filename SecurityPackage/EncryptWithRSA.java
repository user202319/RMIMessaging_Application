/*
 * The Class EncryptWithRSA is defined to show the working of RSA cryptography algorithm 
  This is a asymmetric key cryptography algorithm that encrypts a plain text and  decrypts 
  it using two keys private and public.
 *
 */
// created a Java package named SecurityPackage
package SecurityPackage;
// important libraries imported for the program

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.EncodedKeySpec;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author 
 */
// the class EncryptWithRSA definition begins here
public class EncryptWithRSA {
    // function RSAKeyPairGeneration defined for generation of two keys for encryption
    public static void RSAKeyPairGeneration(String PublicKeyFile, String PrivateKeyFile){
       
        File KeyFilepublic=new File(PublicKeyFile);
        File KeyFileprivate=new File(PrivateKeyFile);
        // checking is done to see if the keys exist or not
        if (! (KeyFilepublic.exists() && KeyFileprivate.exists() )){
            KeyPairGenerator generator=null;
             // if they do not exist key is created
            try {
                generator = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithmException");
            }
            generator.initialize(2048);
            KeyPair keypair = generator.generateKeyPair();

            PrivateKey Keyprivate = keypair.getPrivate();
            PublicKey Keypublic = keypair.getPublic();

            try (FileOutputStream fos = new FileOutputStream(PublicKeyFile)) {
                fos.write(Keypublic.getEncoded());
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFoundException");
            } catch (IOException e) {
                System.out.println("IOException");
            }

            try (FileOutputStream fos = new FileOutputStream(PrivateKeyFile)) {
                fos.write(Keyprivate.getEncoded());
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException");
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
        else{
            // if keys exists then message is printed
            System.out.println("The key existst");
        }
    }
    
    // fucntion for encryption using RSA defined
    public static String messageEncryption(String publicKey, String message){
        String cipherText=null;
        File KeyFilepublic = new File(publicKey);
        byte[] BytespublicKey = null;
        try {
            BytespublicKey = Files.readAllBytes(KeyFilepublic.toPath());
        } catch (IOException e) {
            System.out.println("IOException");
        }
        
        KeyFactory keyFactory = null;
        // exception handling for all encryption exceptions
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        }
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(BytespublicKey);
        PublicKey publicKey1=null;
        try {
            publicKey1 = keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            System.out.println("InvalidKeySpecException");
        }
        Cipher CipherEncryption=null;
        try {
            CipherEncryption = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        } catch (NoSuchPaddingException e) {
            System.out.println("NoSuchPaddingException");
        }
        try {
            CipherEncryption.init(Cipher.ENCRYPT_MODE, publicKey1);
        } catch (InvalidKeyException e) {
            System.out.println("InvalidKeySpecException");
        }
        byte[] BytesSecretMessage = message.getBytes(StandardCharsets.UTF_8);
        byte[] MessageBytesEncryption=null;
        try {
            MessageBytesEncryption = CipherEncryption.doFinal(BytesSecretMessage);
        } catch (IllegalBlockSizeException e) {
            System.out.println("IllegalBlockSizeException");
        } catch (BadPaddingException e) {
            System.out.println("BadPaddingException");
        }
        cipherText = Base64.getEncoder().encodeToString(MessageBytesEncryption);
        return cipherText;
    }
    // function for decryption with RSA defined
    public static String messageDecryption(String privateKeyFileName, String encodedMessage){
        // exception handling for all decryption exceptions
        try {
            File KeyFileprivate = new File(privateKeyFileName);
            byte[] BytesKeyPrivate = Files.readAllBytes(KeyFileprivate.toPath());
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(BytesKeyPrivate);
            
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(BytesKeyPrivate);
            RSAPrivateKey KeyPrivate = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
            
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, KeyPrivate);
            
            byte[] MessageBytesEncryption=Base64.getDecoder().decode(encodedMessage);
            byte[] MessageBytesDecrypted = decryptCipher.doFinal(MessageBytesEncryption);
            String Messagedecrypted = new String(MessageBytesDecrypted, StandardCharsets.UTF_8);
            return Messagedecrypted;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException");
        }
        catch (IOException e) {
            System.out.println("IOException");
        } catch (InvalidKeySpecException e) {
            System.out.println("InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("NoSuchPaddingException");
        } catch (InvalidKeyException e) {
            System.out.println("InvalidKeySpecException");
        } catch (IllegalBlockSizeException ex) {
            System.out.println("IllegalBlockSizeException");
        } catch (BadPaddingException ex) {
           System.out.println("BadPaddingException");
        }
        
        return null;
    }
    //main function defined
    public static void main(String[] arguments) throws InvalidKeySpecException,  FileNotFoundException, NoSuchAlgorithmException,IOException,  NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException, BadPaddingException {
        //input in text form taken
        String Inputmessage="Hi I am Testing.";
        System.out.println("Inputted Message: "+Inputmessage);
        //message is encrypted
        EncryptWithRSA.RSAKeyPairGeneration("user2_pub.key", "user2_private.key");
        String encoded_text=EncryptWithRSA.messageEncryption("./user2/user2_pub.key", Inputmessage);
        System.out.println("Encrypted message: "+encoded_text);
        //message is decrypted
        String text_decrypted=EncryptWithRSA.messageDecryption("./user2/user2_private.key", encoded_text);
        System.out.println("Decrypted Message: "+text_decrypted);
    }
}
