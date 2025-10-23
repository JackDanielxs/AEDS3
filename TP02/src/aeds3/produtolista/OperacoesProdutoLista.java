package aeds3.produtolista;

import java.util.ArrayList;
import java.util.List;

import aeds3.Arquivo;
import aeds3.ArvoreBMais;
import aeds3.ParIdId;
import model.ProdutoListaPresente;

public final class OperacoesProdutoLista extends Arquivo<ProdutoListaPresente> {
    private ArvoreBMais<ParIdId> IdxIndiretoLista;
    private ArvoreBMais<ParIdId> IdxIndiretoProduto; 

    public int create(final ProdutoListaPresente lista) throws Exception {
        int id = super.create(lista);
        this.IdxIndiretoLista.create(new ParIdId(lista.getIdListaPresente(), id));
        this.IdxIndiretoProduto.create(new ParIdId(lista.getId(), id));
        return id;
    }

    public List<ProdutoListaPresente> getListasPresenteByIdProduto(int idProduto) throws Exception {
        List<ProdutoListaPresente> ProdutoLista = new ArrayList<ProdutoListaPresente>();
        ParIdId target = new ParIdId(idProduto, -1);
        List<ParIdId> pares = this.IdxIndiretoProduto.read(target);
        for (ParIdId p : pares) {
            ProdutoListaPresente pl = super.read(p.getID2());
            if (pl != null) { ProdutoLista.add(pl); }
        }
        return ProdutoLista;
    }

    public List<ProdutoListaPresente> getProdutosByIdListaPresente(int idLista) throws Exception {
        List<ProdutoListaPresente> ProdutoLista = new ArrayList<ProdutoListaPresente>();
        ParIdId target = new ParIdId(idLista, -1);
        List<ParIdId> pares = this.IdxIndiretoLista.read(target);
        for (ParIdId p : pares) {
            ProdutoListaPresente pl = super.read(p.getID2());
            if (pl != null) { ProdutoLista.add(pl); }
        }
        return ProdutoLista;
    }

    @Override
    public boolean delete(final int id) throws Exception {
        ProdutoListaPresente lp = this.read(id);
        if(lp == null) return false;
        this.IdxIndiretoProduto.delete(new ParIdId(lp.getId(), id));
        this.IdxIndiretoLista.delete(new ParIdId(lp.getIdListaPresente(), id));
        return super.delete(id);
    }

    public OperacoesProdutoLista() throws Exception {
        super(ProdutoListaPresente.class);
        this.IdxIndiretoLista = new ArvoreBMais<ParIdId>(
            ParIdId.class.getConstructor(),
            5,
            "produtolista/idLista.idProdutoLista"
        );
        this.IdxIndiretoProduto = new ArvoreBMais<ParIdId>(
            ParIdId.class.getConstructor(),
            5,
            "produtolista/idProduto.idProdutoLista"
        );
    }
}
