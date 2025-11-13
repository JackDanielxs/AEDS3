package aeds3.usuario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import aeds3.RegistroHashExtensivel;
import util.CampoObrigatorio;

public final class ParIdEmail implements RegistroHashExtensivel {
    private int id = -1;
    private String email = "";
    private short TAMANHO = 30;

    public ParIdEmail() { };

    private ParIdEmail(final int id, final String email) {
        this.id = id;
        this.email = email;
    }

    public static ParIdEmail create(final int id, final String email) {
        CampoObrigatorio.requireNonBlank(email);
        return new ParIdEmail(id, email);
    }

    public String getEmail() { return this.email; }
    public int getId() { return this.id; }

    @Override
    public short size() { return this.TAMANHO; }

    @Override
    public String toString() {
        return String.format(
            "ID: %d | Email: %s | Tamanho: %d",
            id, email, TAMANHO);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        byte[] emailBytes = this.email.getBytes(StandardCharsets.UTF_8);
        byte[] emailBuffer = new byte[26];
        int length = Math.min(emailBytes.length, emailBuffer.length);
        System.arraycopy(emailBytes, 0, emailBuffer, 0, length);
        dos.write(emailBuffer);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(final byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        byte[] emailBuffer = new byte[26];
        dis.readFully(emailBuffer);
        this.email = new String(emailBuffer, StandardCharsets.UTF_8).trim();
    }

    @Override
    public int hashCode() { return this.email.hashCode(); }
}
