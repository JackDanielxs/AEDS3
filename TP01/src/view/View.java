package view;

import java.util.List;
import java.util.Scanner;

import aeds3.Memoria;
import aeds3.MemoriaViews;

public abstract class View { 

    protected final Scanner sc = new Scanner(System.in);
    private final String titulo = "Presente Fácil\n--------------";
    protected String nomeTela = "";
    private boolean escrever = false;

    public View(final String nomeTela, final boolean escrever) {
        this.nomeTela = nomeTela;
        this.escrever = escrever;
    }

    protected abstract void viewDisplay();

    private void printPath() {
        List<View> snapshot = MemoriaViews.toList();
        String path = "";
        boolean first = true;
        for(int i = 0; i<snapshot.size(); i++) {
            View view = snapshot.get(i);
            if(view.escrever){
                if(!first) path += " ";
                path += "> " + view.nomeTela;
                first = false;
            }
        }
        if(snapshot.size() > 2) path += " ";
        if(this.escrever) path += "> " + this.nomeTela;
        if(path.length() > 0) System.out.println(path + "\n");
    }

    public void display() {
        clearScreen();
        System.out.println(titulo);
        printPath();
        viewDisplay();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {}
        System.out.flush();
    }

    protected void reload() {
        clearScreen();
        System.out.println(titulo);
        printPath();
    }

    protected void nextPage(View tela) {
        MemoriaViews.push(this);
        tela.display();
        this.back();
    }

    protected void alertMessage(final String msg, Object... args){
        try {
            System.out.printf(msg + "\n", args);
            Thread.sleep(1000);
        } catch (final Exception e) {
            e.printStackTrace();
        } 
    }

    protected void back(){
        View backView = MemoriaViews.pop();
        backView.display();
    }

    protected void logout(){
        MemoriaViews.reset();
        Memoria.logout();
        this.nextPage(InicioView.INSTANCE);
    }
    
    protected void exit() {
        System.out.println("Encerrando o programa...");
        System.exit(0);
    }
}
