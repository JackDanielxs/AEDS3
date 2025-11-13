package aeds3;

public final class Memoria {
    private static int idUsuario = -1;
    public static void setUserId(int idUsuario) { Memoria.idUsuario = idUsuario; }
    public static int getUserId() { return idUsuario; }
    public static void logout() { setUserId(-1); }
    public static boolean isLogout(){ return idUsuario == -1; }
}
