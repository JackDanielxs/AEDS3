package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIdEndereco implements RegistroHashExtensivel {
    private int id = -1;
    private long endereco = -1;
    private final short SIZE = 12;

    public ParIdEndereco() {}
    
    private ParIdEndereco(final int id, final long endereco) {
        this.id = id;
        this.endereco = endereco;
    }

    public static ParIdEndereco create(final int id, final long endereco) {
        return new ParIdEndereco(id, endereco);
    }

    public long getEndereco(){ return this.endereco; }

    @Override
    public int hashCode() { return this.id; }

    @Override
    public short size() { return this.SIZE; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeLong(this.endereco);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.endereco = dis.readLong();
    }
}
