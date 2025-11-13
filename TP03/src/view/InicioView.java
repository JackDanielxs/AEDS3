package view;

import controller.UsuarioController;
import view.usuario.LoginUsuarioView;
import view.usuario.ReativarUsuarioView;
import view.usuario.CadastroUsuarioView;

public final class InicioView extends View {
    public static final InicioView INSTANCE = new InicioView();
    UsuarioController controller = UsuarioController.INSTANCE;

    private InicioView() {
        super("Presente Fácil", false);
    }

    @Override
    public void viewDisplay() {
        String option;
        do {
            String menu = """
                (1) Login
                (2) Criar usuário
                (3) Reativar usuário

                (S) Sair

                Opção: """;
            System.out.print(menu);
            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    login();
                    break;
                case "2":
                    signup();
                    break;
                case "3":
                    this.reactivate();
                    break;
                case "S":
                    this.exit();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

            System.out.println();

        } while (!option.equals("S"));
    }

    private void login() {
        this.nextPage(LoginUsuarioView.INSTANCE);
    }

    private void reactivate() {
        this.nextPage(ReativarUsuarioView.INSTANCE);
    }

    private void signup() {
        this.nextPage(CadastroUsuarioView.INSTANCE);
    }
}
