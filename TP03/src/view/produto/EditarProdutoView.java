package view.produto;

import controller.ProdutoController;
import model.Produto;
import util.CampoObrigatorio;
import view.View;

public final class EditarProdutoView extends View {
    public static final EditarProdutoView INSTANCE = new EditarProdutoView();
    private int productId = -1;

    private EditarProdutoView() {
        super("Editar Produto", true);
    }

    public EditarProdutoView setProductId(final int productId) {
        this.productId = productId;
        return this;
    }

    @Override
    public void viewDisplay() {
        Produto product = ProdutoController.INSTANCE.getById(productId);
        if (product == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String name = scanner.nextLine();
        if (CampoObrigatorio.isBlank(name)) {
            name = product.getName();
        }

        System.out.print("Nova descrição: ");
        String description = scanner.nextLine();
        if (CampoObrigatorio.isBlank(description)) {
            description = product.getDescription();
        }

        String phrase = product.isActive() ? "inativar" : "reativar";
        System.out.printf("Deseja %s: (S/N)", phrase);
        String confirmation = scanner.nextLine();
        boolean newStatus = product.isActive();
        if (confirmation.toUpperCase().equals("S")) {
            newStatus = !newStatus;
        }

        ProdutoController.INSTANCE.update(
                Produto.from(
                        name,
                        description,
                        product.getGtin(),
                        product.getId(),
                        newStatus));

        this.alertMessage("Produto editado.");
    }
}
