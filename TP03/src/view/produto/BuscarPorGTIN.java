package view.produto;

import java.util.function.Consumer;

import controller.ProdutoController;
import model.Produto;
import view.View;

public final class BuscarPorGTIN extends View {
    public static final BuscarPorGTIN INSTANCE = new BuscarPorGTIN();
    private Consumer<Produto> function = null;

    private BuscarPorGTIN() {
        super("Buscar produto por GTIN", false);
        this.function = (product) -> handleProduct(product);
    }

    public BuscarPorGTIN setFunction(Consumer<Produto> function) {
        this.function = function;
        return this;
    }

    @Override
    protected void viewDisplay() {
        String helper = """
                Busque um produto pelo código único dele.
                Insira 'R' para voltar.
                Opção: """;
        System.out.println(helper);
        String code = scanner.nextLine().trim();

        if (code == null || code.isBlank() || (!code.equals("R") && code.length() < 13)) {
            this.alertMessage("Código Inválido");
            return;
        } else if (code.equals("R")) return;

        Produto foundProduct = ProdutoController.INSTANCE.getByGTIN(code);
        if (foundProduct != null) this.function.accept(foundProduct);
        else this.alertMessage("Produto com GTIN [%s] não encontrado", code);
        return;
    }

    private void handleProduct(final Produto product) {
        this.nextPage(DetalhesProdutoView.INSTANCE.set(product.getId()));
    }

}
