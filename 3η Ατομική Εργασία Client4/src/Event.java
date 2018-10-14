/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stathis Andr
 */
import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    private byte[] img;
    private Date date;

    public Event(byte[] array) {
        this.img = array;
        this.date = new Date();
    }

    public byte[] getImg() {
        return this.img;
    }

    public Date getDate() {
        return this.date;
    }
}
