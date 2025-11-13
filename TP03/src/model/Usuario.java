package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import util.Encryption;
import util.CampoObrigatorio;

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
            final boolean status) {
        super(id, status);
        this.nome = CampoObrigatorio.requireNonBlank(nome);
        this.email = CampoObrigatorio.requireNonBlank(email);
        this.hashSenha = CampoObrigatorio.requireNonBlank(hashSenha);
        this.pergunta = CampoObrigatorio.requireNonBlank(pergunta);
        this.resposta = CampoObrigatorio.requireNonBlank(resposta);
    }

    public String getName() { return this.nome; }
    public void setName(final String nome) { this.nome = nome; }
    public void setHashPassword(final String hashSenha) { this.hashSenha = hashSenha; }
    public String getHashPassword() { return this.hashSenha.trim(); }
    public String getEmail() { return this.email; }
    public void setEmail(final String email) { this.email = email; }
    public String getSecretQuestion() { return this.pergunta; }
    public void setSecretQuestion(final String pergunta) { this.pergunta = pergunta; }
    public String getSecretAnswer() { return this.resposta; }
    public void setSecretAnswer(final String resposta) { this.resposta = resposta; }

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
