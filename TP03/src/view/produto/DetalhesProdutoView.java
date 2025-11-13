package view.produto;

import java.util.List;
import java.util.stream.Collectors;

import aeds3.Memoria;
import controller.ProdutoController;
import controller.ProdutoListaPresenteController;
import model.ListaPresente;
import model.Produto;
import util.Par;
import view.View;

public final class DetalhesProdutoView extends View {
    private int id = -1;
    private Produto product;
    public static final DetalhesProdutoView INSTANCE = new DetalhesProdutoView();

    private DetalhesProdutoView() {
        super("Detalhes do produto", true);
    }

    public DetalhesProdutoView set(final int id) {
        this.product = ProdutoController.INSTANCE.getById(id);
        this.id = id;
        this.viewName = product.getName();
        return this;
    }

    @Override
    protected void viewDisplay() {
        String option;
        List<ListaPresente> found = ProdutoListaPresenteController.INSTANCE.getAllByIdProduto(id).stream().map(Par::getSecond)
                .toList();
        List<ListaPresente> mine = found.stream().filter(gift -> gift.getUserId() == Memoria.getUserId()).toList();
        String list = "";
        if (mine.size() > 0) {
            list = "Aparece nas listas:\n" +
                    mine.stream().map(l -> "- " + l.getName() + " (" + (l.isActive() ? "Ativado" : "Desativado") + ")")
                            .collect(Collectors.joining("\n"));
        }

        do {
            this.set(id);
            this.reload();
            System.out.printf("""
                    Nome: %s
                    Descrição: %s
                    Status: %s
                    Código GTIN: %s

                    %s
                    Aparece em %d outras listas.

                    (1) Editar produto
                    (2) %s

                    (R) Voltar

                    Opção: """,
                    
                    product.getName(),
                    product.getDescription(),
                    product.isActive() ? "Ativo" : "Inativo",
                    product.getGtin(),
                    list,
                    found.size() - mine.size(),
                    product.isActive() ? "Inativar" : "Reativar");

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    editProductData();
                    break;
                case "2":
                    if (product.isActive())
                        inativar();
                    else
                        reativar();
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

    private void editProductData() {
        this.nextPage(EditarProdutoView.INSTANCE.setProductId(id));
    }

    private void reativar() {
        ProdutoController.INSTANCE.reativar(product.getId());
    }

    private void inativar() {
        ProdutoController.INSTANCE.inativar(product.getId());
    }
}
