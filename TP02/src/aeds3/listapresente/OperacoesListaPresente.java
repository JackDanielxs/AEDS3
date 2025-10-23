package aeds3.listapresente;

import java.util.ArrayList;
import java.util.List;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.HashExtensivel;
import aeds3.ParIdId;
import model.ListaPresente;

public class OperacoesListaPresente extends Arquivo<ListaPresente> {
    private ArvoreBMais<ParIdId> idxIndiretoUsuario;
    private HashExtensivel<ParIdCodigo> idxIndireto;

    public int create(final ListaPresente list) throws Exception {
        int id = super.create(list);
        this.idxIndireto.create(ParIdCodigo.create(list.getId(), list.getCodCompartilha()));
        this.idxIndiretoUsuario.create(new ParIdId(list.getIdUsuario(), id));
        return id;
    }

    public List<ListaPresente> getByIdUsuario(int idUsuario) throws Exception {
        List<ListaPresente> listas = new ArrayList<ListaPresente>();
        ParIdId par = new ParIdId(idUsuario, -1);
        List<ParIdId> pares = this.idxIndiretoUsuario.read(par);
        for (ParIdId p : pares) {
            ListaPresente lista = super.read(p.getID2());
            if (lista != null) listas.add(lista);
        }
        return listas;
    }

    public List<ListaPresente> getByIds(final List<Integer> ids) throws Exception {
        return ids.stream().map(t -> {
            try {
                return read(t);
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }).toList();
    }

    public ListaPresente getByCodigo(final String codigo) throws Exception {
        int id = -1;
        ListaPresente lista = null;

        try {
            ParIdCodigo par = this.idxIndireto.read(codigo.hashCode());

            if (par == null) return null;
            id = par.getId();

            lista = super.read(id);
        } catch (final Exception e) { System.out.println(e.getMessage()); }

        return lista;
    }

    @Override
    public boolean delete(final int id) throws Exception {
        ListaPresente gf = this.read(id);
        if(gf == null) return false;
        this.idxIndireto.delete(gf.getCodCompartilha().hashCode());
        this.idxIndiretoUsuario.delete(new ParIdId(gf.getIdUsuario(), id));
        return super.delete(id);
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
