package view.usuario;

import controller.UsuarioController;
import util.CampoObrigatorio;
import view.PrincipalView;
import view.View;

public final class LoginUsuarioView extends View {
    public static final LoginUsuarioView INSTANCE = new LoginUsuarioView();

    private LoginUsuarioView() {
        super("Login", false);
    }

    @Override
    public void viewDisplay() {
        String email;
        String password;

        System.out.println("Insira seu email: ");
        email = scanner.nextLine();

        System.out.println("Insira sua senha: ");
        password = scanner.nextLine();

        if (CampoObrigatorio.isBlank(email) || CampoObrigatorio.isBlank(password)) {
            this.alertMessage("Ambos os campos são obrigatórios.");
            return;
        }
        
        boolean login = UsuarioController.INSTANCE.login(email, password);
        if (login) super.nextPage(PrincipalView.INSTANCE);
        else this.alertMessage("Email ou senha incorretos.");
    }
}
