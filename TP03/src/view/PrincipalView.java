package view;

import controller.UsuarioController;
import view.listapresente.MinhasListasPresenteView;
import view.listapresente.BuscarListaPresenteView;
import view.produto.MenuProdutosView;
import view.usuario.MeusDadosView;

public final class PrincipalView extends View {
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
                (3) Produtos
                (4) Buscar lista

                (S) Logout

                Opção: """;
            System.out.print(menu);

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    handleMyData();
                    break;
                case "2":
                    handleMyLists();
                    break;
                case "3":
                    handleProducts();
                    break;
                case "4":
                    handleSearchList();
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

    private void handleMyData() {
        this.nextPage(MeusDadosView.INSTANCE);
    }

    private void handleMyLists() {
        this.nextPage(MinhasListasPresenteView.INSTANCE);
    }

    private void handleProducts() {
        this.nextPage(MenuProdutosView.INSTANCE);
    }

    private void handleSearchList() {
        this.nextPage(BuscarListaPresenteView.INSTANCE);
    }
}
