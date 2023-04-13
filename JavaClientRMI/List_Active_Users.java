package JavaClientRMI;
import javax.swing.*;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class List_Active_Users extends JFrame implements ActionListener 
{
    private List<Button_Chat> userList=new ArrayList<Button_Chat>();
    private Container cp;
    private Main_Client mc;
    private String user_name;
    public List_Active_Users(String user_name,Main_Client mc) throws RemoteException, FileNotFoundException
    {
        super("ChatOn Message App");
        this.mc=mc;
        this.user_name=user_name;
        cp=getContentPane();
        cp.setLayout(new FlowLayout());
        this.setSize(300,600);
        this.setVisible(true);
        this.setMaximizedBounds(null);
        this.setResizable(false);
        JLabel label = new JLabel("Welcome:  "+user_name+"       ");
        label.setBounds(10, 50, 50, 50);
        cp.add(label);
        this.activeUserButtonAdd("logout",90,25);
        JLabel label2 = new JLabel("Online Users List:");
        label2.setBounds(50, 50, 50, 50);
        cp.add(label2);
        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                signoutMethod();
                dispose();
                //System.exit(0); //calling the method is a must
            }
        });
    }
    public synchronized void activeUserButtonAdd(String client_name,int length,int breadth) throws FileNotFoundException, RemoteException
    {
        Button_Chat button =new Button_Chat(client_name, mc);
        button.setPreferredSize(new Dimension(length,breadth));
        cp.add(button);
        button.setActionCommand(client_name);
        button.addActionListener(this);
        if(!client_name.equals("logout"))
            userList.add(button);
        //System.out.println("add button"+name);
        //this.repaint();
        String sender=mc.signin.userNameGet();
        File f = new File("./"+sender+"/"+client_name+"_pub.key");
        if(!f.exists()) { 
            FileRepresentaion fr=(FileRepresentaion) mc.server_chat.keyFileDownload(client_name);
            fr.writeBackFile(new FileOutputStream(f));
        }
    }
    public synchronized void activeUserButtonRemove(String name)
    {
        Button_Chat toberemoved=null;
        for(Button_Chat cb : userList ) 
        { 
            if(cb.getName().equals(name))
            {
                cb.getBoxChat().setMessageHistorytextPlain(name + " is ofline now",name);
                cb.getBoxChat().all_Inactive();
                cb.nullify();
                toberemoved=cb;
                //userList.remove(cb);
                //this.remove(cb);
                //this.repaint();
            }
        }
        if(toberemoved!=null){
            userList.remove(toberemoved);
            this.remove(toberemoved);
            this.repaint();
        }
    }
    public void actionPerformed(ActionEvent e) {
        String actionName=e.getActionCommand();
        if(actionName.equals("logout"))
        {
            //System.out.println(e.getActionCommand());
            signoutMethod();
            //System.exit(0);
            //System.exit(0);
        }
        else
        {
            for(Button_Chat cb : userList ) 
            { 
                if(cb.getName().equals(actionName))
                {
                        Box_Chat c=cb.getBoxChat();
                        c.setVisible(true);
                }
            }

        }

    }
    public void putMessage(String message_text, String sender){
        for(Button_Chat cb : userList ) 
        { 
            if(cb.getName().equals(sender))
            {
                Box_Chat c=cb.getBoxChat();
                if(c.isValid()==false)
                {
                    c.setVisible(true);
                    c.toFront ( );
                    c.setMessageHistoryText(message_text, sender);
                }
                else
                {
                    c.setMessageHistoryText(message_text, sender);
                }
            }
        }
    }

    private void signoutMethod() {
        try {
            for(Button_Chat cb : userList ) 
            { 
                cb.getBoxChat().setVisible(false);
                cb.getBoxChat().dispose();
            }
            //ChatApp.getChat().logOut();
            mc.signout_initiation(this.user_name);
        } catch (RemoteException ex) {
            System.out.println("RemoteException");
        }
    }

    void putPlainTextMessage(String msg, String sender) {
        for(Button_Chat cb : userList ) 
        { 
            if(cb.getName().equals(sender))
            {
                Box_Chat c=cb.getBoxChat();
                if(c.isValid()==false)
                {
                    c.setVisible(true);
                    c.toFront ( );
                    c.setMessageHistorytextPlain(msg, sender);
                }
                else
                {
                    c.setMessageHistoryText(msg, sender);
                }
            }
        }
    }
}

