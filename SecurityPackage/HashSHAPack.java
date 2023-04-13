/*
 * This class will be used to show how hashing works. The SHA hashing algorithm is implemented using this class.
 */
// created a Java package named SecurityPackage
package SecurityPackage;

// no such algorithm exception
import java.security.NoSuchAlgorithmException;
//message digest library for hash calculation
import java.security.MessageDigest;
//standardcharset library for defining character set
import java.nio.charset.StandardCharsets;
//biginteger library for storing the hash value
import java.math.BigInteger;

/**
 *
 * @author User
 */
//HashSHAPack class definiton begins here
public class HashSHAPack {
    // this method will generate a hash for the input string
    public static byte[] sHA256Generation(String inputString)
    {
        MessageDigest digest256=null;
        try {
            digest256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException");
        }
        //hash value comouted
        return digest256.digest(inputString.getBytes(StandardCharsets.UTF_8));
    }
    // function to convert hash
    public static String toHex(byte[] hashString)
    {
        BigInteger num = new BigInteger(1, hashString);
        //build the hex string for hash
        StringBuilder StringHex = new StringBuilder(num.toString(16));
        while (StringHex.length() < 64)
        {
            StringHex.insert(0, '0');
        }
        return StringHex.toString();
    }
    // function for hash verification
    public static boolean hashVerify(String inputString, String hexHash256){
         //comparing previous and recomputed hash value
        return (toHex(sHA256Generation(inputString)).equals(hexHash256));
    }
    //main function
    public static void main(String args[])
    {
        //inputed text mesage
        String msg = "Hi This is for Testing";
        //hex conversion
        String SHA256Hash= toHex(sHA256Generation(msg));
        System.out.println("Hash value for for \""+msg + "\": "+SHA256Hash);
        //hash lenght printed
        System.out.println("Length of hash:"+SHA256Hash.length());
        //verification of hash
        System.out.println("Verified:"+ hashVerify(msg, SHA256Hash));
    }
}
