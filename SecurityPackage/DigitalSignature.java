/**
 * A class for creating and verifying digital signatures using the SHA256withRSA algorithm.
 * The class includes methods for creating digital signatures and verifying them.
 */
// created a Java package named SecurityPackage
package SecurityPackage;

import java.util.Base64;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.EncodedKeySpec;
import java.security.SignatureException;
import java.security.Signature;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;
import java.security.KeyFactory;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;


/**
 *
 * @author User
 */
public class DigitalSignature {
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";  
    private static final String ALGO_RSA = "RSA"; 
    
    public static String DigitalSignatureCreation(String msg,String KeyFileNameprivate ) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException
    {  
        byte[] msg_input=msg.getBytes();
        PrivateKey Key=null;
        try {
            File KeyFileprivate = new File(KeyFileNameprivate);
            byte[] KeyBytesprivate = Files.readAllBytes(KeyFileprivate.toPath());
            
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(KeyBytesprivate);
            
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(KeyBytesprivate);
            Key = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        }
        catch (IOException e) {
            System.out.println("IOException");
        } catch (InvalidKeySpecException e) {
            System.out.println("InvalidKeySpecException");
        } 
        
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);  
        sign.initSign(Key);  
        sign.update(msg_input);  
        return Base64.getEncoder().encodeToString(sign.sign());  
    } 
    
    public static boolean DigitalSignatureVerification(String msg, String signature, String KeyPublic) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException  
    {  
        byte[] msg_input= msg.getBytes();
        byte[] signatureToVerify = Base64.getDecoder().decode(signature);
        PublicKey key=null;
        File Keyfilepublic = new File(KeyPublic);
        byte[] keybytespublic = null;
        try {
            keybytespublic = Files.readAllBytes(Keyfilepublic.toPath());
        } catch (IOException e) {
            System.out.println("IOException");
        }
        
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        }
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keybytespublic);
        try {
            key = keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException e) {
            System.out.println("NoSuchAlgorithmException");
        }
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);  
        sign.initVerify(key);  
        sign.update(msg_input);  
        return sign.verify(signatureToVerify);  
    }  
    
    public static void main(String arguments[]) throws InvalidKeyException,NoSuchAlgorithmException, SignatureException 
    {  
        // input message to be signed 
        String msg_input = "Hi This is Testing";   
        // generates digital signature using the message and private key file
        String sign = DigitalSignatureCreation(msg_input, "./user1/user1_private.key"); 
        // prints the inputted message to console
        System.out.println("Inputted Message " + msg_input);
        // prints the value of the digital signature to console
        System.out.println("Value of Digital Signature:\n " + sign);  
        // prints the length of the digital signature to console
        System.out.println("Length of Sign:" + sign.length()); 
        // verifies the digital signature using the message, digital signature, and public key file and prints the result to console
        System.out.println("Verify: "+ DigitalSignatureVerification(msg_input, sign, "./user1/user1_pub.key"));  
    }  
}
