package controller;

import java.util.List;

import aeds3.produto.OperacoesProduto;
import model.Produto;

public final class ProdutoController {
    public static final ProdutoController INSTANCE = new ProdutoController();
    private OperacoesProduto ops;

    private ProdutoController() {
        try { this.ops = new OperacoesProduto(); } 
        catch (final Exception e) { e.printStackTrace(); }
    }

    public Produto getByGTIN(final String gtin) {
        try { return ops.getByGTIN(gtin); } 
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public Produto getById(final int id) {
        try { return this.ops.read(id); }
         catch (final Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Produto> findAll() {
        try { return this.ops.read(); } 
        catch (Exception e) { e.printStackTrace(); }
        return List.of();
    }

    public int create(final String nome, final String desc, final String gtin) {
        try {
            Produto p = this.getByGTIN(gtin);
            if (p != null)
                return -1;
            Produto produto = Produto.create(nome, desc, gtin);
            return ops.create(produto);
        } catch (final Exception e) { return -1; }
    }

    public boolean toggleStatus(final int id, final boolean active) {
        try {
            Produto p = this.getById(id);
            p.changeStatus(active);
            return ops.update(p);
        } catch (Exception e) { return false; }
    }

    public boolean reativar(int id) {
        return this.toggleStatus(id, true);
    }

    public boolean inativar(int id) {
        return this.toggleStatus(id, false);
    }

    public boolean update(final Produto p) {
        try { return ops.update(p); } 
        catch (Exception e) { return false; }
    }

    public List<Produto> getByName(final String nome) {
        try { return this.ops.getByName(nome); } 
        catch (Exception e) { return List.of(); }
    }
}
