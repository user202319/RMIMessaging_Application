package JavaClientRMI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import SecurityPackage.Message_Component;
import SecurityPackage.Message_Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Box_Chat extends JFrame implements ActionListener 
{
    private static final long serialVersionUID = 1L;
    private JTextArea msgHist;
    private JTextArea msgTxt;
    private JButton buttonBrows;
    private JButton buttonSendText;

    private JScrollPane scrollHistory;
    private JScrollPane scrollText;
    private String name;
    Main_Client mc;
    public Box_Chat (String name,Main_Client cm) throws RemoteException, FileNotFoundException {
        super(name);
        this.name=name;
        this.setLayout(null);
        this.mc=cm;
        
        msgHist = new JTextArea();
        msgTxt=new JTextArea();
        buttonSendText = new JButton("Send");
        buttonBrows = new JButton("Send File");
        scrollText=new JScrollPane(msgTxt);

        msgHist.setLineWrap(true);
        msgHist.setEditable(false);
        scrollHistory=new JScrollPane(msgHist);
        scrollHistory.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollHistory.setBounds(3,3,388,200);

        msgTxt.setLineWrap(true);
        scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollText.setBounds(3,205,388,65);

        buttonSendText.setBounds(3,275,190,65);
        buttonBrows.setBounds(198,275,190,65);

        //this.add(messageHistory);
        this.add(scrollHistory);

        this.add(scrollText);
        this.add(buttonSendText);
        this.add(buttonBrows);
        
        buttonSendText.addActionListener(this);
        buttonBrows.addActionListener(this);
        this.setMaximizedBounds(null);
        this.setResizable(false);
        this.setSize(401, 376);
        this.setLocation(201, 101);
        //this.setVisible(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        
    }

    public void actionPerformed(ActionEvent e) {
    Chooser ch;
    if(e.getSource()==buttonBrows)
    {
        ch = new Chooser();
        if(ch!=null)
        {
            FileRepresentaion packet = new FileRepresentaion(ch.getFilePath());
            String sender=mc.signin.userNameGet();
            packet.readFileEnc(name, sender);
//            try {
//                
//                cm.sendFile(packet,ch.getFileName(), name, sender);
//                messageHistory.append(ch.getFileName()+" Transferd to "+name+" succesfully\n");
//                //cm.sendMessage("You have received a file named " + ch.getFileName() +" from me. \n", name, sender);
//                System.out.println("send file initiated");
//            } catch (RemoteException e1) {
//                    e1.printStackTrace();
//            }
//            finally{
//                    ch=null;
//            }
        }
    }
    else
    {
        try {
            String sender=mc.signin.userNameGet();
            msgHist.append("You: "+msgTxt.getText()+"\n");
            Message_Component m1=new Message_Component(msgTxt.getText(),name, sender);
            mc.sendMessage(m1.encodedMessageBuild(), name, sender);
              
        } catch (RemoteException ex) {
                ex.printStackTrace();
        }
        msgTxt.setText("");
        }
    }

    public void setMessageHistoryText(String msg, String sender)
    {
        msg=Message_Component.decodedMessageBuilt(msg);
        msg=sender+": "+msg;
        msgHist.append(msg+"\n");
    }
    
    public void setMessageHistorytextPlain(String msg, String sender)
    {
        msg=sender+": "+msg;
        msgHist.append(msg+"\n");
    }
    
    public void all_Inactive()
    {
            msgTxt.setEditable(false);
            buttonSendText.setEnabled(false);
            buttonBrows.setEnabled(false);
    }
}

class Chooser {
    private JFileChooser chooser;
    private String fileName;
    private String filePath;

    public Chooser() {
        chooser = new JFileChooser();
        int r = chooser.showOpenDialog(new JFrame());
        if (r == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getName();
            filePath = chooser.getSelectedFile().getPath();
        }
    }
    public String getFileName()
    {
            return fileName;
    }
    public String getFilePath()
    {
            return filePath;
    }
}
