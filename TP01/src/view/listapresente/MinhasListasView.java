package view.listapresente;

import java.util.List;

import aeds3.Memoria;
import controller.ListaPresenteController;
import model.ListaPresente;
import util.IsNumber;
import view.View;

public class MinhasListasView extends View {
    public static final MinhasListasView INSTANCE = new MinhasListasView();
    private MinhasListasView() {
        super("Minhas listas", true);
    }

    @Override
    public void viewDisplay() {
        List<ListaPresente> list = ListaPresenteController.INSTANCE.getListasByIdUsuario(Memoria.getIdUsuario());

        StringBuilder menuBuilder = new StringBuilder("");

        if (list.isEmpty()) {
            menuBuilder.append("Nenhuma lista encontrada.\n");
        } else {
            for (int i = 0; i < list.size(); i++) {
                ListaPresente giftList = list.get(i);
                menuBuilder.append(
                    String.format("(%d) %s %s\n", i + 1, 
                        giftList.getNome(),
                        giftList.getStatus() ? "" : "(Desativada)"
                    )
                );
            }
        }
        
        String option;

        do {
            System.out.printf(
                """
                %s

                (N) Criar lista
                (R) Voltar

                Opção: """, menuBuilder.toString()
            );

            option = sc.nextLine().trim().toUpperCase();

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
                            handleListSelection(list.get(listNumber-1));
                        }
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
            }

            System.out.println();

        } while (!option.equals("R"));
    }

    private void handleListSelection(ListaPresente list) {
        this.nextPage(DetalhesLista.INSTANCE.set(list.getId()));
    }

    private void createNewList() {
        this.nextPage(CadastroListaView.INSTANCE);
    }
}
