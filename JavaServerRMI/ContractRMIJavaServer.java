/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaServerRMI;
import java.rmi.*;
import JavaClientRMI.ContractRMIJavaClient;
import JavaClientRMI.FileRepresentaion;

/**
 *
 * @author 
 */
public interface ContractRMIJavaServer extends Remote {
    boolean signin(String client_name, String pwd) throws RemoteException;
    void message_incorporation(ContractRMIJavaClient cli,String client_name) throws RemoteException;
    void signout(String Client_Name) throws RemoteException;
    void messageSend(String msg_string,String receipient_nm,String sender_nm) throws RemoteException;

//    public void fileDrop(FileRepresentaion packet, String fileName, String name, String sender) throws RemoteException ;
    public void keyFileUpload(FileRepresentaion packet, String fileName) throws RemoteException;
    public FileRepresentaion keyFileDownload(String userName) throws RemoteException;
}
