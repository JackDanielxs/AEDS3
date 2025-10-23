package view.usuario;

import java.util.List;

import controller.UsuarioController;
import util.CampoObrigatorio;
import view.View;

public final class ReativarUsuarioView extends View {
    public static final ReativarUsuarioView INSTANCE = new ReativarUsuarioView();

    private ReativarUsuarioView() {
        super("Reativar usuário", false);
    }

    @Override
    public void viewDisplay() {
        String email;
        String senha;
        String resposta;

        System.out.println("Insira seu email: ");
        email = scanner.nextLine();

        System.out.println("Insira sua senha: ");
        senha = scanner.nextLine();

        if (CampoObrigatorio.isBlank(email) || CampoObrigatorio.isBlank(senha)) {
            this.alertMessage("Todos os campos são obrigatórios.");
            return;
        }

        List<String> secret = UsuarioController.INSTANCE.getPergunta(email, senha);

        if (secret.isEmpty()) {
            this.alertMessage("Email ou senha incorretos.");
            return;
        }

        System.out.println("Confirme sua resposta secreta para a pergunta secreta a seguir: ");
        System.out.println(secret.get(0) + ": ");
        resposta = scanner.nextLine();
        if (!secret.get(1).equals(resposta)) {
            this.alertMessage("Incorreto");
            return;
        }

        boolean resp = UsuarioController.INSTANCE.updateStatus(true);
        if (!resp) {
            this.alertMessage("Ocorreu um erro.");
            return;
        }

        this.alertMessage("Usuário reativado.");
    }
}