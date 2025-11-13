package controller;

import java.util.List;

import aeds3.Memoria;
import aeds3.usuario.OperacoesUsuario;
import model.Usuario;
import util.Encryption;
import util.CampoObrigatorio;

public final class UsuarioController {
    public final static UsuarioController INSTANCE = new UsuarioController();
    private OperacoesUsuario ops;

    private UsuarioController() {
        try { this.ops = new OperacoesUsuario(); } 
        catch (final Exception e) { e.printStackTrace(); }
    }

    public Usuario getById(final int id) {
        try { return this.ops.read(id); } 
        catch (final Exception e) { return null; }
    }

    public boolean login(final String email, final String senha) {
        try {
            Usuario u = this.ops.getByEmail(email);
            if (u == null)
                return false;

            if (!u.getHashPassword().equals(Encryption.toMd5(senha)))
                return false;

            Memoria.setUserId(u.getId());
            return u.isActive();
        } catch (final Exception e) { return false; }
    }

    public List<String> getUserQuestion(final String email, final String senha) {
        try {
            Usuario u = this.ops.getByEmail(email);
            if (u == null)
                return List.of();

            if (!u.getHashPassword().equals(Encryption.toMd5(senha)))
                return List.of();

            Memoria.setUserId(u.getId());
            return List.of(u.getSecretQuestion(), u.getSecretAnswer());
        } catch (final Exception e) { List.of(); }
        return List.of();
    }

    public boolean logout() {
        Memoria.logout();
        return Memoria.isLogout();
    }

    public int create(
            final String name,
            final String email,
            final String password,
            final String secretQuestion,
            final String secretAnswer) {
        int id = -1;
        try {
            Usuario u = Usuario.create(name, email, password, secretQuestion, secretAnswer);
            id = this.ops.create(u);
        } catch (final Exception e) { return -1; }

        return id;
    }

    public void updateUser(
            final int id,
            final String name,
            final String email,
            final String password,
            final String secretQuestion,
            final String secretAnswer) {
        try {
            final Usuario oldUser = getById(id);
            if (oldUser != null) {
                String oldEmail = oldUser.getEmail();

                boolean isNewPassword = CampoObrigatorio.isNotBlank(password)
                        && !password.equals(oldUser.getHashPassword());

                Usuario newUser = Usuario.from(
                        oldUser.getId(),
                        name,
                        email,
                        isNewPassword ? Encryption.toMd5(password.trim()) : oldUser.getHashPassword(),
                        secretQuestion,
                        secretAnswer,
                        true);

                this.ops.update(newUser);
                ops.indiceIndireto(newUser, oldEmail);
            } else
                System.out.println("Usuário com ID [" + id + "] não encontrado.");

        } catch (final Exception e) { System.out.println("Erro ao atualizar usuário: " + e.getMessage()); }
    }

    public boolean delete() {
        try {
            if (!this.ops.delete(Memoria.getUserId())) { return false; }
        } catch (final Exception e) { return false; }
        this.logout();
        return true;
    }

    public boolean changeStatus(final boolean active) {
        int id = Memoria.getUserId();
        try {
            Usuario user = this.getById(id);
            user.changeStatus(active);
            ListaPresenteController.INSTANCE.toggleStatus(false);
            return ops.update(user);
        } catch (final Exception e) { return false; }
    }
}
