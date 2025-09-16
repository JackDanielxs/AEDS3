package controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import aeds3.Memoria;
import aeds3.listapresente.OperacoesLista;
import model.ListaPresente;

public class ListaPresenteController {
    public final static ListaPresenteController INSTANCE = new ListaPresenteController();
    private OperacoesLista ops;

    private ListaPresenteController() {
        try {
            this.ops = new OperacoesLista();
        } catch (final Exception e) { e.printStackTrace(); }
    }

    public ListaPresente getById(final int id) {
        try {
            return this.ops.read(id);
        } catch (final Exception e) { return null; }
    }

    public List<ListaPresente> getListasByIdUsuario(final int idUsuario) {
        try {
            return this.ops.getAllByIdUsuario(idUsuario);
        } catch (final Exception e) { return List.of(); }
    }

    public ListaPresente getByCodigo(final String cod) {
        try {
            return ops.getByCodigo(cod);
        } catch (final Exception e) { return null; }
    }

    public int create(
        final String nome, 
        final String descricao, 
        final Optional<LocalDate> dtExpiracao
    ) {
        try {
            ListaPresente lista = ListaPresente.create(
                nome,
                descricao,
                dtExpiracao,
                Memoria.getIdUsuario()
            );
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

    public boolean editarStatus(final int id, final boolean status) {
        try {
            ListaPresente lista = this.getById(id);
            lista.editarStatus(status);
            return ops.update(lista);
        } catch (Exception e) { return false; }
    }

    public boolean editarStatusPorUsuario(final boolean status) {
        try {
            List<ListaPresente> lista = this.getListasByIdUsuario(Memoria.getIdUsuario());
            return lista.stream().allMatch(v -> {
                v.editarStatus(status);
                try {
                    return ops.update(v);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        } catch (final Exception e) { return false; }
    }

    public boolean inativar(final int id){
        return this.editarStatus(id, false);
    }
}