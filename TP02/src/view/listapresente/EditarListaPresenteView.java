package view.listapresente;

import controller.ListaPresenteController;
import model.ListaPresente;
import util.CampoObrigatorio;
import view.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class EditarListaPresenteView extends View {
    public static final EditarListaPresenteView INSTANCE = new EditarListaPresenteView();
    private int giftListId = -1;

    private EditarListaPresenteView() {
        super("Editar Lista", true);
    }

    public EditarListaPresenteView set(final int giftListId) {
        this.giftListId = giftListId;
        return this;
    }

    @Override
    public void viewDisplay() {
        ListaPresente giftList = ListaPresenteController.INSTANCE.getById(giftListId);
        if (giftList == null) {
            System.out.println("Lista não encontrada!");
            return;
        }

        System.out.print("Novo nome: ");
        String name = scanner.nextLine();
        if (CampoObrigatorio.isBlank(name)) {
            name = giftList.getNome();
        }

        System.out.print("Nova descrição: ");
        String description = scanner.nextLine();
        if (CampoObrigatorio.isBlank(description)) {
            description = giftList.getDescricao();
        }

        System.out.print("Nova data de expiração (DD/MM/YYYY): ");
        String expirationInput = scanner.nextLine();
        Optional<LocalDate> expirationDate = giftList.getDtExpiracao();

        if (CampoObrigatorio.isNotBlank(expirationInput)) {
            try {
                expirationDate = Optional.of(LocalDate.parse(expirationInput, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } catch (final Exception e) {
                System.out.println("Data inválida. Mantendo a anterior.");
            }
        }

        String phrase = giftList.isActive() ? "desativar" : "reativar";
        System.out.printf("Deseja %s: (S/N)", phrase);
        String confirmation = scanner.nextLine();
        boolean newStatus = giftList.isActive();
        if (confirmation.toUpperCase().equals("S")) {
            newStatus = !newStatus;
        }

        ListaPresenteController.INSTANCE.update(
                ListaPresente.from(
                        name,
                        description,
                        giftList.getDtCriacao(),
                        expirationDate,
                        giftList.getCodCompartilha(),
                        giftList.getIdUsuario(),
                        giftList.getId(),
                        newStatus));

        this.alertMessage("Lista atualizada.");
    }
}
