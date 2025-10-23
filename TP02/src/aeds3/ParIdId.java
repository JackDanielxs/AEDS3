package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ParIdId implements RegistroArvoreBMais<ParIdId> {
  private int id1;
  private int id2;
  private short TAMANHO = 8;

  public int getID1() { return this.id1; }
  public int getID2() { return this.id2; }

  public ParIdId() { this(-1, -1); }

  public ParIdId(int id) { this(id, -1); }

  public ParIdId(int id1, int id2) {
    try {
      this.id1 = id1;
      this.id2 = id2;
    } catch (Exception ec) { ec.printStackTrace(); }
  }

  @Override
  public ParIdId clone() { return new ParIdId(this.id1, this.id2); }

  public short size() { return this.TAMANHO; }

  public int compareTo(ParIdId a) {
    if (this.id1 != a.id1)
      return this.id1 - a.id1;
    else
      return this.id2 == -1 ? 0 : this.id2 - a.id2;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id1);
    dos.writeInt(this.id2);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id1 = dis.readInt();
    this.id2 = dis.readInt();
  }
}