package view.usuario;

import aeds3.Memoria;
import controller.UsuarioController;
import model.Usuario;
import util.CampoObrigatorio;
import view.View;

public final class EditarUsuarioView extends View {
    public static final EditarUsuarioView INSTANCE = new EditarUsuarioView();

    private EditarUsuarioView() {
        super("Editar dados", true);
    }

    @Override
    public void viewDisplay() {
        Usuario user = UsuarioController.INSTANCE.getById(Memoria.getIdUsuario());
        if (user == null) {
            this.alertMessage("Usuário não encontrado.");
            return;
        }
        System.out.println("Deixar em branco -> será utilizado o dado anterior");
        System.out.print("Novo nome: ");
        String name = scanner.nextLine();
        if (CampoObrigatorio.isBlank(name)) {
            name = user.getNome();
        }

        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        if (CampoObrigatorio.isBlank(email)) {
            email = user.getEmail();
        }

        System.out.print("Nova senha: ");
        String password = scanner.nextLine();
        if (CampoObrigatorio.isBlank(password)) {
            password = user.getHash();
        }

        System.out.print("Nova pergunta de segurança: ");
        String secretQuestion = scanner.nextLine();
        if (CampoObrigatorio.isBlank(secretQuestion)) {
            secretQuestion = user.getPergunta();
        }

        System.out.print("Nova resposta de segurança: ");
        String secretAnswer = scanner.nextLine();
        if (CampoObrigatorio.isBlank(secretAnswer)) {
            secretAnswer = user.getResposta();
        }

        UsuarioController.INSTANCE.updateUser(
                user.getId(),
                name,
                email,
                password,
                secretQuestion,
                secretAnswer);

        this.alertMessage("Dados atualizados.");
    }
}
