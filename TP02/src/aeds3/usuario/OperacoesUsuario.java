package aeds3.usuario;

import aeds3.Arquivo;
import aeds3.HashExtensivel;
import model.Usuario;

public final class OperacoesUsuario extends Arquivo<Usuario> {
    HashExtensivel<ParIdEmail> idxIndireto;

    public OperacoesUsuario() throws Exception {
        super(Usuario.class);
        this.idxIndireto = new HashExtensivel<ParIdEmail>(
            ParIdEmail.class.getConstructor(), 
            5,
            "user/id.email",
            "user/id.email"
        );
    }

    public int create(final Usuario user) throws Exception {
        int id = super.create(user);
        this.idxIndireto.create(ParIdEmail.create(user.getId(), user.getEmail()));
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
    
    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = this.read(id);
        if(u == null) return false;
        this.idxIndireto.delete(u.getEmail().hashCode());
        return super.delete(id);
    }

    public void novoIdxIndireto(final Usuario user, final String oldEmail) throws Exception {
        this.idxIndireto.delete(oldEmail.hashCode());
        this.idxIndireto.create(ParIdEmail.create(user.getId(), user.getEmail()));
    }
}