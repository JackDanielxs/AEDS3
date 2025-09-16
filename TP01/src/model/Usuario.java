package model;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import util.Encryption;
import util.CampoObrigatorio;

public class Usuario extends Registro{
    private String nome = "";
    private String email = "";
    private String hashSenha = "";
    private String pergunta = "";
    private String resposta = "";

    public Usuario() {}
    private Usuario(
        final int id,
        final String nome,
        final String email,
        final String hashSenha,
        final String pergunta,
        final String resposta,
        final boolean status
    ) {
        super(id, status);
        this.nome = CampoObrigatorio.require(nome);
        this.email = CampoObrigatorio.require(email);
        this.hashSenha = CampoObrigatorio.require(hashSenha);
        this.pergunta = CampoObrigatorio.require(pergunta);
        this.resposta = CampoObrigatorio.require(resposta);
    }
    public String getNome(){ return this.nome; }
    public void setNome(final String nome){ this.nome = nome; }
    public void setHashSenha(final String hashSenha){ this.hashSenha = hashSenha; }
    public String getHashSenha(){ return this.hashSenha.trim(); }
    public String getEmail() { return this.email; }
    public void setEmail(final String email) { this.email = email; }
    public String getPergunta() { return this.pergunta; }
    public void setPergunta(final String pergunta) { this.pergunta = pergunta; }
    public String getResposta() { return this.resposta; }
    public void setResposta(final String resposta) { this.resposta = resposta; }

    public static Usuario create(
        final String nome,
        final String email,
        final String senha,
        final String pergunta,
        final String resposta
    ) { return new Usuario(-1, nome, email, Encryption.toMd5(senha), pergunta, resposta, true); }

    public static Usuario from(
        final int id,
        final String nome,
        final String email,
        final String hashSenha,
        final String pergunta,
        final String resposta,
        final boolean status
    ) { return new Usuario(id, nome, email, hashSenha, pergunta, resposta, status); }

    @Override
    public void fromByteArray(final byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readUTF();
        this.pergunta = dis.readUTF();
        this.resposta = dis.readUTF();
        this.status = dis.readBoolean();
    }
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.hashSenha);
        dos.writeUTF(this.pergunta);
        dos.writeUTF(this.resposta);
        dos.writeBoolean(status);
        return baos.toByteArray();
    }
}
