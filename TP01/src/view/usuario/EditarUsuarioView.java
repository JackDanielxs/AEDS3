package view.usuario;

import aeds3.Memoria;
import controller.UsuarioController;
import model.Usuario;
import util.CampoObrigatorio;
import view.View;

public class EditarUsuarioView extends View {
    public static final EditarUsuarioView INSTANCE = new EditarUsuarioView();

    private EditarUsuarioView() {
        super("Editar dados", true);
    }

    @Override
    public void viewDisplay() {
        Usuario u = UsuarioController.INSTANCE.getById(Memoria.getIdUsuario());
        if (u == null) {
            this.alertMessage("Usuário não encontrado.");
            return;
        }
        System.out.println("Deixar em branco -> será utilizado o dado anterior");
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        if (CampoObrigatorio.isNotValid(nome)) {
            nome = u.getNome();
        }

        System.out.print("Novo email: ");
        String email = sc.nextLine();
        if (CampoObrigatorio.isNotValid(email)) {
            email = u.getEmail();
        }

        System.out.print("Nova senha: ");
        String senha = sc.nextLine();
        if (CampoObrigatorio.isNotValid(senha)) {
            senha = u.getHashSenha();
        }

        System.out.print("Nova pergunta de segurança: ");
        String pergunta = sc.nextLine();
        if (CampoObrigatorio.isNotValid(pergunta)) {
            pergunta = u.getPergunta();
        }

        System.out.print("Nova resposta de segurança: ");
        String resposta = sc.nextLine();
        if (CampoObrigatorio.isNotValid(resposta)) {
            resposta = u.getResposta();
        }

        UsuarioController.INSTANCE.updateUser(
                u.getId(),
                nome,
                email,
                senha,
                pergunta,
                resposta
        );

        this.alertMessage("Dados atualizados.");
    }
}
