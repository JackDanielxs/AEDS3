package aeds3.usuario;

import aeds3.Arquivo;
import aeds3.HashExtensivel;
import model.Usuario;

public class OperacoesUsuario extends Arquivo<Usuario> {
    HashExtensivel<ParIdEmail> idxIndireto;

    public OperacoesUsuario() throws Exception {
        super(Usuario.class);
        this.idxIndireto = new HashExtensivel<ParIdEmail>(
            ParIdEmail.class.getConstructor(), 
            5,
            "usuario/id.email",
            "usuario/id.email"
        );
    }

    public int create(final Usuario u) throws Exception {
        int id = super.create(u);
        this.idxIndireto.create(ParIdEmail.create(u.getId(), u.getEmail()));
        return id;
    }

    public Usuario getByEmail(final String email){
        int id = -1; 
        Usuario u = null;
        try{
            ParIdEmail par = this.idxIndireto.read(email.hashCode());
            
            if(par == null) return null;
            id = par.getId();
            
            u = super.read(id);
        }catch(final Exception e){ System.out.println(e.getMessage()); }

        return u;
    }

    public void updateIdxIndireto(final Usuario u, final String oldEmail) throws Exception {
        this.idxIndireto.delete(oldEmail.hashCode());
        this.idxIndireto.create(ParIdEmail.create(u.getId(), u.getEmail()));
    }
}
