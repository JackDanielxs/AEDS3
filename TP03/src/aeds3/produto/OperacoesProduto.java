package aeds3.produto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import aeds3.Arquivo;
import aeds3.HashExtensivel;
import aeds3.ListaInvertida;
import aeds3.Buscar;
import model.Produto;

public final class OperacoesProduto extends Arquivo<Produto> {
    private HashExtensivel<ParIdGTIN> idxIndireto;
    private ListaInvertida listaInvertida;
    private Buscar search;    

    public int create(final Produto produto) throws Exception {
        int id = super.create(produto);

        this.idxIndireto.create(ParIdGTIN.create(id, produto.getGtin()));
        this.search.create(produto.getName().toLowerCase(), id);

        return id;
    }

    public boolean update(final Produto produto) {
        try {
            Produto antigo = this.read(produto.getId());
            if (antigo == null)
                return false;
                
            if (!antigo.getName().equals(produto.getName())) {
                this.search.delete(antigo.getName(), produto.getId());
                this.search.delete(produto.getName(), produto.getId());
            }
            return super.update(produto);
        } catch (Exception e) { return false; }
    }

    public boolean delete(final int id) throws Exception {
        Produto produto = this.read(id);
        if (produto == null)
            return false;

        this.idxIndireto.delete(produto.getGtin().hashCode());
        this.search.delete(produto.getName().toLowerCase(), id);
        return super.delete(id);
    }

    public Produto getByGTIN(final String gtin) throws Exception {
        int id = -1;
        Produto p = null;

        try {
            ParIdGTIN par = this.idxIndireto.read(gtin.hashCode());
            if (par == null)
                return null;

            id = par.getId();
            p = super.read(id);
        } catch (Exception e) { System.out.println(e.getMessage()); }

        return p;
    }

    public int count() throws Exception { return this.read().size(); }    

    public List<Produto> getByName(final String nome) throws Exception {
        List<Integer> ids = this.search.search(nome);

        return ids.stream()
                .map(t -> {
                    try { return super.read(t); } catch (Exception e) { e.printStackTrace(); }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public OperacoesProduto() throws Exception {
        super(Produto.class);
        this.idxIndireto = new HashExtensivel<ParIdGTIN>(
                ParIdGTIN.class.getConstructor(),
                5,
                "produto/id.gtin",
                "produto/id.gtin"
            );
        this.listaInvertida = new ListaInvertida(
                5, "produto/nome.d", "produto/nome.b"
            );
        this.search = new Buscar(this.listaInvertida);
    }
}