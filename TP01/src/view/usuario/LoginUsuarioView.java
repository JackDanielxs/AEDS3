package view.usuario;

import controller.UsuarioController;
import util.CampoObrigatorio;
import view.PrincipalView;
import view.View;

public class LoginUsuarioView extends View {
    public static final LoginUsuarioView INSTANCE = new LoginUsuarioView();

    private LoginUsuarioView() {
        super("Login", false);
    }

    @Override
    public void viewDisplay() {
        String email;
        String senha;

        System.out.println("Insira seu email: ");
        email = sc.nextLine();

        System.out.println("Insira sua senha: ");
        senha = sc.nextLine();

        if (CampoObrigatorio.isNotValid(email) || CampoObrigatorio.isNotValid(senha)) {
            this.alertMessage("Ambos os campos são obrigatórios.");
            return;
        }

        boolean login = UsuarioController.INSTANCE.login(email, senha);

        if(login){
            super.nextPage(PrincipalView.INSTANCE);
        }else{
            this.alertMessage("Email ou senha incorretos.");
        }
    }
}
