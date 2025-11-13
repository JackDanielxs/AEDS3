package aeds3.listapresente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.RegistroHashExtensivel;
import util.CampoObrigatorio;

public final class ParIdCodigo implements RegistroHashExtensivel {
    private int id = -1;
    private String codCompartilha = "";
    private short TAMANHO = 14;

    public ParIdCodigo() { };

    private ParIdCodigo(final int id, final String codCompartilha) {
        this.id = id;
        this.codCompartilha = CampoObrigatorio.requireNonBlank(codCompartilha);
    }

    public static ParIdCodigo create(final int id, final String codCompartilha) {
        return new ParIdCodigo(id, codCompartilha);
    }

    public String getShareCode() { return this.codCompartilha; }
    public int getId() { return this.id; }

    @Override
    public short size() { return this.TAMANHO; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.write(this.codCompartilha.getBytes());
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] shareCode = new byte[10];
        this.id = dis.readInt();
        dis.read(shareCode);
        this.codCompartilha = new String(shareCode);
    }

    @Override
    public int hashCode() { return this.codCompartilha.hashCode(); }
}
