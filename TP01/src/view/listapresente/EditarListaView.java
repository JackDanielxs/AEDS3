package view.listapresente;

import controller.ListaPresenteController;
import model.ListaPresente;
import util.CampoObrigatorio;
import view.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EditarListaView extends View {
    public static final EditarListaView INSTANCE = new EditarListaView();
    private int giftListId = -1;
    private EditarListaView() {
        super("Editar Lista", true);
    }
    public EditarListaView setGiftListId(final int giftListId) {
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
        String name = sc.nextLine();
        if (CampoObrigatorio.isNotValid(name)) {
            name = giftList.getNome();
        }

        System.out.print("Nova descrição: ");
        String description = sc.nextLine();
        if (CampoObrigatorio.isNotValid(description)) {
            description = giftList.getDescricao();
        }

        System.out.print("Nova data de expiração (DD/MM/YYYY): ");
        String expirationInput = sc.nextLine();
        Optional<LocalDate> expirationDate = giftList.getDtExpiracao();

        if (CampoObrigatorio.isValid(expirationInput)) {
            try {
                expirationDate = Optional.of(LocalDate.parse(expirationInput, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } catch (final Exception e) {
                System.out.println("Data inválida.");
            }
        }

        String phrase = giftList.getStatus() ? "desativar" : "reativar";
        System.out.printf("Deseja %s: (S ou N)", phrase);
        String confirmation = sc.nextLine();
        boolean newStatus = giftList.getStatus();
        if (confirmation.toUpperCase().equals("S")) {
            newStatus = !newStatus;
        }

        ListaPresenteController.INSTANCE.update(
            ListaPresente.from(
                name,
                description,
                giftList.getDtCriacao(),
                expirationDate,
                giftList.getCodigo(),
                giftList.getIdUsuario(),
                giftList.getId(),
                newStatus
            )
        );

        this.alertMessage("Lista atualizada.");
    }
}
