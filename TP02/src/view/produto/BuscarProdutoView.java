package view.produto;

import controller.ProdutoController;
import model.Produto;
import view.View;

public final class BuscarProdutoView extends View {
    public static final BuscarProdutoView INSTANCE = new BuscarProdutoView();

    private BuscarProdutoView() {
        super("Buscar produto", false);
    }

    @Override
    protected void viewDisplay() {
        String helper = """
                Busque um produto pelo código GTIN dele.
                Insira 'R' para voltar.
                Opção: """;
        System.out.println(helper);
        String code = scanner.nextLine().trim().toUpperCase();

        if(code == null || code.isBlank() || (!code.equals("R") && code.length()<13)){
            this.alertMessage("Código Inválido");
            return;
        } else if(code.equals("R")){ return; }

        Produto foundProduct = ProdutoController.INSTANCE.getByGTIN(code);
        if(foundProduct != null) 
            this.nextPage(DetalhesProdutoView.INSTANCE.set(foundProduct.getId()));
        else
            this.alertMessage("Produto com GTIN %s não encontrado", code);
        return;
    }

    
}
