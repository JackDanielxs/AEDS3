package view.produtolista;

import java.util.List;

import controller.ProdutoListaPresenteController;
import model.Produto;
import model.ProdutoListaPresente;
import util.IsNumber;
import util.Par;
import view.View;

public final class ProdutoListaView extends View {
    public static final ProdutoListaView INSTANCE = new ProdutoListaView();
    private int giftListId = -1;
    private List<Par<ProdutoListaPresente, Produto>> list = List.of();

    private ProdutoListaView() {
        super("Produtos", true);
    }

    public ProdutoListaView set(final int id) {
        this.giftListId = id;
        return this;
    }

    @Override
    protected void viewDisplay() {
        String option;

        do {
            list = ProdutoListaPresenteController.INSTANCE.getAllByIdListaPresente(giftListId);
            StringBuilder menuBuilder = new StringBuilder();
            if (list.isEmpty()) {
                menuBuilder.append("Nenhum Produto encontrado.\n");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    ProdutoListaPresente productGiftList = list.get(i).getFirst();
                    Produto product = list.get(i).getSecond();
                    menuBuilder.append(
                            String.format("(%d) %s (x%d)\n", i + 1,
                                    product.getName(),
                                    productGiftList.getQuantity()));
                }
            }

            System.out.printf(
                    """
                            %s
                            (A) Acrescentar produto
                            (R) Voltar

                            Opção: """, menuBuilder.toString());

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "A":
                    addProduct();
                    break;
                case "R":
                    this.back();
                    break;
                default:
                    if (IsNumber.validate(option)) {
                        int listNumber = Integer.parseInt(option);
                        if (listNumber >= 1 && listNumber <= list.size()) {
                            handleListSelection(list.get(listNumber - 1).getFirst());
                        }
                    } else { System.out.println("Opção inválida."); }
                    break;
            }

            System.out.println();

        } while (!option.equals("R"));
    }

    private void addProduct() {
        this.nextPage(CadastroProdutoView.INSTANCE.set(giftListId));
    }

    private void handleListSelection(final ProdutoListaPresente product) {
        this.nextPage(DetalhesProdutoLista.INSTANCE.set(product.getId()));
    }
}
