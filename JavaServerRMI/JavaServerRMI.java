/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// package of server RMI
package JavaServerRMI;

//important libraries import
import JavaClientRMI.FileRepresentaion;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import JavaClientRMI.ContractRMIJavaClient;
/**
 *
 * @author
 */
public class JavaServerRMI extends UnicastRemoteObject implements ContractRMIJavaServer{
    private List<ChatClient> clients=new ArrayList<ChatClient>();
    public JavaServerRMI() throws RemoteException {}
    @Override
    public boolean signin(String client_name, String pwd) throws RemoteException{
        boolean flag_result=false;
        for(ChatClient cc: clients )
        { 
            if(cc.getName().equals(client_name)){
                return false;
            }
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection connect=DriverManager.getConnection("jdbc:mysql://localhost:3306/authserver","root","");		
            Statement statement = connect.createStatement();
            String qry ="SELECT password from login where userName = '"+client_name+"'";
            ResultSet res_set = statement.executeQuery(qry);
            if(res_set.next()==true)
            {
                String pass = res_set.getString("password");
                if(pass.equals(pwd))
                {
                    flag_result=true;
                    
                }
            }
            connect.close();
            res_set.close();
            statement.close();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        } catch (SQLException e) {
            System.out.println("SQLException");
        }
        finally{
            return flag_result;
	}
    }
    @Override
    public void signout(String Client_Name) throws RemoteException {
        for(ChatClient cc: clients )
        {
            if(cc.getName().equals(Client_Name))
            {
                clients.remove(cc);
                System.out.println("removed:" + Client_Name);
                break;
            }    
        }
        for(ChatClient cc: clients )
        {
            cc.getClientObjRef().activeUserRemove(Client_Name);
        }
    }

    @Override
    public void messageSend(String msg_string,String receipient_nm,String sender_nm) throws RemoteException {
        for(ChatClient c: clients )
        {
            if(c.getName().equals(receipient_nm))
            {
                c.getClientObjRef().messageDrop(msg_string, sender_nm);
                System.out.println(sender_nm+" --> "+receipient_nm+" : "+msg_string);
                break;
            }
        }
    }

    @Override
    public void message_incorporation(ContractRMIJavaClient cli, String client_name) throws RemoteException {
        for(ChatClient c: clients )
        { 
            try 
            {
                c.getClientObjRef().activeUserAdd(client_name);
                cli.activeUserAdd(c.getName());
            }
            catch (Exception e){
                System.out.println("Execption ocuured");
            }
        }
        clients.add(new ChatClient(cli, client_name));
        System.out.println(client_name+" login successful!");
    }

//    @Override
//    public void fileDrop(FileRepresentaion packet, String fileName, String name, String sender) throws RemoteException  {
//        if(!name.equals("")){
//            for(ChatClient c: clients ){ 
//                if(c.getName().equals(name)){
//                    //System.out.println("send file server");
//                    c.getClientObjRef().fileDrop(packet, fileName,  name, sender);
//                }
//            }
//        }
//    }

    @Override
    public void keyFileUpload(FileRepresentaion packet, String fileName) throws RemoteException {
        try{
            File directory = new File("./server");
            if (! directory.exists()){
                directory.mkdir();
            }
            //System.out.println(name+"called: client putfile");
            FileOutputStream fos = new FileOutputStream("./server/"+fileName);
            packet.writeBackFile(fos);
        } catch (Exception ex) {
            //Logger.getLogger(RMIJavaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public FileRepresentaion keyFileDownload(String userName) throws RemoteException {
        FileRepresentaion fr = new FileRepresentaion("./server/"+userName+"_pub.key", false);
        fr.readFile();
        return fr;
    }

   
    
    private class ChatClient{
        private ContractRMIJavaClient ci;
        private String name;
        public ChatClient(ContractRMIJavaClient ci, String name){
            this.ci=ci;
            this.name=name;
        }
        public ContractRMIJavaClient getClientObjRef(){
            return ci;
        }
        public String getName(){
            return name;
        }
    }
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        Registry reg = LocateRegistry.createRegistry(1070);
        String url = "rmi://127.0.0.1:1070/Chat";
        Naming.rebind(url, new JavaServerRMI());
        System.out.println("Sever started! It is Running");
    }
    
}
