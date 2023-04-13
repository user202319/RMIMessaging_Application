package JavaClientRMI;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserSigninPage extends JFrame implements ActionListener
{
    private JButton login;
    private JLabel label_user_name,label_password;
    private JTextField  input_user_name,input_password;
    private String userName;
    private Main_Client client_app_handle;
    UserSigninPage(Main_Client client_app_handle)
    {
        super("Chat On LOGIN FORM");
        this.client_app_handle=client_app_handle;
        label_user_name = new JLabel();
        label_user_name.setText("User ID:");
        input_user_name = new JTextField(16);
        input_user_name.setText("user");

        label_password = new JLabel();
        label_password.setText("Passcode:");
        input_password = new JPasswordField(16);
        this.setLayout(null);
        input_password.setText("12");
        login=new JButton("Login");
        this.add(label_user_name);
        this.add(input_user_name);
        this.add(label_password);
        this.add(input_password);
        this.add(login);
        this.setMaximizedBounds(null);
        this.setResizable(false);

        label_user_name.setBounds(26,141,201,41);
        input_user_name.setBounds(26,182,242,26);
        label_password.setBounds(26,202,202,42);
        input_password.setBounds(26,241,241,26);

        login.setBounds(101,301,101,41);

        login.addActionListener(this);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                System.exit(0); 
            }
        });
    }
   public void actionPerformed(ActionEvent ap)
    {

        String givenUserName=input_user_name.getText();
        String password=input_password.getText();
        try {
            if (client_app_handle.signin_initiate(givenUserName, password))
            {
                JOptionPane.showMessageDialog(this,"Login Succesful","Success",JOptionPane.INFORMATION_MESSAGE);
                dirsandkeysCreation(givenUserName);
                client_app_handle.chatListDisplay(givenUserName);
            }
            else{
                System.out.println("enter the valid username and password");
                JOptionPane.showMessageDialog(this,"Incorrect login or Already logedin","Error",JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("RemoteException");
         }
         catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
        }
    }
    public void userNameSet(String name) {
            userName=name;
    }

    public String userNameGet(){
            return userName;
    }

    private void dirsandkeysCreation(String userName) throws RemoteException {
        //create a root dir for the user
        File directory = new File("./"+userName);
        if (! directory.exists()){
            directory.mkdir();
        }
        //create dir for shared files
        directory = new File("./"+userName+"/shared_files");
        if (! directory.exists()){
            directory.mkdir();
        }
        String public_key="./"+userName+"/"+userName+"_pub.key";
        String pri_key="./"+userName+"/"+userName+"_private.key";
        SecurityPackage.EncryptWithRSA.RSAKeyPairGeneration(public_key,pri_key );
        FileRepresentaion key_file_obj = new FileRepresentaion(public_key, false);
        key_file_obj.readFile();
        client_app_handle.server_chat.keyFileUpload(key_file_obj, userName+"_pub.key");
    }
}