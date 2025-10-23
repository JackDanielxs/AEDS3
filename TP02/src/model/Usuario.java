package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import util.CampoObrigatorio;
import util.Encryption;

public final class Usuario extends Registro {
    private String nome = "";
    private String email = "";
    private String hashSenha = "";
    private String pergunta = "";
    private String resposta = "";

    public Usuario() { }

    private Usuario(
            final int id,
            final String nome,
            final String email,
            final String hashSenha,
            final String pergunta,
            final String resposta,
            final boolean isActive) {
        super(id, isActive);
        this.nome = CampoObrigatorio.requireNonBlank(nome);
        this.email = CampoObrigatorio.requireNonBlank(email);
        this.hashSenha = CampoObrigatorio.requireNonBlank(hashSenha);
        this.pergunta = CampoObrigatorio.requireNonBlank(pergunta);
        this.resposta = CampoObrigatorio.requireNonBlank(resposta);
    }

    public String getNome() { return this.nome; }
    public String getEmail() { return this.email; }
    public String getHash() { return this.hashSenha.trim(); }
    public String getPergunta() { return this.pergunta; }
    public String getResposta() { return this.resposta; }

    public void setNome(final String nome) { this.nome = nome; }
    public void setEmail(final String email) { this.email = email; }
    public void setHash(final String hashSenha) { this.hashSenha = hashSenha; }
    public void setPergunta(final String pergunta) { this.pergunta = pergunta; }
    public void setResposta(final String resposta) { this.resposta = resposta; }

    public static Usuario from(
            final int id,
            final String nome,
            final String email,
            final String hashSenha,
            final String pergunta,
            final String resposta,
            final boolean isActive) {
        return new Usuario(id, nome, email, hashSenha, pergunta, resposta, isActive);
    }

    public static Usuario create(
            final String nome,
            final String email,
            final String senha,
            final String pergunta,
            final String resposta) {
        return new Usuario(-1, nome, email, Encryption.toMd5(senha), pergunta, resposta, true);
    }

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
