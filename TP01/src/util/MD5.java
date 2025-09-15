package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {
    public static String toMd5(final String password){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hashBytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Erro ao gerar hash MD5.",e);
        }
    }
}
