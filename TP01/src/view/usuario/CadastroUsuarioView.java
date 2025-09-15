package view.usuario;

import controller.UsuarioController;
import util.CampoObrigatorio;
import view.View;

public class CadastroUsuarioView extends View {
    public static final CadastroUsuarioView INSTANCE = new CadastroUsuarioView();

    private CadastroUsuarioView() {
        super("Cadastrar", false);
    }

    @Override
    public void viewDisplay() {
        String nome;
        String email;
        String senha;
        String pergunta;
        String resposta;

        System.out.println("Insira seu nome:");
        nome = sc.nextLine();

        System.out.println("Insira seu e-mail:");
        email = sc.nextLine();

        System.out.println("Insira sua senha:");
        senha = sc.nextLine();

        System.out.println("Insira sua pergunta secreta:");
        pergunta = sc.nextLine();

        System.out.println("Insira a resposta da sua pergunta secreta:");
        resposta = sc.nextLine();

        if (
            CampoObrigatorio.isNotValid(nome) || 
            CampoObrigatorio.isNotValid(email) || 
            CampoObrigatorio.isNotValid(senha) || 
            CampoObrigatorio.isNotValid(pergunta) || 
            CampoObrigatorio.isNotValid(resposta)
        ) {
            this.alertMessage("Todos os campos são obrigatórios.");
            return;
        }

        int id = UsuarioController.INSTANCE.create(nome, email, senha, pergunta, resposta);

        if(id == -1){
            this.alertMessage("Não foi possivel cadastrar.");
        }else{
            this.alertMessage("Cadastrado. Acesse via login.");
        }
    }
}
