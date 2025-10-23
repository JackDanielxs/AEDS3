package view.produto;

import view.View;

public final class MenuProdutosView extends View {
    public static final MenuProdutosView INSTANCE = new MenuProdutosView();
    private MenuProdutosView() {
        super("Produtos", true);
    }

    @Override
    public void viewDisplay() {
        String option;

        do {
            String menu = """
                (1) Buscar produtos
                (2) Consultar produtos
                (3) Cadastrar produto

                (R) Voltar

                Opção: """;
            System.out.print(menu);

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    searchByGTIN();
                    break;
                case "2":
                    listAllProducts();
                    break;
                case "3":
                    newProducts();
                    break;
                case "R":
                    this.back();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

            System.out.println();

        } while (!option.equals("S"));
    }

    private void searchByGTIN() {
        this.nextPage(BuscarProdutoView.INSTANCE);
    }

    private void listAllProducts() {
        this.nextPage(ListarProdutosView.INSTANCE);
    }

    private void newProducts() {
        this.nextPage(CadastroProdutoView.INSTANCE);
    }
}