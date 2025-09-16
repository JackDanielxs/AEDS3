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

public class ListaPresente extends Registro {
    private String nome = "";
    private String descricao = "";
    private LocalDate dtCriacao = LocalDate.now();
    private Optional<LocalDate> dtExpiracao = null;
    private String codigo = "";
    private int idUsuario = -1;

    public ListaPresente(){}
    private ListaPresente(
        final int id,
        final String nome,
        final String descricao,
        final LocalDate dtCriacao,
        final Optional<LocalDate> dtExpiracao,
        final String codigo,
        final int idUsuario,
        final boolean status
    ) {
        super(id);
        this.nome = CampoObrigatorio.require(nome);
        this.descricao = descricao;
        this.dtCriacao = dtCriacao;
        this.dtExpiracao = dtExpiracao;
        this.codigo = CampoObrigatorio.require(codigo);
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() { return this.idUsuario; }
    public String getNome() { return this.nome; }
    public String getDescricao() { return this.descricao; }
    public LocalDate getDtCriacao() { return this.dtCriacao; }
    public Optional<LocalDate> getDtExpiracao() { return this.dtExpiracao; }
    
    public String getDtExpiracaoFormatada(Object... pre) {
        String prefix = (pre != null && pre.length > 0)
        ? String.join(" ", Arrays.stream(pre)
                                 .filter(Objects::nonNull)
                                 .map(Object::toString)
                                 .toArray(String[]::new)) + " "
        : "";
        return this.dtExpiracao
                .map(v-> prefix + v.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .orElse(prefix + "Sem data de expiração");
    }

    public String getCodigo() { return this.codigo; }

    public static ListaPresente create(
            final String nome,
            final String descricao,
            final Optional<LocalDate> dtExpiracao,
            final int idUsuario
    ) { return new ListaPresente(-1, nome, descricao, LocalDate.now(), dtExpiracao, NanoID.nanoid(), idUsuario, true); }

    public static ListaPresente from(
        final String nome,
        final String descricao,
        final LocalDate dtCriacao,
        final Optional<LocalDate> dtExpiracao,
        final String codigo,
        final int idUsuario,
        final int id,
        final boolean status
    ) { return new ListaPresente(id, nome, descricao, dtCriacao, dtExpiracao, codigo, idUsuario, status); }

    

    @Override
    public void fromByteArray(byte[] array) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
        byte[] cod = new byte[10];
        
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.dtCriacao = LocalDate.ofEpochDay(dis.readInt());
        final int ExpDate = dis.readInt();
        this.dtExpiracao = Optional.ofNullable(ExpDate == -1 ? null : LocalDate.ofEpochDay(ExpDate));
        dis.read(cod);
        this.codigo = new String(cod);
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
        dos.writeInt(this.dtExpiracao.map(expDate -> (int)expDate.toEpochDay()).orElse(-1));
        dos.write(this.codigo.getBytes());
        dos.writeBoolean(this.status);
        return baos.toByteArray();
    }
}
