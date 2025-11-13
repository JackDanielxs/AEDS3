package view.produto;

import java.util.List;
import java.util.function.Consumer;
import controller.ProdutoController;
import model.Produto;
import util.IsNumber;
import view.View;

public final class BuscarPorNome extends View {
    public static final BuscarPorNome INSTANCE = new BuscarPorNome();
    private Consumer<Produto> function = null;

    private BuscarPorNome() {
        super("Buscar produto por nome", false);
        this.function = (product) -> handleListSelection(product);
    }

    public BuscarPorNome setFunction(Consumer<Produto> function) {
        this.function = function;
        return this;
    }

    @Override
    protected void viewDisplay() {
        String helper = """
                Busque um produto pelo nome dele.
                Insira 'R' para voltar.
                Opção: """;
        System.out.println(helper);
        String name = scanner.nextLine().trim().toLowerCase();

        if (name == null || name.isBlank()) {
            this.alertMessage("Nome Inválido");
            return;
        } else if (name.equals("R")) return;

        List<Produto> products = ProdutoController.INSTANCE.getByName(name);
        if (products != null && !products.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("\nForam encontrados %d produtos com o nome [%s]:\n", products.size(), name));

            for (int i = 0; i < products.size(); i++) {
                Produto p = products.get(i);
                sb.append(
                        String.format("(%d) %s %s\n",
                                i + 1,
                                p.getName(),
                                p.isActive() ? "" : "(Desativado)"));
            }
            String option;
            do {
                System.out.printf(
                        """
                                %s
                                (R) Voltar

                                Opção: """, sb.toString());

                option = scanner.nextLine().trim().toUpperCase();

                switch (option) {
                    case "R":
                        this.back();
                        break;
                    default:
                        if (IsNumber.validate(option)) {
                            int listNumber = Integer.parseInt(option);
                            if (listNumber >= 1 && listNumber <= products.size()) {
                                this.function.accept(products.get(listNumber - 1));
                            }
                        } else { System.out.println("Opção inválida."); }
                        break;
                }

                System.out.println();

            } while (!option.equals("R"));
        } else { this.alertMessage("Produto com nome [%s] não encontrado", name); }
    }

    private void handleListSelection(final Produto product) {
        this.nextPage(DetalhesProdutoView.INSTANCE.set(product.getId()));
    }

}
