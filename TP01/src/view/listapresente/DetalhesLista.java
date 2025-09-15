package view.listapresente;

import controller.ListaPresenteController;
import model.ListaPresente;
import view.View;

public class DetalhesLista extends View {
    public static final DetalhesLista INSTANCE = new DetalhesLista();
    private int id = -1;
    private ListaPresente giftList;

    private DetalhesLista() {
        super("Detalhes da lista", true);
    }

    public DetalhesLista set(final int id) {
        this.giftList = ListaPresenteController.INSTANCE.getById(id);
        this.id = id;
        this.nomeTela = giftList.getNome();
        return this;
    }

    public void viewDisplay() {
        String option;

        do {
            this.set(id);
            this.reload();

            System.out.printf("""
                Nome: %s
                Descrição: %s
                Data de criação: %s
                Data de expiração: %s
                Status: %s
                Código: %s

                (1) ditar lista (Produtos) - **AINDA NÃO IMPLEMENTADO**
                (2) Editar lista (Dados)
                (3) Desativar lista
                
                (R) Voltar
                
                Opção: """,
                giftList.getNome(),
                giftList.getDescricao(),
                giftList.getDtCriacao(),
                giftList.getDtExpiracaoFormatada(),
                giftList.getStatus() ? "Ativa" : "Inativa",
                giftList.getCodigo()
            );

            option = sc.nextLine().trim().toUpperCase();

            switch (option) {
                case "1":
                    // manageProducts();
                    break;
                case "2":
                    editListData();
                    break;
                case "3":
                    inativar();
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

    // private void manageProducts() {}

    private void editListData() {
        this.nextPage(EditarListaView.INSTANCE.setGiftListId(giftList.getId()));
    }

    private void inativar() {
        ListaPresenteController.INSTANCE.inativar(giftList.getId());
    }
}
