
import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stathis Andr
 */
public interface ServerInterface extends Remote {

    public void joinChat(ClientInterface client) throws RemoteException;

    public void leaveChat(ClientInterface client) throws RemoteException;

    public void sendMessage(Event e) throws RemoteException;
    
}
