package aeds3;

public class Memoria {
    private static int idUsuario = -1;
    public static void setIdUsuario(int idUsuario) { Memoria.idUsuario = idUsuario; }
    public static int getIdUsuario() { return idUsuario; }
    public static void logout() { setIdUsuario(-1); }
    public static boolean isLogout() { return idUsuario == -1; }
}