package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import util.CampoObrigatorio;

public final class Produto extends Registro {
    private String nome;
    private String descricao;
    private String gtin;

    public Produto() { }

    public String getGtin() { return this.gtin; }
    public String getDescription() { return this.descricao; }
    public String getName() { return this.nome; }

    private Produto(
            final int id,
            final String nome,
            final String descricao,
            final boolean status,
            final String gtin) {
        super(id, status);
        this.nome = CampoObrigatorio.requireNonBlank(nome);
        this.descricao = descricao;
        this.gtin = CampoObrigatorio.requireMinSize(gtin, 13);
    }

    public static Produto create(
            final String nome,
            final String descricao,
            final String gtin) {
        return new Produto(-1, nome, descricao, true, gtin);
    }

    @Override
    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
        byte[] gtin = new byte[13];

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        dis.read(gtin);
        this.gtin = new String(gtin);
        this.status = dis.readBoolean();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.write(gtin.getBytes());
        dos.writeBoolean(status);
        return baos.toByteArray();
    }

    public static Produto from(final String nome, final String descricao, final String gtin, final int id,
            final boolean newStatus) {
        return new Produto(id, nome, descricao, newStatus, gtin);
    }

}
