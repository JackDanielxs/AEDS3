package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class HashExtensivel<T extends RegistroHashExtensivel>  {
    String directoryNameFile;
    String nomeArquivoCestos;
    RandomAccessFile directoryFile;
    RandomAccessFile bucketFile;
    int quantityPerBucket;
    Directory directory;
    Constructor<T> constructor;

    protected class Cesto {
        Constructor<T> constructor;
        short quantityMaxima; 
        short bytesPorElemento; 
        short bytesPorCesto; 

        byte localDepth;
        short quantity;
        ArrayList<T> elements;

        public Cesto(Constructor<T> ct, int qtdmax) throws Exception {
            this(ct, qtdmax, 0);
        }

        public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
            constructor = ct;
            if (qtdmax > 32767)
                throw new Exception("quantidade máxima de 32.767 elementos");
            if (pl > 127)
                throw new Exception("Profundidade local máxima de 127 bits");

            localDepth = (byte) pl;
            quantity = 0;
            quantityMaxima = (short) qtdmax;
            elements = new ArrayList<>(quantityMaxima);
            bytesPorElemento = ct.newInstance().size();
            bytesPorCesto = (short) (bytesPorElemento * quantityMaxima + 3);
        }

        public byte[] toByteArray() throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(localDepth);
            dos.writeShort(quantity);
            int i = 0;
            while (i < quantity) {
                dos.write(elements.get(i).toByteArray());
                i++;
            }
            byte[] vazio = new byte[bytesPorElemento];
            while (i < quantityMaxima) {
                dos.write(vazio);
                i++;
            }
            return baos.toByteArray();
        }

        public void fromByteArray(byte[] ba) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            localDepth = dis.readByte();
            quantity = dis.readShort();
            int i = 0;
            elements = new ArrayList<>(quantityMaxima);
            byte[] dados = new byte[bytesPorElemento];
            T elem;
            while (i < quantityMaxima) {
                dis.read(dados);
                elem = constructor.newInstance();
                elem.fromByteArray(dados);
                elements.add(elem);
                i++;
            }
        }

        public boolean create(T elem) {
            if (full())
                return false;
            int i = quantity - 1;
            while (i >= 0 && elem.hashCode() < elements.get(i).hashCode())
                i--;
            elements.add(i + 1, elem);
            quantity++;
            return true;
        }

        public T read(int chave) {
            if (empty())
                return null;
            int i = 0;
            while (i < quantity && chave > elements.get(i).hashCode())
                i++;
            if (i < quantity && chave == elements.get(i).hashCode())
                return elements.get(i);
            else
                return null;
        }
        
        public boolean update(T elem) {
            if (empty())
                return false;
            int i = 0;
            while (i < quantity && elem.hashCode() > elements.get(i).hashCode())
                i++;
            if (i < quantity && elem.hashCode() == elements.get(i).hashCode()) {
                elements.set(i, elem);
                return true;
            } else
                return false;
        }

        public boolean delete(int chave) {
            if (empty())
                return false;
            int i = 0;
            while (i < quantity && chave > elements.get(i).hashCode())
                i++;
            if (chave == elements.get(i).hashCode()) {
                elements.remove(i);
                quantity--;
                return true;
            } else
                return false;
        }

        public boolean empty() {
            return quantity == 0;
        }

        public boolean full() {
        return quantity == quantityMaxima;
        }

        public String toString() {
            String s = "Profundidade Local: " + localDepth + "\nquantity: " + quantity + "\n| ";
            int i = 0;
            while (i < quantity) {
                s += elements.get(i).toString() + " | ";
                i++;
            }
            while (i < quantityMaxima) {
                s += "- | ";
                i++;
            }
            return s;
        }

        public int size() {
        return bytesPorCesto;
        }

    }

    protected class Directory {
        byte profundidadeGlobal;
        long[] enderecos;

        public Directory() {
            profundidadeGlobal = 0;
            enderecos = new long[1];
            enderecos[0] = 0;
        }

        public boolean updateAddress(int p, long e) {
            if (p > Math.pow(2, profundidadeGlobal))
                return false;
            enderecos[p] = e;
            return true;
        }

        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantity = (int) Math.pow(2, profundidadeGlobal);
            int i = 0;
            while (i < quantity) {
                dos.writeLong(enderecos[i]);
                i++;
            }
            return baos.toByteArray();
        }

        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantity = (int) Math.pow(2, profundidadeGlobal);
            enderecos = new long[quantity];
            int i = 0;
            while (i < quantity) {
                enderecos[i] = dis.readLong();
                i++;
            }
        }

        public String toString() {
            String s = "\nProfundidade global: " + profundidadeGlobal;
            int i = 0;
            int quantity = (int) Math.pow(2, profundidadeGlobal);
            while (i < quantity) {
                s += "\n" + i + ": " + enderecos[i];
                i++;
            }
            return s;
        }

        protected long address(int p) {
            if (p > Math.pow(2, profundidadeGlobal))
                return -1;
            return enderecos[p];
        }

        protected boolean duplica() {
            if (profundidadeGlobal == 127)
                return false;
            profundidadeGlobal++;
            int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
            int q2 = (int) Math.pow(2, profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            int i = 0;
            while (i < q1) {
                novosEnderecos[i] = enderecos[i];
                i++;
            }
            while (i < q2) {
                novosEnderecos[i] = enderecos[i - q1];
                i++;
            }
            enderecos = novosEnderecos;
            return true;
        }
        
        protected int hash(int key) {
        return Math.abs(key) % (int) Math.pow(2, profundidadeGlobal);
        }

        protected int hash2(int key, int pl) {
        return Math.abs(key) % (int) Math.pow(2, pl);
        }

    }

    public HashExtensivel(
        Constructor<T> constructor, 
        int quantityPerBucket, 
        String directoryNameFile, 
        String nomeArquivoCestos
    ) throws Exception {
        this.constructor = constructor;
        this.quantityPerBucket = quantityPerBucket;
        this.directoryNameFile = "data/"+directoryNameFile+".d.db";
        this.nomeArquivoCestos = "data/"+nomeArquivoCestos+".c.db";

        File file = new File(this.directoryNameFile);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        directoryFile = new RandomAccessFile(this.directoryNameFile, "rw");
        
        file = new File(this.nomeArquivoCestos);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        bucketFile = new RandomAccessFile(this.nomeArquivoCestos, "rw");
        
        if (directoryFile.length() == 0 || bucketFile.length() == 0) {
            directory = new Directory();
            byte[] bd = directory.toByteArray();
            directoryFile.write(bd);

            Cesto c = new Cesto(constructor, quantityPerBucket);
            bd = c.toByteArray();
            bucketFile.seek(0);
            bucketFile.write(bd);
        }
    }

    public boolean create(T elem) throws Exception {
        byte[] bd = new byte[(int) directoryFile.length()];
        directoryFile.seek(0);
        directoryFile.read(bd);
        directory = new Directory();
        directory.fromByteArray(bd);

        int i = directory.hash(elem.hashCode());
        
        long enderecoCesto = directory.address(i);
        Cesto c = new Cesto(constructor, quantityPerBucket);
        byte[] ba = new byte[c.size()];
        bucketFile.seek(enderecoCesto);
        bucketFile.read(ba);
        c.fromByteArray(ba);
        
        if (c.read(elem.hashCode()) != null)
            throw new Exception("Elemento já existe");

        if (!c.full()) {
            c.create(elem);
            bucketFile.seek(enderecoCesto);
            bucketFile.write(c.toByteArray());
            return true;
        }
        
        byte pl = c.localDepth;
        if (pl >= directory.profundidadeGlobal)
        directory.duplica();
        byte pg = directory.profundidadeGlobal;

        Cesto c1 = new Cesto(constructor, quantityPerBucket, pl + 1);
        bucketFile.seek(enderecoCesto);
        bucketFile.write(c1.toByteArray());

        Cesto c2 = new Cesto(constructor, quantityPerBucket, pl + 1);
        long novoEndereco = bucketFile.length();
        bucketFile.seek(novoEndereco);
        bucketFile.write(c2.toByteArray());

        int inicio = directory.hash2(elem.hashCode(), c.localDepth);
        int deslocamento = (int) Math.pow(2, pl);
        int max = (int) Math.pow(2, pg);
        boolean troca = false;
        for (int j = inicio; j < max; j += deslocamento) {
            if (troca)
                directory.updateAddress(j, novoEndereco);
            troca = !troca;
        }
        
        bd = directory.toByteArray();
        directoryFile.seek(0);
        directoryFile.write(bd);

        for (int j = 0; j < c.quantity; j++) {
            create(c.elements.get(j));
        }
        create(elem); 
        return true;

    }

    public T read(final int key) throws Exception {
        byte[] bd = new byte[(int) directoryFile.length()];
        directoryFile.seek(0);
        directoryFile.read(bd);
        directory = new Directory();
        directory.fromByteArray(bd);

        int i = directory.hash(key);

        long enderecoCesto = directory.address(i);
        Cesto c = new Cesto(constructor, quantityPerBucket);
        byte[] ba = new byte[c.size()];
        bucketFile.seek(enderecoCesto);
        bucketFile.read(ba);
        c.fromByteArray(ba);

        return c.read(key);
    }

    public boolean update(T elem) throws Exception {
        byte[] bd = new byte[(int) directoryFile.length()];
        directoryFile.seek(0);
        directoryFile.read(bd);
        directory = new Directory();
        directory.fromByteArray(bd);

        int i = directory.hash(elem.hashCode());

        long enderecoCesto = directory.address(i);
        Cesto c = new Cesto(constructor, quantityPerBucket);
        byte[] ba = new byte[c.size()];
        bucketFile.seek(enderecoCesto);
        bucketFile.read(ba);
        c.fromByteArray(ba);
        
        if (!c.update(elem))
            return false;

        bucketFile.seek(enderecoCesto);
        bucketFile.write(c.toByteArray());
        return true;

    }

    public boolean delete(int key) throws Exception {
        byte[] bd = new byte[(int) directoryFile.length()];
        directoryFile.seek(0);
        directoryFile.read(bd);
        directory = new Directory();
        directory.fromByteArray(bd);

        int i = directory.hash(key);

        long enderecoCesto = directory.address(i);
        Cesto c = new Cesto(constructor, quantityPerBucket);
        byte[] ba = new byte[c.size()];
        bucketFile.seek(enderecoCesto);
        bucketFile.read(ba);
        c.fromByteArray(ba);
        
        if (!c.delete(key))
            return false;

        bucketFile.seek(enderecoCesto);
        bucketFile.write(c.toByteArray());
        return true;
    }

    public void print() {
        try {
            byte[] bd = new byte[(int) directoryFile.length()];
            directoryFile.seek(0);
            directoryFile.read(bd);
            directory = new Directory();
            directory.fromByteArray(bd);
            System.out.println("\nDIRETÓRIO ------------------");
            System.out.println(directory);

            System.out.println("\nCESTOS ---------------------");
            bucketFile.seek(0);
            while (bucketFile.getFilePointer() != bucketFile.length()) {
                System.out.println("Endereço: " + bucketFile.getFilePointer());
                Cesto c = new Cesto(constructor, quantityPerBucket);
                byte[] ba = new byte[c.size()];
                bucketFile.read(ba);
                c.fromByteArray(ba);
                System.out.println(c + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        directoryFile.close();
        bucketFile.close();
    }
}
