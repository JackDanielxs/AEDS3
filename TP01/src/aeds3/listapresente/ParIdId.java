package aeds3.listapresente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import aeds3.RegistroArvoreBMais;

public class ParIdId implements RegistroArvoreBMais<ParIdId> {
  private int idUsuario;
  private int idLista;
  private short TAMANHO = 8;

  public int getIdUsuario() { return this.idUsuario; }
  public int getIdLista() { return this.idLista; }

  public ParIdId() { this(-1, -1); }
  public ParIdId(int id) { this(id, -1); }

  public ParIdId(int idUsuario, int idLista) {
    try {
      this.idUsuario = idUsuario;
      this.idLista = idLista;
    } catch (Exception ec) { ec.printStackTrace(); }
  }

  @Override
  public ParIdId clone() {
    return new ParIdId(this.idUsuario, this.idLista);
  }

  public short size() { return this.TAMANHO; }

  public int compareTo(ParIdId a) {
    if (this.idUsuario != a.idUsuario)
      return this.idUsuario - a.idUsuario;
    else
      return this.idLista == -1 ? 0 : this.idLista - a.idLista;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.idUsuario);
    dos.writeInt(this.idLista);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idUsuario = dis.readInt();
    this.idLista = dis.readInt();
  }
}