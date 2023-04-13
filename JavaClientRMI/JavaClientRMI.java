/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaClientRMI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import JavaServerRMI.ContractRMIJavaServer;

/**
 *
 * @author 
 */
public class JavaClientRMI extends UnicastRemoteObject implements ContractRMIJavaClient{
    private List<String> user_list = new ArrayList<>();
    private String client_name; 
    private Main_Client mc;
    public JavaClientRMI(String name, Main_Client mc)throws RemoteException {
        this.client_name=name;
        this.mc=mc;
    }
    @Override
    public void messageDrop(String msg_string, String sender_name) throws RemoteException {
        System.out.println(sender_name+" --> "+client_name+": "+msg_string);
        mc.au_list.putMessage(msg_string, sender_name);
    }

    @Override
    public synchronized void activeUserAdd(String name) throws RemoteException {
        user_list.add(name);
        try {
            mc.au_list.activeUserButtonAdd(name,280,25);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        }
        mc.au_list.pack();
        mc.au_list.setSize(300,600);
        //System.out.println("name="+name);
    }

    @Override
    public synchronized void activeUserRemove(String name) throws RemoteException {
        user_list.remove(name);
        mc.au_list.activeUserButtonRemove(name);
    }
  

//    @Override
//    public void fileDrop(FileRepresentaion pckt, String name_file, String recpient_name, String sender_name) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
 
    /*public static void main(String arguments[]) throws NotBoundException,RemoteException, MalformedURLException, InterruptedException
    {
        if(arguments.length==1 && Integer.parseInt(arguments[0])==1)
        {
            
            JavaClientRMI jcr1=new JavaClientRMI("user1");

            String link = "rmi://127.0.0.1:1070/Chat";
            Remote server = Naming.lookup(link);
            ContractRMIJavaSever serverchat=(ContractRMIJavaSever) server;
            serverchat.signin(jcr1, "user1", "123");
            while(true){
                if(jcr1.user_list.size()==1)
                    System.out.println(jcr1.user_list.size());
                    break;
            }
            int i=0;
            System.out.println("I am user1:");
            while(true){
                String message= "test message "+(++i)+": user1 --> user2";
                System.out.println("Sending message: "+message);
                serverchat.messageSend(message, "user2", "user1");
                Thread.sleep(1000);
            }
        
        }
        else{
            String url = "rmi://127.0.0.1:1070/Chat";
            Remote server = Naming.lookup(url);
            ContractRMIJavaSever serverchat=(ContractRMIJavaSever) server;
            JavaClientRMI jcr2=new JavaClientRMI("user2");
            
            serverchat.signin(jcr2, "user2", "123");
            while(true){
                if(jcr2.user_list.size()==1)
                    System.out.println(jcr2.user_list.size());
                    break;
            }
            int i=0;
            System.out.println("I am user 2");
            while(true){
                String message = "test message "+(++i)+ ": user2 --> user1";
                System.out.println("Sending message: "+message);
                serverchat.messageSend(message, "user1", "user2");
                Thread.sleep(1000);
            }
        }
    }*/

   