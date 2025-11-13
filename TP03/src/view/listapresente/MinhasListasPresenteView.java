package view.listapresente;

import java.util.List;

import aeds3.Memoria;
import controller.ListaPresenteController;
import model.ListaPresente;
import util.IsNumber;
import view.View;

public final class MinhasListasPresenteView extends View {
    public static final MinhasListasPresenteView INSTANCE = new MinhasListasPresenteView();

    private MinhasListasPresenteView() {
        super("Minhas listas", true);
    }

    @Override
    public void viewDisplay() {
        List<ListaPresente> list = ListaPresenteController.INSTANCE.getAllByIdUsuario(Memoria.getUserId());

        StringBuilder menuBuilder = new StringBuilder("");

        if (list.isEmpty()) {
            menuBuilder.append("Nenhuma lista encontrada.\n");
        } else {
            for (int i = 0; i < list.size(); i++) {
                ListaPresente giftList = list.get(i);
                menuBuilder.append(
                        String.format("(%d) %s %s %s\n", i + 1,
                                giftList.getName(),
                                giftList.getExpirationDateFormated("-"),
                                giftList.isActive() ? "" : "(Desativado)"));
            }
        }

        String option;

        do {
            System.out.printf(
                    """
                            %s

                            (N) Criar lista
                            (R) Voltar

                            Opção: """, menuBuilder.toString());

            option = scanner.nextLine().trim().toUpperCase();

            switch (option) {
                case "N":
                    createNewList();
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

    private void handleListSelection(final ListaPresente list) {
        this.nextPage(DetalhesListaPresenteView.INSTANCE.set(list.getId()));
    }

    private void createNewList() {
        this.nextPage(CadastroListaPresenteView.INSTANCE);
    }
}
