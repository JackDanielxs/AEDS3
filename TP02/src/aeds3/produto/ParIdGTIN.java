package aeds3.produto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.RegistroHashExtensivel;
import util.CampoObrigatorio;

public final class ParIdGTIN implements RegistroHashExtensivel {
    private int id = -1;
    private String gtin = "";
    private short TAMANHO = 17;

    public ParIdGTIN() { };

    private ParIdGTIN(final int id, final String gtin) {
        this.id = id;
        this.gtin = CampoObrigatorio.requireMinSize(gtin, 13);
    }

    public static ParIdGTIN create(final int id, final String gtin) {
        return new ParIdGTIN(id, gtin);
    }

    public String getGtin() { return this.gtin; }
    public int getId() { return this.id; }

    @Override
    public short size() { return this.TAMANHO; }

    @Override
    public int hashCode() { return this.gtin.hashCode(); }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.write(this.gtin.getBytes());
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] gtin = new byte[13];
        this.id = dis.readInt();
        dis.read(gtin);
        this.gtin = new String(gtin);
    } 
}