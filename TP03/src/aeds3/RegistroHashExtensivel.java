package aeds3;

import java.io.IOException;

public interface RegistroHashExtensivel {
  public int hashCode();
  public short size();
  public byte[] toByteArray() throws IOException;
  public void fromByteArray(byte[] ba) throws IOException;
}
