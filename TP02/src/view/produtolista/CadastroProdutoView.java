package view.produtolista;

import controller.ProdutoController;
import controller.ProdutoListaPresenteController;
import model.Produto;
import util.CampoObrigatorio;
import view.View;

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
                    (1) Buscar produtos
                    (2) Consultar produtos

                    (R) Voltar

                    Opção: """;
            System.out.print(menu);

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    getByGTIN();
                    break;
                case "2":
                    getAll();
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

    private void getByGTIN() {
        try {
            System.out.println("Insira o código GTIN: ");
            String gtin = CampoObrigatorio.requireMinSize(scanner.nextLine().trim(), 13);

            Produto product = ProdutoController.INSTANCE.getByGTIN(gtin);
            if (product == null) {
                this.alertMessage("Produto não encontrado.");
                return;
            }

            int newId = ProdutoListaPresenteController.INSTANCE.create(giftListId, product.getId());
            if(newId != -1){
                this.nextPage(DetalhesProdutoLista.INSTANCE.set(newId));
            } else {
                this.alertMessage("Produto já existente nessa lista.");
            }
        } catch (IllegalArgumentException e) {
            this.alertMessage("GTIN inválido.");
        } catch (Exception e) {
            this.alertMessage("Erro ao buscar o produto.");
        }
    }

    private void getAll() {
        this.nextPage(AdicionarProdutoListaView.INSTANCE.setGiftListId(giftListId));
    }
}
