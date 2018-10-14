
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stathis Andr
 */
public class RMIClient4 extends UnicastRemoteObject implements ClientInterface {

    public static JScrollPane scroll;
    public static JLabel images;
    public static JTextArea messages;
    public RMIClient4() throws RemoteException {
        super();
    }

    @Override
    public void update(Event e) throws RemoteException {
        try {
            InputStream in = new ByteArrayInputStream(e.getImg());
            BufferedImage bImageFromConvert = ImageIO.read(in);

            ImageIcon icon = new ImageIcon(bImageFromConvert);
            RMIClient4 c = new RMIClient4();
            messages.append("Movement event caught at: " + e.getDate()+"\n");
            images.setIcon(icon);
            images.repaint();

            ImageIO.write(bImageFromConvert, "PNG", new File("newevent.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(RMIClient4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RMIClient4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {

        ServerInterface look_up = (ServerInterface) Naming.lookup("//localhost/ZooSecurityRMI");
        RMIClient4 client4 = new RMIClient4();

        JFrame frame = new JFrame("Live Video Security");

        images = new JLabel();
        images.setBounds(10, 10, 400, 300);

        messages = new JTextArea();
        messages.setEditable(false);
        messages.setBounds(300, 10, 350, 400);
        
        scroll = new JScrollPane(messages);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        JButton logout = new JButton("Log Out");
        logout.setBounds(550, 420, 100, 30);

        frame.add(scroll);
        frame.add(images);
        frame.add(messages);
        frame.add(logout);

        frame.setSize(700, 500);
        frame.setLayout(null);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ServerInterface look_up = (ServerInterface) Naming.lookup("//localhost/ZooSecurityRMI");
                    look_up.leaveChat(client4);
                    System.exit(1);
                } catch (NotBoundException ex) {
                    Logger.getLogger(RMIClient4.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(RMIClient4.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIClient4.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        look_up.joinChat(client4);

    }

}
