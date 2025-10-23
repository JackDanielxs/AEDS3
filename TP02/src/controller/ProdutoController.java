package controller;

import java.util.List;

import aeds3.produto.OperacoesProduto;
import model.Produto;

public final class ProdutoController {
    public static final ProdutoController INSTANCE = new ProdutoController();
    private OperacoesProduto ops;

    private ProdutoController() {
        try {
            this.ops = new OperacoesProduto();
        } catch (final Exception e) { e.printStackTrace(); }
    }

    public Produto getByGTIN(final String gtin) {
        try {
            return ops.getByGTIN(gtin);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public Produto getById(final int id) {
        try {
            return this.ops.read(id);
        } catch (final Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Produto> getAll() {
        try {
            return this.ops.read();
        } catch (Exception e) { e.printStackTrace(); }
        return List.of();
    }

    public int create(final String name, final String description, final String gtin) {
        try {
            Produto p = this.getByGTIN(gtin);
            if(p != null) return -1;
            Produto product = Produto.create(name, description, gtin);
            return ops.create(product);
        } catch (final Exception e) { return -1; }
    }

    public boolean updateStatus(final int id, final boolean active) {
        try {
            Produto product = this.getById(id);
            product.updateStatus(active);
            return ops.update(product);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean reativar(int id) { return this.updateStatus(id, true); }

    public boolean inativar(int id) { return this.updateStatus(id, false); }

    public boolean update(final Produto product) {
        try {
            return ops.update(product);
        } catch (Exception e) { return false; }
    }
}
