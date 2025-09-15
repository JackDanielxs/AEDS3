package model;
import java.io.IOException;

public abstract class Registro {
    protected int id = -1;
    protected boolean status = true;

    public Registro() {}
    public Registro(final int id) { this.id = id; }

    public Registro(final int id, final boolean status) {
        this.id = id;
        this.status = status;
    }

    public void setId(final int id) { this.id = id; }
    public int getId() { return this.id; }
    public boolean getStatus() { return this.status; }
    public void editarStatus(final boolean status){ this.status = status; }

    public abstract void fromByteArray(final byte[] array) throws IOException;
    public abstract byte[] toByteArray() throws IOException;
}