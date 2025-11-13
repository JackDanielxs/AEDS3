package view.usuario;

import aeds3.Memoria;
import controller.UsuarioController;
import model.Usuario;
import view.View;

public final class MeusDadosView extends View {
    public static final MeusDadosView INSTANCE = new MeusDadosView();

    private MeusDadosView() {
        super("Meus dados", true);
    }

    @Override
    public void viewDisplay() {
        String option;

        do {
            Usuario user = UsuarioController.INSTANCE.getById(Memoria.getUserId());

            System.out.printf(
                """
                Nome: %s
                Email: %s

                (1) Editar dados
                (2) Inativar usuário

                (R) Voltar

                Opção: """,
                    user.getName(),
                    user.getEmail()
            );

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    editUserData();
                    break;
                case "2":
                    deactivate();
                    break;
                case "R":
                    this.back();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

            System.out.println();

        } while (!option.equals("R"));
    }

    private void editUserData() {
        this.nextPage(EditarUsuarioView.INSTANCE);
    }

    private void deactivate() {
        UsuarioController.INSTANCE.changeStatus(false);
        this.logout();
    }
}
