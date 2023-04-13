package JavaClientRMI;
import SecurityPackage.Message_Component;
import static SecurityPackage.Message_Component.decodedMessageBuilt;
import java.io.*;
import java.util.Base64;

public class FileRepresentaion implements Serializable {
    private String name;
    // the data in my file
    //private byte[] data;
    private String base64_string;
    private boolean encFlag;
    public FileRepresentaion( String name ){
          this.name = name;
          this.encFlag=true;
    }
    public FileRepresentaion( String name, boolean encFlag){
          this.name = name;
          this.encFlag=encFlag;
    }
    public String getFileName(){
          return name;
    }
    public void readFile(){
        try{
            File file = new File( name );
            byte[] data = new byte[ (int)(file.length()) ];
            (new FileInputStream( file )).read( data );
            base64_string=Base64.getEncoder().encodeToString(data);
        }catch( Exception e ){
            //e.printStackTrace();
        }
    }	
    public void readFileEnc(String sender, String receiver){
        if(encFlag==true){
            readFile();
            Message_Component m1=new Message_Component(this.base64_string, sender, receiver);
            this.base64_string=m1.encodedMessageBuild();
        }
    }
    public void writeBackFile( OutputStream out  ){
        try{
            if(encFlag==true){
                this.base64_string=Message_Component.decodedMessageBuilt(base64_string);
            }
            byte[] temp=Base64.getDecoder().decode(base64_string);
            out.write( temp );
            out.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
}