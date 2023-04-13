/*
* This Message_Component class will demonstarte an input plain text message being encoded with aes, rsa, hash value
computation and digital signature and finally decoded back to original msg
*/
//the security package
package SecurityPackage;

/**
 *
 * @author 
 */
public class Message_Component {
    private String original_msg;
    private String sender;
    private String receipient;
    private String msg_hash;
    private String dig_sign;
    private String Key_AES;
    private String message_cipher;
    private String AES_Key_Cipher;
    
    public Message_Component(String plain_text_msg, String sender, String receiver){
        try{
            this.original_msg=plain_text_msg;
            this.sender=sender;
            this.receipient=receiver;
            this.msg_hash=HashSHAPack.toHex(HashSHAPack.sHA256Generation(plain_text_msg));
            this.dig_sign=DigitalSignature.DigitalSignatureCreation(plain_text_msg, "./"+sender+"/"+sender+"_private.key");
            String ret[]=EncryptWithAES.aESEncryption(msg_hash+dig_sign+plain_text_msg);
            this.Key_AES=ret[0];
            this.message_cipher=ret[1];
            this.AES_Key_Cipher=EncryptWithRSA.messageEncryption("./"+sender+"/"+receiver+"_pub.key", Key_AES);
        }
        catch(Exception ex){
            System.out.println("Message object not created");
        }
    }
    
    public String encodedMessageBuild(){
        System.out.println(AES_Key_Cipher.length());
        return sender+"<"+receipient+">"+AES_Key_Cipher+message_cipher;
    }
    public static String decodedMessageBuilt(String string_encoded){
         try {
            int mark_1=string_encoded.indexOf('<');
            int mark_2=string_encoded.indexOf('>');
            String sender=string_encoded.substring(0, mark_1);
            String receipient=string_encoded.substring(mark_1+1, mark_2);
            
            String trail_encoded= string_encoded.substring(mark_2+1);
            String AES_Key_Cipher = trail_encoded.substring(0, 344);
            String message_cipher = trail_encoded.substring(344);
            
            String Key_AES = EncryptWithRSA.messageDecryption("./"+receipient+"./"+receipient+"_private.key", AES_Key_Cipher);
            String original_msg_with_hash = EncryptWithAES.aESDecryption(Key_AES, message_cipher);
            
            String msg_hash= original_msg_with_hash.substring(0, 64);
            String dig_sign= original_msg_with_hash.substring(64, 64+344);
            String original_text = original_msg_with_hash.substring(64+344);
            
            if(!HashSHAPack.hashVerify(original_text, msg_hash)){
                throw new ExceptionofDataIntegrity();
            }
            if(!DigitalSignature.DigitalSignatureVerification(original_text, dig_sign, "./"+receipient+"./"+sender+"_pub.key")){
                throw new AutheticationError();
            }
            return original_text;
        } catch (Exception ex) {
            System.out.println("Exception ocurred");
        }
        return null;
    }
      public static void main(String[] arguments) {
        // inputted plain text message
        String message="Hello! Sending Message for Testing";
        //object created for message class with the plain text message, user 1 and 2 details
        Message_Component msg1=new Message_Component(message, "user1", "user2");
        //plain text is printed along with details
        System.out.println("User1 wants to send message to user2, plain text message is: "+message);
        //fucntion called for encoding
        String encode=msg1.encodedMessageBuild();
        //encoding successful message printed
        System.out.println("Encoded message with hash , digital siganture and details of the sender & receiver: "+encode);
        //decoded message output is printed
        System.out.println("Message decoding, integrity checking and authentication validation successful:\n Decoded message:"+decodedMessageBuilt(encode));
    }
}
