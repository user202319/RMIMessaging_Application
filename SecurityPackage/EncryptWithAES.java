/*
 * This class is defined to demonstarte the implmentation of symmetric key cryptography using AES algorithm
 */
package SecurityPackage;
//important files imported
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;


/**
 *
 * @author User
 */
public class EncryptWithAES {
    
    public static String randomPassKeyGenerator(int len)
    {
        // the limit of characters for the random key
        final String charlimit = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
       // to hold the key a stringbuilder is  created
        StringBuilder stringbuild = new StringBuilder();
        
       //SecureRandom instance created
        SecureRandom randomgen = new SecureRandom();
        
        //random characters selected for the key using loop
        
        for (int i = 0; i < len; i++)
        {
            int randomIndex = randomgen.nextInt(charlimit.length());
            // ecah new character is appended in the builder
            stringbuild.append(charlimit.charAt(randomIndex));
        }
        // the stringbuilder conversion to string
        return stringbuild.toString();
    }
    //function for encryption
    public static String[] aESEncryption(String inputtext){
        String theKey=EncryptWithAES.randomPassKeyGenerator(32);
        byte[] mainkey;
        SecretKeySpec secKey=null;
        MessageDigest sha_1 = null;
        String code_cipher=null;
        try {
          mainkey = theKey.getBytes("UTF-8");
          sha_1 = MessageDigest.getInstance("SHA-1");
          mainkey = sha_1.digest(mainkey);
          mainkey = Arrays.copyOf(mainkey, 16);
          secKey = new SecretKeySpec(mainkey, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            code_cipher = Base64.getEncoder()
              .encodeToString(cipher.doFinal(inputtext.getBytes("UTF-8")));
            } catch (Exception e) {
            System.out.println("Encryption Error: " + e.toString());
        }
        String value_returned[]={theKey, code_cipher};
        return value_returned;
    }
    //function for decryption
    public static String aESDecryption(String pass_key, String cipher_text) throws ExceptionofDecryption
    {
        String original_text="";
        byte[] mainkey;
        SecretKeySpec sec_key=null;
        MessageDigest sha_1 = null;
        try {
          mainkey = pass_key.getBytes("UTF-8");
          sha_1 = MessageDigest.getInstance("SHA-1");
          mainkey = sha_1.digest(mainkey);
          mainkey = Arrays.copyOf(mainkey, 16);
          sec_key = new SecretKeySpec(mainkey, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        
        try {
            Cipher newcipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            newcipher.init(Cipher.DECRYPT_MODE, sec_key);
            original_text = new String(newcipher.doFinal(Base64.getDecoder()
              .decode(cipher_text)));
            } 
        catch (Exception e) {
            System.out.println("Decryption Error: " + e.toString());
            throw new ExceptionofDecryption();
        }     
        return original_text;
    }
    //main function for testing
    public static void main(String[] args) {
        // input message
        String original_text="Hello, Testing is being done.";
        String decrypted_text="";
        //printed input message
        System.out.println("Original text: "+original_text);
        String value_returned[]=aESEncryption(original_text);
        System.out.println("Generated AES key randomly: "+value_returned[0]);
        System.out.println("Cipher text AES: " + value_returned[1]);
        try {
            decrypted_text=aESDecryption(value_returned[0], value_returned[1]);
            //output printed
            System.out.println("After Decryption: "+decrypted_text);
        } catch (ExceptionofDecryption ex) {
            System.out.println("Decryption failure");
        }
        if(original_text.equals(decrypted_text)){
            System.out.println("Succesful Operation ");
        }
        else{
            System.out.println("Operation failure");
        }
    }
}
