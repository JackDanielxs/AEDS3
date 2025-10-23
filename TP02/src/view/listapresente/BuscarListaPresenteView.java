package view.listapresente;

import controller.ListaPresenteController;
import model.ListaPresente;
import util.CampoObrigatorio;
import view.View;

public final class BuscarListaPresenteView extends View {
    public static final BuscarListaPresenteView INSTANCE = new BuscarListaPresenteView();

    private BuscarListaPresenteView() {
        super("Buscar lista", true);
    }

    @Override
    public void viewDisplay() {
        String helper = """
                Busque uma lista de presente pelo código único dela.
                Insira 'R' para voltar.
                Opção: """;
        System.out.println(helper);
        String cod = scanner.nextLine().trim();

        if (CampoObrigatorio.isBlank(cod) || (!cod.equals("R") && cod.length() < 10)) {
            this.alertMessage("Código Inválido");
            return;
        } else if (cod.equals("R")) { return; }

        ListaPresente foundList = ListaPresenteController.INSTANCE.getByCodCompartilha(cod);
        if (foundList != null)
            this.nextPage(DetalhesListaPresenteView.INSTANCE.set(foundList.getId()));
        else 
            this.alertMessage("Lista com código [%s] não encontrada", cod);
        return;
    }
}
