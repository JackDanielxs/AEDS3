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
            return ops.getListasPresenteByIdProduto(idProduto).stream()
                    .flatMap(it -> {
                        try {
                            ListaPresente giftList = opsLista.read(it.getIdListaPresente());
                            return Stream.of(new Par<>(it, giftList));
                        } catch (Exception e) { return Stream.empty(); }
                    })
                    .toList();
        } catch (Exception e) { return List.of(); }
    }

    public List<Par<ProdutoListaPresente, Produto>> getAllByIdLista(final int idLista) {
        try {
            return ops.getProdutosByIdListaPresente(idLista).stream()
                    .flatMap(it -> {
                        try {
                            Produto produto = opsProduto.read(it.getId());
                            return Stream.of(new Par<>(it, produto));
                        } catch (Exception e) { return Stream.empty(); }
                    })
                    .toList();
        } catch (final Exception e) { return List.of(); }
    }

    public ProdutoListaPresente getById(final int idProdutoLista) {
        try {
            return ops.read(idProdutoLista);
        } catch (final Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean update(final ProdutoListaPresente produtoLista) {
        try {
            return ops.update(produtoLista);
        } catch (Exception e) { return false; }
    }

    public boolean delete(final int idProdutoLista) {
        try {
            return ops.delete(idProdutoLista);
        } catch (Exception e) { return false; }
    }

    public int create(final int idLista, final int idProduto) {
        try {
            ProdutoListaPresente list = ProdutoListaPresente.create(idProduto, idLista);
            return ops.create(list);
        } catch (final Exception e) { return -1; }
    }
}