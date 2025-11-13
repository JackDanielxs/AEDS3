package view.listapresente;

import aeds3.Memoria;
import controller.ListaPresenteController;
import controller.UsuarioController;
import model.ListaPresente;
import model.Usuario;
import view.View;
import view.produtolista.ProdutoListaView;

public final class DetalhesListaPresenteView extends View {
    public static final DetalhesListaPresenteView INSTANCE = new DetalhesListaPresenteView();
    private int id = -1;
    private ListaPresente giftList;
    private Usuario user;

    private DetalhesListaPresenteView() {
        super("Detalhes da lista", true);
    }

    public DetalhesListaPresenteView set(final int id) {
        this.giftList = ListaPresenteController.INSTANCE.getById(id);
        this.user = UsuarioController.INSTANCE.getById(giftList.getUserId());
        this.id = id;
        this.viewName = giftList.getName();
        return this;
    }

    public void viewDisplay() {
        String option;

        do {
            this.set(id);
            this.reload();
            if (Memoria.getUserId() != user.getId()) {
                System.out.println(String.format("Responsável pela lista: %s\n", user.getName()));
            }
            System.out.printf("""
                    Nome: %s
                    Descrição: %s
                    Data de criação: %s
                    Data de expiração: %s
                    Status: %s
                    Código: %s

                    (1) Editar lista (Produtos)
                    (2) Editar lista (Dados)
                    (3) %s lista

                    (R) Voltar

                    Opção: """,
                    
                    giftList.getName(),
                    giftList.getDescription(),
                    giftList.getCreatedAt(),
                    giftList.getExpirationDateFormated(),
                    giftList.isActive() ? "Ativa" : "Inativa",
                    giftList.getCode(),
                    giftList.isActive() ? "Inativar" : "Reativar");

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    manageProducts();
                    break;
                case "2":
                    editListData();
                    break;
                case "3":
                    if (giftList.isActive())
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

    private void manageProducts() {
        this.nextPage(ProdutoListaView.INSTANCE.set(id));
    }

    private void editListData() {
        this.nextPage(EditarListaPresenteView.INSTANCE.set(id));
    }

    private void reativar() {
        ListaPresenteController.INSTANCE.reativar(id);
    }

    private void inativar() {
        ListaPresenteController.INSTANCE.inativar(id);
    }
}
