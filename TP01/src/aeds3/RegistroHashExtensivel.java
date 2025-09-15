package aeds3;

import java.io.IOException;

public interface RegistroHashExtensivel {
  public int hashCode(); // the Object already have this method so dont throw an error for not implement this
  public short size();
  public byte[] toByteArray() throws IOException;
  public void fromByteArray(byte[] ba) throws IOException;
}
