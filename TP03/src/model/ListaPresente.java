package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import util.NanoID;
import util.CampoObrigatorio;

public final class ListaPresente extends Registro {
    private String nome = "";
    private String descricao = "";
    private LocalDate dtCriacao = LocalDate.now();
    private Optional<LocalDate> dtExpiracao = null;
    private String codCompartilha = "";
    private int idUsuario = -1;

    public ListaPresente() { }

    private ListaPresente(
            final int id,
            final String nome,
            final String descricao,
            final LocalDate dtCriacao,
            final Optional<LocalDate> dtExpiracao,
            final String codCompartilha,
            final int idUsuario,
            final boolean status) {
        super(id, status);
        this.nome = CampoObrigatorio.requireNonBlank(nome);
        this.descricao = descricao;
        this.dtCriacao = dtCriacao;
        this.dtExpiracao = dtExpiracao;
        this.codCompartilha = CampoObrigatorio.requireNonBlank(codCompartilha);
        this.idUsuario = idUsuario;
    }

    public int getUserId() { return this.idUsuario; }
    public String getName() { return this.nome; }
    public String getDescription() { return this.descricao; }
    public LocalDate getCreatedAt() { return this.dtCriacao; }
    public Optional<LocalDate> getExpirationDate() { return this.dtExpiracao; }
    public String getCode() { return this.codCompartilha; }

    public String getExpirationDateFormated(Object... pre) {
        String prefix = (pre != null && pre.length > 0)
                ? String.join(" ", Arrays.stream(pre)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .toArray(String[]::new)) + " "
                : "";
        return this.dtExpiracao
                .map(v -> prefix + v.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .orElse(prefix + "sem data");
    }

    public static ListaPresente from(
            final String nome,
            final String descricao,
            final LocalDate dtCriacao,
            final Optional<LocalDate> dtExpiracao,
            final String codCompartilha,
            final int idUsuario,
            final int id,
            final boolean status) {
        return new ListaPresente(id, nome, descricao, dtCriacao, dtExpiracao, codCompartilha, idUsuario, status);
    }

    public static ListaPresente create(
        final String nome,
        final String descricao,
        final Optional<LocalDate> dtExpiracao,
        final int idUsuario) 
    {
        return new ListaPresente(-1, nome, descricao, LocalDate.now(), dtExpiracao, NanoID.nanoid(), idUsuario, true);
    }

    @Override
    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
        byte[] shareCode = new byte[10];

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.dtCriacao = LocalDate.ofEpochDay(dis.readInt());
        final int optionalExpDate = dis.readInt();
        this.dtExpiracao = Optional.ofNullable(optionalExpDate == -1 ? null : LocalDate.ofEpochDay(optionalExpDate));
        dis.read(shareCode);
        this.codCompartilha = new String(shareCode);
        this.idUsuario = dis.readInt();
        this.status = dis.readBoolean();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int) this.dtCriacao.toEpochDay());
        dos.writeInt(this.dtExpiracao.map(expDate -> (int) expDate.toEpochDay()).orElse(-1));
        dos.write(this.codCompartilha.getBytes());
        dos.writeInt(idUsuario);
        dos.writeBoolean(this.status);
        return baos.toByteArray();
    }
}
