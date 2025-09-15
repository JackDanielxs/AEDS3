package aeds3.usuario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.RegistroHashExtensivel;

public class ParIdEmail implements RegistroHashExtensivel {
    private int id = -1;
    private String email = "";
    private short SIZE = 30;

    public ParIdEmail(){};
    
    private ParIdEmail(final int id, final String email){
        this.id = id;
        this.email = email;
    }

    public static ParIdEmail create(final int id, final String email){
        return new ParIdEmail(id, email);
    }

    public String getEmail(){ return this.email; }
    public int getId(){ return this.id; }

    @Override
    public short size(){ return this.SIZE; }

    @Override
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.email);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.email = dis.readUTF();
    }

    @Override
    public int hashCode() { return this.email.hashCode(); }
}
