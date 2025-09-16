package controller;

import java.util.List;

import aeds3.Memoria;
import aeds3.usuario.OperacoesUsuario;
import model.Usuario;
import util.Encryption;
import util.CampoObrigatorio;

public class UsuarioController {
    public final static UsuarioController INSTANCE = new UsuarioController();
    private OperacoesUsuario ops;

    private UsuarioController() {
        try {
            this.ops = new OperacoesUsuario();
        } catch (final Exception e) { e.printStackTrace(); }
    }

    public Usuario getById(final int id) {
        try {
            return this.ops.read(id);
        } catch (final Exception e) { return null; }
    }

    public List<String> getPergunta(final String email, final String senha){
        try{        
            Usuario user = this.ops.getByEmail(email);
            if(user == null) return List.of();
            if(!user.getHashSenha().equals(Encryption.toMd5(senha))) return List.of();
            Memoria.setIdUsuario(user.getId());
            return List.of(user.getPergunta(), user.getResposta());
        }catch(final Exception e){
            List.of();
        }
        return List.of();
    }

    public boolean login(final String email, final String senha){
        try{        
            Usuario user = this.ops.getByEmail(email);
            if(user == null) return false;
            if(!user.getHashSenha().equals(Encryption.toMd5(senha))) return false;
            Memoria.setIdUsuario(user.getId());
            return user.getStatus();
        }catch(final Exception e){ return false; }
    }

    public int create(
        final String nome,
        final String email,
        final String senha,
        final String pergunta,
        final String resposta
    ){
        int id = -1;
        try{
            Usuario user = Usuario.create(nome, email, senha, pergunta, resposta);
            id = this.ops.create(user);
        }catch(final Exception e){ return -1; }

        return id;
    }

    public void updateUser(
        final int id, 
        final String nome,
        final String email, 
        final String senha, 
        final String pergunta,
        final String resposta
        ) {
        try {
            final Usuario antigo = getById(id);
            if (antigo != null) {
                String oldEmail = antigo.getEmail();

                boolean novaSenha = CampoObrigatorio.isValid(senha) && !senha.equals(antigo.getHashSenha());
               
                Usuario novo = Usuario.from(
                    antigo.getId(), 
                    nome, 
                    email, 
                    novaSenha ? Encryption.toMd5(senha.trim()) : antigo.getHashSenha(),
                    pergunta, 
                    resposta,
                    true
                );

                this.ops.update(novo);
                ops.updateIdxIndireto(novo, oldEmail);
            } else System.out.println("Usuário com ID " + id + " não encontrado.");

        } catch (final Exception e) { System.out.println("Erro ao atualizar usuário: " + e.getMessage()); }
    }

    public boolean delete(){
        try {
            if(!this.ops.delete(Memoria.getIdUsuario())){ return false; }
        } catch (final Exception e) { return false; }
        this.logout();
        return true;
    }

    public boolean editarStatus(final boolean status) {
        int id = Memoria.getIdUsuario();
        try {
            Usuario user = this.getById(id);
            user.editarStatus(status);
            ListaPresenteController.INSTANCE.editarStatusPorUsuario(false);
            return ops.update(user);
        } catch (final Exception e) { return false; }
    }

    public boolean logout(){
        Memoria.logout();
        return Memoria.isLogout();
    }
}
