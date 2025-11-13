package view.listapresente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import controller.ListaPresenteController;
import util.CampoObrigatorio;
import view.View;

public final class CadastroListaPresenteView extends View {
    public static final CadastroListaPresenteView INSTANCE = new CadastroListaPresenteView();

    private CadastroListaPresenteView() {
        super("Nova Lista", false);
    }

    @Override
    protected void viewDisplay() {
        System.out.println("--- Nova Lista ---");

        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();

        System.out.print("Descrição: ");
        String description = scanner.nextLine().trim();

        Optional<LocalDate> expirationDate = Optional.empty();
        System.out.print("Definir data de expiração? (S/N): ");
        String defineExpiration = scanner.nextLine().trim().toUpperCase();

        if (defineExpiration.equals("S")) {
            System.out.print("Data de expiração (dd/MM/yyyy): ");
            String dateInput = scanner.nextLine().trim();
            try {
                LocalDate expDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                expirationDate = Optional.of(expDate);
            } catch (final Exception e) { System.out.println("Data inválida."); }
        }

        if (CampoObrigatorio.isBlank(name) || CampoObrigatorio.isBlank(description)) {
            this.alertMessage("Nome e descrição não podem ficar vazios.");
            return;
        }

        int resultId = ListaPresenteController.INSTANCE.create(name, description, expirationDate);

        if (resultId == -1) System.out.println("Erro ao criar a lista.");
        else System.out.println("Lista criada.");
    }
}
