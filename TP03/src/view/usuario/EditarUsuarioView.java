package view.usuario;

import aeds3.Memoria;
import controller.UsuarioController;
import model.Usuario;
import util.CampoObrigatorio;
import view.View;

public final class EditarUsuarioView extends View {
    public static final EditarUsuarioView INSTANCE = new EditarUsuarioView();

    private EditarUsuarioView() {
        super("Alterar dados", true);
    }

    @Override
    public void viewDisplay() {
        Usuario user = UsuarioController.INSTANCE.getById(Memoria.getUserId());
        if (user == null) {
            this.alertMessage("Usuário não encontrado!");
            return;
        }
        System.out.println("Deixar em branco -> será utilizado o dado anterior");
        System.out.print("Novo nome: ");
        String name = scanner.nextLine();
        if (CampoObrigatorio.isBlank(name)) {
            name = user.getName();
        }

        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        if (CampoObrigatorio.isBlank(email)) {
            email = user.getEmail();
        }

        System.out.print("Nova senha: ");
        String password = scanner.nextLine();
        if (CampoObrigatorio.isBlank(password)) {
            password = user.getHashPassword();
        }

        System.out.print("Nova pergunta de segurança: ");
        String secretQuestion = scanner.nextLine();
        if (CampoObrigatorio.isBlank(secretQuestion)) {
            secretQuestion = user.getSecretQuestion();
        }

        System.out.print("Nova resposta de segurança: ");
        String secretAnswer = scanner.nextLine();
        if (CampoObrigatorio.isBlank(secretAnswer)) {
            secretAnswer = user.getSecretAnswer();
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
