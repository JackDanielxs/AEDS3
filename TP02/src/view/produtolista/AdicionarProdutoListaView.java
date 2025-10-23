package view.produtolista;

import java.util.List;

import controller.ProdutoController;
import controller.ProdutoListaPresenteController;
import model.Produto;
import util.IsNumber;
import view.View;

public final class AdicionarProdutoListaView extends View {
    public static final AdicionarProdutoListaView INSTANCE = new AdicionarProdutoListaView();
    private int OFFSET = 0;
    private int MAX = 10;
    private List<Produto> list = List.of();
    private int maxPage = 0;
    private int page = 0;
    private int giftListId = -1;

    private AdicionarProdutoListaView() {
        super("Listagem", true);
        OFFSET = 0;
        list = ProdutoController.INSTANCE.getAll();
        MAX = 10;
        maxPage = list.size() > 0 ? (int) Math.ceil((list.size() / (double) MAX)) : 0;
        page = maxPage > 0 ? 1 : 0;
    }

    public AdicionarProdutoListaView setGiftListId(final int giftListId) {
        this.giftListId = giftListId;
        return this;
    }

    @Override
    protected void viewDisplay() {
        String option;

        do {
            StringBuilder menuBuilder = new StringBuilder(String.format(
                    "Página %d de %d \n\n", page, maxPage));

            if (list.isEmpty()) {
                menuBuilder.append("Nenhum Produto encontrado.\n");
            } else {
                for (int i = OFFSET; i < Math.min(OFFSET + MAX, list.size()); i++) {
                    Produto product = list.get(i);
                    menuBuilder.append(
                            String.format("(%d) %s %s\n", i + 1,
                                    product.getNome(),
                                    product.isActive() ? "" : "(Desativado)"));
                }
            }

            menuBuilder.append(String.format(
                    "%s%s",
                    (page > 1) ? "\n(A) Página anterior\n" : "",
                    (page < maxPage) ? "\n(P) Próxima página" : ""));

            System.out.printf(
                    """
                            %s
                            (R) Voltar

                            Opção: """, menuBuilder.toString());

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "A":
                    previousPageList();
                    break;
                case "P":
                    nextPageList();
                    break;
                case "R":
                    this.back();
                    break;
                default:
                    if (IsNumber.validate(option)) {
                        int listNumber = Integer.parseInt(option);
                        if (listNumber >= 1 && listNumber <= list.size()) {
                            handleListSelection(list.get(listNumber - 1));
                        }
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
            }

            System.out.println();

        } while (!option.equals("R"));

    }

    private void handleListSelection(final Produto product) {
        int newId = ProdutoListaPresenteController.INSTANCE.create(giftListId, product.getId());
        if(newId != -1){
            this.nextPage(DetalhesProdutoLista.INSTANCE.set(newId));
        } else {
            this.alertMessage("Error while adding the product");
        }
    }

    private void previousPageList() {
        if (OFFSET >= MAX) {
            OFFSET -= MAX;
            page--;
        }
    }

    private void nextPageList() {
        if (OFFSET + MAX < list.size()) {
            OFFSET += MAX;
            page++;
        }
    }

}
