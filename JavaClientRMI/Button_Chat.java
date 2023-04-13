package JavaClientRMI;


import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import javax.swing.JButton;

/**
 *
 * @author Creonix
 */
public class Button_Chat extends JButton
{
    String nm;
    Box_Chat cb;
    Main_Client cli_main;
    public Button_Chat(String name,Main_Client cm) throws RemoteException, FileNotFoundException {
            super(name);
            this.nm=name;
            cb=new Box_Chat(name,cm);
            this.cli_main=cm;
    }
    Box_Chat getBoxChat()
    {
            return cb;
    }
    @Override
    public String getName()
    {
            return nm;
    }
    public void nullify()
    {
            nm=null;
            cb=null;
    }
}