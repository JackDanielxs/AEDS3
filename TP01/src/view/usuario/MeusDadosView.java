package view.usuario;

import aeds3.Memoria;
import controller.UsuarioController;
import model.Usuario;
import view.View;

public class MeusDadosView extends View {
    public static final MeusDadosView INSTANCE = new MeusDadosView();

    private MeusDadosView() {
        super("Dados pessoais", true);
    }

    @Override
    public void viewDisplay() {
        String option;

        do {
            Usuario user = UsuarioController.INSTANCE.getById(Memoria.getIdUsuario());
            
            System.out.printf(
                """
                Nome: %s
                Email: %s

                (1) Editar dados
                (2) Inativar usuário

                (R) Voltar

                Opção: """,
                    user.getNome(),
                    user.getEmail()
            );

            option = sc.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    editUserData();
                    break;
                case "2":
                    inativar();
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

    private void editUserData() { this.nextPage(EditarUsuarioView.INSTANCE); }

    private void inativar() {
        UsuarioController.INSTANCE.editarStatus(false);
        this.logout();
    }
}
