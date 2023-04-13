package JavaClientRMI;


import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import JavaServerRMI.ContractRMIJavaServer;


/**
 *
 * @author 
 */
public class Main_Client {
    ContractRMIJavaServer server_chat = null;
    JavaClientRMI jcr = null;
    UserSigninPage signin=null;
    List_Active_Users au_list=null;
    public Main_Client(ContractRMIJavaServer chatServer){
        this.server_chat=chatServer;
    }
    public boolean signin_initiate(String client_name, String pwd) throws RemoteException{
        this.jcr=new JavaClientRMI(client_name, this);
        return this.server_chat.signin(client_name, pwd);
    }
    public void signout_initiation(String userName) throws RemoteException{
        this.server_chat.signout(userName);
        this.au_list.activeUserButtonRemove(userName);
        this.au_list.setVisible(false);
        this.signin.setVisible(true);    
    }
    void chatListDisplay(String user_name) throws RemoteException, FileNotFoundException {
        this.signin.setVisible(false);
        this.signin.userNameSet(user_name);
        this.au_list=new List_Active_Users(user_name, this);
        this.au_list.setVisible(true);
        this.server_chat.message_incorporation(jcr, user_name);
    }
    void sendMessage(String msg_string, String receipient_name, String sender_client_name) throws RemoteException{
        server_chat.messageSend(msg_string, receipient_name, sender_client_name);
    }

//    void sendFile(FileRepresentaion packet, String fileName, String name, String sender) throws RemoteException {
//        System.out.println("send file client main");
//        server_chat.fileDrop(packet, fileName,  name, sender);
//    }
    public static void main(String[] args) throws NotBoundException,  RemoteException, MalformedURLException, InterruptedException {
    String url = "rmi://127.0.0.1:1070/Chat";
    Remote server = Naming.lookup(url);
    ContractRMIJavaServer chatServer=(ContractRMIJavaServer) server;
    Main_Client mc=new Main_Client(chatServer);
    mc.signin=new UserSigninPage(mc);
    mc.signin.setVisible(true);
    mc.signin.setSize(300,600);
}

}
