
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stathis Andr
 */
public class RMIServer extends UnicastRemoteObject implements ServerInterface {

    private ArrayList<ClientInterface> Security;

    public RMIServer() throws RemoteException {
        super();
        Security = new ArrayList<ClientInterface>();
    }

    @Override
    public void joinChat(ClientInterface client) throws RemoteException {
        System.out.println("Client connected to Server");
        Security.add(client);
    }

    @Override
    public void leaveChat(ClientInterface client) throws RemoteException {
        System.out.println("Client disconnected from Server");
        Security.remove(client);
    }

    @Override
    public void sendMessage(Event ev) throws RemoteException {
        for (ClientInterface client : Security) {
            try {
                client.update(ev);
            } catch (RemoteException ex) {
                Security.remove(client);
            }
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        try {
            RMIServer server = new RMIServer();
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/ZooSecurityRMI", server);
            System.out.println("Waiting new Messages");

            while (true) {
                BufferedImage originalImage = null;

                byte[] imageInByte = null;

                int image = rand.nextInt(4) + 1;

                switch (image) {
                    case 1:
                        originalImage = ImageIO.read(new File("tile000.png"));
                        break;
                    case 2:
                        originalImage = ImageIO.read(new File("tile001.png"));
                        break;
                    case 3:
                        originalImage = ImageIO.read(new File("tile002.png"));
                        break;
                    case 4:
                        originalImage = ImageIO.read(new File("tile003.png"));
                        break;
                    default:
                        break;
                }

                try ( // convert BufferedImage to byte array
                        ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(originalImage, "PNG", baos);

                    imageInByte = baos.toByteArray();
                }
                Event ev = new Event(imageInByte);

                int n = rand.nextInt(7) + 2;
                TimeUnit.SECONDS.sleep(n);
                server.sendMessage(ev);

            }

        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
