package aeds3.listapresente;

import java.util.ArrayList;
import java.util.List;

import aeds3.ArvoreBMais;
import aeds3.Arquivo;
import aeds3.HashExtensivel;
import aeds3.ParIdId;
import model.ListaPresente;

public final class OperacoesListaPresente extends Arquivo<ListaPresente> {
    private ArvoreBMais<ParIdId> idxIndiretoUsuario;
    private HashExtensivel<ParIdCodigo> idxIndireto;

    public int create(final ListaPresente list) throws Exception {
        int id = super.create(list);
        this.idxIndireto.create(ParIdCodigo.create(list.getId(), list.getCode()));
        this.idxIndiretoUsuario.create(new ParIdId(list.getUserId(), id));
        return id;
    }

    @Override
    public boolean delete(final int id) throws Exception {
        ListaPresente lista = this.read(id);
        if(lista == null) return false;
        this.idxIndireto.delete(lista.getCode().hashCode());
        this.idxIndiretoUsuario.delete(new ParIdId(lista.getUserId(), id));
        return super.delete(id);
    }

    public List<ListaPresente> getAllByIdUsuario(int idUsuario) throws Exception {
        List<ListaPresente> listas = new ArrayList<ListaPresente>();
        ParIdId target = new ParIdId(idUsuario, -1);
        List<ParIdId> pares = this.idxIndiretoUsuario.read(target);
        for (ParIdId par : pares) {
            ListaPresente lista = super.read(par.getID2());
            if (lista != null) 
                listas.add(lista);
        }
        return listas;
    }

    public List<ListaPresente> getAllById(final List<Integer> ids) throws Exception {
        return ids.stream().map(t -> {
            try { return read(t); } catch (Exception e) { e.printStackTrace(); }
            return null;
        }).toList();
    }

    public ListaPresente getByCodCompartilha(final String codigo) throws Exception {
        int id = -1;
        ListaPresente lista = null;

        try {
            ParIdCodigo par = this.idxIndireto.read(codigo.hashCode());

            if (par == null)
                return null;

            id = par.getId();
            lista = super.read(id);
        } catch (final Exception e) { System.out.println(e.getMessage()); }

        return lista;
    }

    public OperacoesListaPresente() throws Exception {
        super(ListaPresente.class);
        this.idxIndiretoUsuario = new ArvoreBMais<ParIdId>(
            ParIdId.class.getConstructor(),
            5,
            "listapresente/idUsuario.idLista"
        );
        this.idxIndireto = new HashExtensivel<ParIdCodigo>(
            ParIdCodigo.class.getConstructor(),
            5,
            "listapresente/id.codigo",
            "listapresente/id.codigo"
        );
    }
}
