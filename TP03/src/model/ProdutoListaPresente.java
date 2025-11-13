package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ProdutoListaPresente extends Registro {
    private int idProduto = -1;
    private int idListaPresente = -1;
    private int quantidade = 0;
    private String descricao;

    public ProdutoListaPresente() { }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0)
            return;
        this.quantidade = quantidade;
    }

    public void changeDescription(final String descricao) { this.descricao = descricao; }
    public int getProductId() { return this.idProduto; }
    public String getDescription() { return this.descricao; }
    public int getQuantity() { return this.quantidade; }
    public int getGiftListId() { return this.idListaPresente; }

    private ProdutoListaPresente(
            final int id,
            final int idProduto,
            final int idListaPresente,
            final int quantidade,
            final String descricao,
            final boolean status) {
        super(id, status);
        this.idProduto = idProduto;
        this.idListaPresente = idListaPresente;
        if (quantidade < 0) { throw new IllegalArgumentException("Quantidade não pode ser menor que 0"); }
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public static ProdutoListaPresente create(
            final int idProduto,
            final int idListaPresente,
            final int quantidade,
            final String descricao) {
        return new ProdutoListaPresente(-1, idProduto, idListaPresente, quantidade, descricao, true);
    }

    public static ProdutoListaPresente create(
            final int idProduto,
            final int idListaPresente) {
        return new ProdutoListaPresente(-1, idProduto, idListaPresente, 1, "", true);
    }

    @Override
    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idProduto = dis.readInt();
        this.idListaPresente = dis.readInt();
        this.quantidade = dis.readInt();
        this.descricao = dis.readUTF();
        this.status = dis.readBoolean();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.idListaPresente);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.descricao);
        dos.writeBoolean(this.status);
        return baos.toByteArray();
    }

}
