package controller;

import java.util.List;

import aeds3.Memoria;
import aeds3.usuario.OperacoesUsuario;
import model.Usuario;
import util.CampoObrigatorio;
import util.Encryption;

public final class UsuarioController {
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

    public boolean login(final String email, final String senha) {
        try {
            Usuario u = this.ops.getByEmail(email);
            if (u == null) return false;
            if (!u.getHash().equals(Encryption.toMd5(senha))) return false;
            Memoria.setIdUsuario(u.getId());
            return u.isActive();
        } catch (final Exception e) { return false; }
    }

    public List<String> getPergunta(final String email, final String password) {
        try {
            Usuario u = this.ops.getByEmail(email);
            if (u == null) return List.of();
            if (!u.getHash().equals(Encryption.toMd5(password))) return List.of();
            Memoria.setIdUsuario(u.getId());
            return List.of(u.getPergunta(), u.getResposta());
        } catch (final Exception e) { List.of(); }
        return List.of();
    }

    public boolean logout() {
        Memoria.logout();
        return Memoria.isLogout();
    }

    public int create(
            final String nome,
            final String email,
            final String senha,
            final String pergunta,
            final String resposta) {
        int id = -1;
        try {
            Usuario user = Usuario.create(nome, email, senha, pergunta, resposta);
            id = this.ops.create(user);
        } catch (final Exception e) { return -1; }

        return id;
    }

    public void updateUser(
            final int id,
            final String nome,
            final String email,
            final String senha,
            final String pergunta,
            final String resposta) {
        try {
            final Usuario bd = getById(id);
            if (bd != null) {
                String oldEmail = bd.getEmail();

                boolean isNewPassword = CampoObrigatorio.isNotBlank(senha)
                        && !senha.equals(bd.getHash());

                Usuario novo = Usuario.from(
                        bd.getId(),
                        nome,
                        email,
                        isNewPassword ? Encryption.toMd5(senha.trim()) : bd.getHash(),
                        pergunta,
                        resposta,
                        true);

                this.ops.update(novo);
                ops.novoIdxIndireto(novo, oldEmail);
            } else { System.out.println("Usuário (" + id + ") não encontrado."); }
        } catch (final Exception e) { System.out.println("Erro ao atualizar usuário: " + e.getMessage()); }
    }

    public boolean delete() {
        try {
            if (!this.ops.delete(Memoria.getIdUsuario())) { return false; }
        } catch (final Exception e) { return false; }
        this.logout();
        return true;
    }

    public boolean updateStatus(final boolean status) {
        int id = Memoria.getIdUsuario();
        try {
            Usuario u = this.getById(id);
            u.updateStatus(status);
            ListaPresenteController.INSTANCE.toggleStatusUsuario(false);
            return ops.update(u);
        } catch (final Exception e) { return false; }
    }
}
