package view;

import controller.UsuarioController;
import view.listapresente.BuscarListaView;
import view.listapresente.MinhasListasView;
import view.usuario.MeusDadosView;

public class PrincipalView extends View {
    public static final PrincipalView INSTANCE = new PrincipalView();
    private PrincipalView() {
        super("Início", true);
    }

    @Override
    public void viewDisplay() {
        String option;

        do {
            String menu = """
                (1) Dados pessoais
                (2) Minhas listas
                (3) Produtos - **AINDA NÃO IMPLEMENTADO**
                (4) Buscar lista

                (S) Logout

                Opção: """;
            System.out.print(menu);

            option = sc.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    MeusDadosView();
                    break;
                case "2":
                    MinhasListasView();
                    break;
                case "3":
                    //ProdutosView();
                    break;
                case "4":
                    BuscarListaView();
                    break;
                case "S":
                    UsuarioController.INSTANCE.logout();
                    this.logout();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

            System.out.println();

        } while (!option.equals("S"));
    }

    private void MeusDadosView() {
        this.nextPage(MeusDadosView.INSTANCE);
    }

    private void MinhasListasView() {
        this.nextPage(MinhasListasView.INSTANCE);
    }

    // private void ProdutosView() {}

    private void BuscarListaView() {
        this.nextPage(BuscarListaView.INSTANCE);
    }
}
