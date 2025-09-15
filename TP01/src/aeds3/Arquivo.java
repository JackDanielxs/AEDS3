package aeds3;
import java.io.File;
import java.io.RandomAccessFile;

import model.Registro;

public class Arquivo<T extends Registro> {
    private final int HEAD_LENGTH = 12;
    private RandomAccessFile file;
    private Class<T> pClass;
    HashExtensivel<ParIdEndereco> directIndex;
    
    public Arquivo(final Class<T> privClass) throws Exception {
        this.pClass = privClass;

        String className = this.pClass.getSimpleName().toLowerCase();
        String geralDirName = "data";
        File dir = new File(geralDirName);
        if(!dir.exists())
            dir.mkdir();

        String dirName = geralDirName+"/"+className;
        dir = new File(dirName);
        if(!dir.exists())
            dir.mkdir();

        String fileName = dirName + "/" + className + ".db";
        file = new RandomAccessFile(fileName, "rw");
        if(file.length() < HEAD_LENGTH) {
            file.writeInt(0);
            file.writeLong(-1);
        }

        directIndex = new HashExtensivel<>(
            ParIdEndereco.class.getConstructor(),
            4,
            className + "/" + className,
            className + "/" + className
        );
    }

    public int create(T obj) throws Exception {
        file.seek(0);
        int nextId = file.readInt()+1;
        file.seek(0);
        file.writeInt(nextId);
        obj.setId(nextId);
        byte[] b = obj.toByteArray();

        long address = getDeleted(b.length);
        if(address == -1){
            file.seek(file.length());
            address = file.getFilePointer();
            file.writeByte(' ');
            file.writeShort(b.length);
            file.write(b); 
        } else {
            file.seek(address);
            file.writeByte(' ');
            file.skipBytes(2);
            file.write(b); 
        }
        
        directIndex.create(ParIdEndereco.create(nextId, address));

        return obj.getId();
    }
    
    public T read(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        ParIdEndereco pid = directIndex.read(id);
        if(pid!=null) {
            file.seek(pid.getEndereco());
            obj = pClass.getConstructor().newInstance();
            lapide = file.readByte();
            if(lapide==' ') {
                tam = file.readShort();
                b = new byte[tam];
                file.read(b);
                obj.fromByteArray(b);
                if(obj.getId()==id)
                    return obj;
            }
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        ParIdEndereco pie = directIndex.read(id);
        if(pie!=null) {
            file.seek(pie.getEndereco());
            obj = pClass.getConstructor().newInstance();
            lapide = file.readByte();
            if(lapide==' ') {
                tam = file.readShort();
                b = new byte[tam];
                file.read(b);
                obj.fromByteArray(b);

                if(obj.getId() == id && directIndex.delete(id)) {
                    file.seek(pie.getEndereco());
                    file.write('*');
                    addDeleted(tam, pie.getEndereco());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean update(T novoObj) throws Exception {
        T obj;
        short tam;
        byte[] b;
        byte lapide;

        ParIdEndereco pie = directIndex.read(novoObj.getId());
        if(pie!=null) {
            file.seek(pie.getEndereco());
            obj = pClass.getConstructor().newInstance();
            lapide = file.readByte();
            if(lapide == ' ') {
                tam = file.readShort();
                b = new byte[tam];
                file.read(b);
                obj.fromByteArray(b);
                if(obj.getId() == novoObj.getId()) {
                    byte[] b2 = novoObj.toByteArray();
                    short tam2 = (short)b2.length;
                    if(tam2 <= tam) {
                        file.seek(pie.getEndereco() + 3);
                        file.write(b2);
                    }
                    else {
                        file.seek(pie.getEndereco());
                        file.write('*');
                        addDeleted(tam, pie.getEndereco());                        
                        long novoaddress = getDeleted(b.length);   
                        if(novoaddress == -1) {   
                            file.seek(file.length());
                            novoaddress = file.getFilePointer();
                            file.writeByte(' ');       
                            file.writeShort(tam2);       
                            file.write(b2);              
                        } else {
                            file.seek(novoaddress);
                            file.writeByte(' ');       
                            file.skipBytes(2);         
                            file.write(b2);              
                        }

                        directIndex.update(ParIdEndereco.create(novoObj.getId(), novoaddress));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void addDeleted(int necessaryLength, long newAddress) throws Exception {
        long previous = 4; 
        file.seek(previous);
        long address = file.readLong(); 
        long next = -1;
        int length;
        if(address == -1) {  
            file.seek(4);
            file.writeLong(newAddress);
            file.seek(newAddress + 3);
            file.writeLong(-1);
        } else {
            do {
                file.seek(address + 1);
                length = file.readShort();
                next = file.readLong();
                if(length > necessaryLength) {  
                    if(previous == 4) 
                        file.seek(previous);
                    else
                        file.seek(previous + 3);
                    file.writeLong(newAddress);
                    file.seek(newAddress + 3);
                    file.writeLong(address);
                    break;
                }
                if(next == -1) {  
                    file.seek(address + 3);
                    file.writeLong(newAddress);
                    file.seek(newAddress + 3);
                    file.writeLong(-1);
                    break;
                }
                previous = address;
                address = next;
            } while (address != -1);
        }
    }
    
    public long getDeleted(int necessaryLength) throws Exception {
        long previous = 4; 
        file.seek(previous);
        long address = file.readLong(); 
        long next = -1; 
        int length;
        while(address != -1) {
            file.seek(address + 1);
            length = file.readShort();
            next = file.readLong();
            if(length > necessaryLength) {  
                if(previous == 4)  
                    file.seek(previous);
                else
                    file.seek(previous + 3);
                file.writeLong(next);
                break;
            }
            previous = address;
            address = next;
        }
        return address;
    }

    public void close() throws Exception { file.close(); }
}