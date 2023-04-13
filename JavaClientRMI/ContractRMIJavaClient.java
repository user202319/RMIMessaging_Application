/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaClientRMI;

import java.rmi.*;

/**
 *
 * @author 
 */
public interface ContractRMIJavaClient extends Remote{
    public void messageDrop(String msg_string,String sender_client_name) throws RemoteException;
    public void activeUserAdd(String name) throws RemoteException;
    public void activeUserRemove(String name) throws RemoteException;
//    public void fileDrop(FileRepresentaion pckt, String name_file, String recpient_name, String sender_name);

}
