package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import aeds3.Memoria;
import aeds3.listapresente.OperacoesListaPresente;
import model.ListaPresente;

public final class ListaPresenteController {
    public final static ListaPresenteController INSTANCE = new ListaPresenteController();
    private OperacoesListaPresente ops;

    private ListaPresenteController() {
        try {
            this.ops = new OperacoesListaPresente();
        } catch (final Exception e) { e.printStackTrace(); }
    }

    public List<ListaPresente> getListasPresenteByUsuario(final int idUsuario) {
        try {
            return this.ops.getByIdUsuario(idUsuario);
        } catch (final Exception e) { return List.of(); }
    }

    public ListaPresente getById(final int id) {
        try {
            return this.ops.read(id);
        } catch (final Exception e) { return null; }
    }

    public ListaPresente getByCodCompartilha(final String codigo) {
        try {
            return ops.getByCodigo(codigo);
        } catch (final Exception e) { return null; }
    }

    public int create(
            final String nome,
            final String descricao,
            final Optional<LocalDate> dtExpiracao) {
        try {
            ListaPresente lista = ListaPresente.create(
                    nome,
                    descricao,
                    dtExpiracao,
                    Memoria.getIdUsuario());
            return ops.create(lista);
        } catch (final Exception e) { return -1; }
    }

    public boolean update(final ListaPresente lista) {
        try {
            return ops.update(lista);
        } catch (Exception e) { return false; }
    }

    public boolean delete(final int idLista) {
        try {
            return ops.delete(idLista);
        } catch (Exception e) { return false; }
    }

    public boolean changeStatus(final int id, final boolean status) {
        try {
            ListaPresente lista = this.getById(id);
            lista.updateStatus(status);
            return ops.update(lista);
        } catch (Exception e) { return false; }
    }

    public boolean reativar(final int id) {
        return this.changeStatus(id, true);
    }

    public boolean inativar(final int id) {
        return this.changeStatus(id, false);
    }

    public boolean toggleStatusUsuario(final boolean status) {
        try {
            List<ListaPresente> listas = this.getListasPresenteByUsuario(Memoria.getIdUsuario());
            return listas.stream().allMatch(v -> {
                v.updateStatus(status);
                try {
                    return ops.update(v);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (final Exception e) { return false; }
    }
}
