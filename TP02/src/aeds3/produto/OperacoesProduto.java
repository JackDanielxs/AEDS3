package aeds3.produto;

import aeds3.Arquivo;
import aeds3.HashExtensivel;
import model.Produto;

public final class OperacoesProduto extends Arquivo<Produto> {
    private HashExtensivel<ParIdGTIN> idxIndireto;

    public int create(final Produto product) throws Exception {
        int id = super.create(product);
        this.idxIndireto.create(ParIdGTIN.create(product.getId(), product.getGtin()));
        return id;
    }

    public Produto getByGTIN(final String gtin) throws Exception {
        int id = -1;
        Produto p = null;

        try {
            ParIdGTIN pair = this.idxIndireto.read(gtin.hashCode());

            if (pair == null) return null;
            id = pair.getId();

            p = super.read(id);
        } catch (Exception e) { System.out.println(e.getMessage()); }

        return p;
    }

    public OperacoesProduto() throws Exception {
        super(Produto.class);
        this.idxIndireto = new HashExtensivel<ParIdGTIN>(
                ParIdGTIN.class.getConstructor(),
                5,
                "produto/id.gtin",
                "produto/id.gtin");
    }
}