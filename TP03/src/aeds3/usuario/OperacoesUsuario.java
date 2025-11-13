package aeds3.usuario;

import aeds3.Arquivo;
import aeds3.HashExtensivel;
import model.Usuario;

public final class OperacoesUsuario extends Arquivo<Usuario> {
    HashExtensivel<ParIdEmail> idxIndireto;

    public int create(final Usuario user) throws Exception {
        int id = super.create(user);
        this.idxIndireto.create(ParIdEmail.create(user.getId(), user.getEmail()));
        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario user = this.read(id);
        if(user == null) return false;
        this.idxIndireto.delete(user.getEmail().hashCode());
        return super.delete(id);
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
    
    public void indiceIndireto(final Usuario user, final String email) throws Exception {
        this.idxIndireto.delete(email.hashCode());
        this.idxIndireto.create(ParIdEmail.create(user.getId(), user.getEmail()));
    }

    public OperacoesUsuario() throws Exception {
        super(Usuario.class);
        this.idxIndireto = new HashExtensivel<ParIdEmail>(
            ParIdEmail.class.getConstructor(), 
            5,
            "usuario/id.email",
            "usuario/id.email"
        );
    }
}
