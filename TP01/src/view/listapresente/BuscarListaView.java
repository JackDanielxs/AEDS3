package view.listapresente;

import controller.ListaPresenteController;
import model.ListaPresente;
import util.CampoObrigatorio;
import view.View;

public class BuscarListaView extends View {
    public static final BuscarListaView INSTANCE = new BuscarListaView();

    private BuscarListaView() {
        super("Buscar lista", true);
    }

    @Override
    public void viewDisplay() {
        String helper = """
                Busque uma lista de presente pelo código único dela.
                Insira 'R' para voltar.
                Opção: """;
        System.out.println(helper);
        String cod = sc.nextLine().trim();

        if(CampoObrigatorio.isNotValid(cod) || (!cod.equals("R") && cod.length() < 10)){
            this.alertMessage("Codigo Inválido");
            return;
        } else if(cod.equals("R")){ return; }

        ListaPresente foundList = ListaPresenteController.INSTANCE.getByCodigo(cod);
        
        if(foundList != null) 
            this.nextPage(DetalhesLista.INSTANCE.set(foundList.getId()));
        else
            this.alertMessage("Lista com código [%s] não encontrada", cod);
        return;
    }
}

