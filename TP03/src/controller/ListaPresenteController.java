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
        try { this.ops = new OperacoesListaPresente(); }
        catch (final Exception e) { e.printStackTrace(); }
    }

    public List<ListaPresente> getAllByIdUsuario(final int idUsuario) {
        try { return this.ops.getAllByIdUsuario(idUsuario); }
        catch (final Exception e) { return List.of(); }
    }

    public ListaPresente getById(final int id) {
        try { return this.ops.read(id); } 
        catch (final Exception e) { return null; }
    }

    public ListaPresente getByCodCompartilha(final String cod) {
        try { return ops.getByCodCompartilha(cod); }
        catch (final Exception e) { return null; }
    }

    public int create(
            final String name,
            final String description,
            final Optional<LocalDate> expirationDate) {
        try {
            ListaPresente list = ListaPresente.create(
                    name,
                    description,
                    expirationDate,
                    Memoria.getUserId());
            return ops.create(list);
        } catch (final Exception e) { return -1; }
    }

    public boolean update(final ListaPresente list) {
        try { return ops.update(list); } 
        catch (Exception e) { return false; }
    }

    public boolean delete(final int listId) {
        try { return ops.delete(listId); } 
        catch (Exception e) { return false; }
    }

    public boolean changeStatus(final int id, final boolean active) {
        try {
            ListaPresente lista = this.getById(id);
            lista.changeStatus(active);
            return ops.update(lista);
        } catch (Exception e) { return false; }
    }

    public boolean reativar(final int id) {
        return this.changeStatus(id, true);
    }

    public boolean inativar(final int id) {
        return this.changeStatus(id, false);
    }

    public boolean toggleStatus(final boolean active) {
        try {
            List<ListaPresente> lista = this.getAllByIdUsuario(Memoria.getUserId());
            return lista.stream().allMatch(v -> {
                v.changeStatus(active);
                try { return ops.update(v); } 
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (final Exception e) { return false; }
    }
}
