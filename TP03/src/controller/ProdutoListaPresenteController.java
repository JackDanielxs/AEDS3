package controller;

import java.util.List;
import java.util.stream.Stream;

import aeds3.listapresente.OperacoesListaPresente;
import aeds3.produto.OperacoesProduto;
import aeds3.produtolista.OperacoesProdutoLista;
import model.ListaPresente;
import model.Produto;
import model.ProdutoListaPresente;
import util.Par;

public final class ProdutoListaPresenteController {
    public static final ProdutoListaPresenteController INSTANCE = new ProdutoListaPresenteController();
    private OperacoesProdutoLista ops;
    private OperacoesProduto opsProduto;
    private OperacoesListaPresente opsLista;

    private ProdutoListaPresenteController() {
        try {
            this.ops = new OperacoesProdutoLista();
            this.opsProduto = new OperacoesProduto();
            this.opsLista = new OperacoesListaPresente();
        } catch (final Exception e) { e.printStackTrace(); }
    }

    public List<Par<ProdutoListaPresente, ListaPresente>> getAllByIdProduto(final int idProduto) {
        try {
            return ops.getListasByIdProduto(idProduto).stream()
                    .flatMap(it -> {
                        try {
                            ListaPresente lista = opsLista.read(it.getGiftListId());
                            return Stream.of(new Par<>(it, lista));
                        } catch (Exception e) { return Stream.empty(); }
                    }).toList();
        } catch (Exception e) { return List.of(); }
    }

    public List<Par<ProdutoListaPresente, Produto>> getAllByIdListaPresente(final int idListaPresente) {
        try {
            return ops.getProdutosByIdLista(idListaPresente).stream()
                    .flatMap(it -> {
                        try {
                            Produto product = opsProduto.read(it.getProductId());
                            return Stream.of(new Par<>(it, product));
                        } catch (Exception e) { return Stream.empty(); }
                    }).toList();
        } catch (final Exception e) { return List.of(); }
    }

    public ProdutoListaPresente findById(final int listaProdutoId) {
        try { return ops.read(listaProdutoId); } 
        catch (final Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean update(final ProdutoListaPresente listaProduto) {
        try { return ops.update(listaProduto); } 
        catch (Exception e) { return false; }
    }

    public boolean delete(final int listaProdutoId) {
        try { return ops.delete(listaProdutoId); } 
        catch (Exception e) { return false; }
    }

    public int create(final int idLista, final int idProduto) {
        try {
            ProdutoListaPresente list = ProdutoListaPresente.create(idProduto, idLista);
            return ops.create(list);
        } catch (final Exception e) { return -1; }
    }

}
