package view.produtolista;

import controller.ProdutoController;
import controller.ProdutoListaPresenteController;
import model.Produto;
import model.ProdutoListaPresente;
import util.IsNumber;
import view.View;

public final class DetalhesProdutoLista extends View {
    private int productGiftListId = -1;
    private Produto product;
    private ProdutoListaPresente productGiftList;
    public static final DetalhesProdutoLista INSTANCE = new DetalhesProdutoLista();

    private DetalhesProdutoLista() {
        super("Detalhes do produto", true);
    }

    public DetalhesProdutoLista set(final int productGiftListId) {
        this.productGiftList = ProdutoListaPresenteController.INSTANCE.getById(productGiftListId);
        this.productGiftListId = productGiftListId;

        this.product = ProdutoController.INSTANCE.getById(productGiftList.getId());

        this.viewName = product.getNome();

        return this;
    }

    @Override
    protected void viewDisplay() {
        String option;

        do {
            this.set(productGiftListId);
            this.reload();

            System.out.printf("""
                    Nome: %s
                    Código GTIN-13: %s
                    Descrição: %s
                    Quantidade: %d

                    (1) Alterar a quantidade
                    (2) Remover esse produto da lista

                    (R) Retornar ao menu anterior

                    Opção: """,
                    product.getNome(),
                    product.getGtin(),
                    product.getDescricao(),
                    productGiftList.getQuantidade()
            );

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    changeQuantity();
                    break;
                case "2":
                    remove();
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

    private void remove() {
        boolean ok = ProdutoListaPresenteController.INSTANCE.delete(productGiftListId);
        if (ok) {
            System.out.println("Produto removido.");
            this.back();
        } else { System.out.println("Erro ao remover o produto."); }
    }

    private void changeQuantity() {
        System.out.println("Digite a nova quantidade: ");
        String newQ = scanner.nextLine().trim().toUpperCase();
        if (IsNumber.validate(newQ)) {
            int nq = Integer.parseInt(newQ);
            if (nq < 0)
                System.out.println("Insira um valor válido");
            productGiftList.setQuantidade(nq);
            ProdutoListaPresenteController.INSTANCE.update(productGiftList);
        } else { System.out.println("Insira um valor válido"); }
    }
}
