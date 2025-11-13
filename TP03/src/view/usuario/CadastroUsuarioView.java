package view.usuario;

import controller.UsuarioController;
import util.CampoObrigatorio;
import view.View;

public final class CadastroUsuarioView extends View {
    public static final CadastroUsuarioView INSTANCE = new CadastroUsuarioView();

    private CadastroUsuarioView() {
        super("Cadastrar", false);
    }

    @Override
    public void viewDisplay() {
        String name;
        String email;
        String password;
        String secretQuestion;
        String secretAnswer;

        System.out.println("Insira seu nome:");
        name = scanner.nextLine();

        System.out.println("Insira seu e-mail:");
        email = scanner.nextLine();

        System.out.println("Insira sua senha:");
        password = scanner.nextLine();

        System.out.println("Insira sua pergunta secreta:");
        secretQuestion = scanner.nextLine();

        System.out.println("Insira a resposta da sua pergunta secreta:");
        secretAnswer = scanner.nextLine();

        if (CampoObrigatorio.isBlank(name) ||
                CampoObrigatorio.isBlank(email) ||
                CampoObrigatorio.isBlank(password) ||
                CampoObrigatorio.isBlank(secretQuestion) ||
                CampoObrigatorio.isBlank(secretAnswer)) {
            this.alertMessage("Todos os campos são obrigatórios.");
            return;
        }

        int id = UsuarioController.INSTANCE.create(name, email, password, secretQuestion, secretAnswer);

        if(id == -1) this.alertMessage("Não foi possivel cadastrar.");
        else this.alertMessage("Cadastrado. Acesse via login.");
    }
}
