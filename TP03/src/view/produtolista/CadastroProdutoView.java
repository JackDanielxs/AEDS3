package view.produtolista;

import controller.ProdutoListaPresenteController;
import model.Produto;
import view.View;
import view.produto.BuscarPorGTIN;
import view.produto.BuscarPorNome;

public final class CadastroProdutoView extends View {
    public static final CadastroProdutoView INSTANCE = new CadastroProdutoView();

    private CadastroProdutoView() {
        super("Incluir produto", true);
    }

    private int giftListId = -1;

    public CadastroProdutoView set(final int giftListId) {
        this.giftListId = giftListId;
        return this;
    }

    @Override
    public void viewDisplay() {
        String option;

        do {
            String menu = """
                    (1) Buscar produtos (GTIN)
                    (2) Buscar produtos (Nome)
                    (3) Listar produtos

                    (R) Voltar

                    Opção: """;
            System.out.print(menu);

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    searchByGTIN();
                    break;
                case "2":
                    searchByName();
                    break;
                case "3":
                    listAll();
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

    private void searchByGTIN() {
        this.nextPage(BuscarPorGTIN.INSTANCE.setFunction(product -> create(product)));
    }

    private void listAll() {
        this.nextPage(AdicionarProdutoListaView.INSTANCE.setGiftListId(giftListId));
    }

    private void searchByName() {
        this.nextPage(BuscarPorNome.INSTANCE.setFunction(product -> create(product)));
    }

    private void create(final Produto product) {
        if(!product.isActive()) {
            this.alertMessage("Produto inativado.");
            return;
        }
        int newId = ProdutoListaPresenteController.INSTANCE.create(giftListId, product.getId());
        if (newId != -1) this.nextPage(DetalhesProdutoLista.INSTANCE.set(newId));
        else this.alertMessage("Produto já existente nessa lista.");
    }
}
