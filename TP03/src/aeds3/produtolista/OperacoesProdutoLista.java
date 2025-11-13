package aeds3.produtolista;

import java.util.ArrayList;
import java.util.List;

import aeds3.ArvoreBMais;
import aeds3.Arquivo;
import aeds3.ParIdId;
import model.ProdutoListaPresente;

public final class OperacoesProdutoLista extends Arquivo<ProdutoListaPresente> {
    private ArvoreBMais<ParIdId> IdxIndiretoLista;
    private ArvoreBMais<ParIdId> IdxIndiretoProduto; 

    public int create(final ProdutoListaPresente lista) throws Exception {
        int id = super.create(lista);
        this.IdxIndiretoLista.create(new ParIdId(lista.getGiftListId(), id));
        this.IdxIndiretoProduto.create(new ParIdId(lista.getProductId(), id));
        return id;
    }

    @Override
    public boolean delete(final int id) throws Exception {
        ProdutoListaPresente pl = this.read(id);
        if(pl == null) return false;
        this.IdxIndiretoProduto.delete(new ParIdId(pl.getProductId(), id));
        this.IdxIndiretoLista.delete(new ParIdId(pl.getGiftListId(), id));
        return super.delete(id);
    }

    public List<ProdutoListaPresente> getListasByIdProduto(int idProduto) throws Exception {
        List<ProdutoListaPresente> listasProdutos = new ArrayList<ProdutoListaPresente>();
        ParIdId target = new ParIdId(idProduto, -1);
        List<ParIdId> pares = this.IdxIndiretoProduto.read(target);
        for (ParIdId par : pares) {
            ProdutoListaPresente listaProd = super.read(par.getID2());
            if (listaProd != null)
                listasProdutos.add(listaProd);
        }
        return listasProdutos;
    }

    public List<ProdutoListaPresente> getProdutosByIdLista(int idListaPresente) throws Exception {
        List<ProdutoListaPresente> listasProdutos = new ArrayList<ProdutoListaPresente>();
        ParIdId target = new ParIdId(idListaPresente, -1);
        List<ParIdId> pares = this.IdxIndiretoLista.read(target);
        for (ParIdId par : pares) {
            ProdutoListaPresente listaProd = super.read(par.getID2());
            if (listaProd != null)
                listasProdutos.add(listaProd);
        }
        return listasProdutos;
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
