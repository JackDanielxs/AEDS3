package aeds3.listapresente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.RegistroHashExtensivel;

public class ParIdCodigo implements RegistroHashExtensivel {
    private int id = -1;
    private String codigo = "";
    private short SIZE = 14;

    public ParIdCodigo(){};

    private ParIdCodigo(final int id, final String codigo){
        this.id = id;
        this.codigo = codigo;
    }

    public static ParIdCodigo create(final int id, final String codigo){
        return new ParIdCodigo(id, codigo);
    }

    public String getCodigo(){ return this.codigo; }
    public int getId(){ return this.id; }

    @Override
    public short size(){ return this.SIZE; }

    @Override
    public int hashCode() { return this.codigo.hashCode(); }

    @Override
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.write(this.codigo.getBytes());
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] cod = new byte[10];
        this.id = dis.readInt();
        dis.read(cod);
        this.codigo = new String(cod);
    }
}
