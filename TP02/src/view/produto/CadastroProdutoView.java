package view.produto;

import controller.ProdutoController;
import view.View;

public final class CadastroProdutoView extends View {
    public static final CadastroProdutoView INSTANCE = new CadastroProdutoView();

    private CadastroProdutoView() {
        super("Cadastrar Produto", true);
    }

    @Override
    protected void viewDisplay() {

        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();

        System.out.print("Descrição: ");
        String description = scanner.nextLine().trim();

        System.out.print("Código GTIN: ");
        String gtin = scanner.nextLine().trim();

        int resultId = ProdutoController.INSTANCE.create(name, description, gtin);

        if (resultId == -1) 
            this.alertMessage("Erro ao cadastrar o Produto.");
         else 
            this.alertMessage("Produto cadastrado com sucesso.");
    }
}
