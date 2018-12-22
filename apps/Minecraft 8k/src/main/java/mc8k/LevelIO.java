package mc8k;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class LevelIO {
    private static final int MAGIC_NUMBER = 262451152;
    private static final int CURRENT_VERSION = 1;
    public String error = null;

    public LevelIO() {
    }

    public boolean load(Level level, InputStream inputStream) throws IOException {
        int n;
        DataInputStream dataInputStream;
        try {
            dataInputStream = new DataInputStream(new GZIPInputStream(inputStream));
            n = dataInputStream.readInt();
            if (n != MAGIC_NUMBER) {
                this.error = "Bad level file format";
                return false;
            }
        }
        catch (Exception var4_5) {
            var4_5.printStackTrace();
            this.error = "Failed to load level: " + var4_5.toString();
            return false;
        }
        n = dataInputStream.readInt();
        if (n > CURRENT_VERSION) {
            this.error = "Bad level file format";
            return false;
        }
        String string = dataInputStream.readUTF();
        String string2 = dataInputStream.readUTF();
        long l = dataInputStream.readLong();
        short s = dataInputStream.readShort();
        short s2 = dataInputStream.readShort();
        short s3 = dataInputStream.readShort();
        short s4 = dataInputStream.readShort();
        short s5 = dataInputStream.readShort();
        short s6 = dataInputStream.readShort();
        byte[] _blocks = new byte[s * s2 * s3];
        dataInputStream.readFully(_blocks);
        dataInputStream.close();
        int[] blocks = new int[s * s2 * s3];
        for (int i = 0; i < _blocks.length; i ++) {
            blocks[i] = (int) _blocks[i];
        }
        level.setData(s, s3, s2, blocks);
        level.name = string;
        level.creator = string2;
        level.createTime = l;
        level.spawnX = s4;
        level.spawnY = s5;
        level.spawnZ = s6;
        return true;
    }

    public void save(Level level, OutputStream outputStream) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new GZIPOutputStream(outputStream));
            dataOutputStream.writeInt(MAGIC_NUMBER);
            dataOutputStream.writeInt(CURRENT_VERSION);
            dataOutputStream.writeUTF(level.name);
            dataOutputStream.writeUTF(level.creator);
            dataOutputStream.writeLong(level.createTime);
            dataOutputStream.writeShort(level.width);
            dataOutputStream.writeShort(level.height);
            dataOutputStream.writeShort(level.depth);
            dataOutputStream.writeShort(level.spawnX);
            dataOutputStream.writeShort(level.spawnY);
            dataOutputStream.writeShort(level.spawnZ);
            byte[] blocks = new byte[level.width * level.height * level.depth];
            for (int i = 0; i < level.blocks.length; i ++) {
                blocks[i] = (byte) level.blocks[i];
            }
            dataOutputStream.write(blocks);
            dataOutputStream.close();
        }
        catch (Exception var3_4) {
            var3_4.printStackTrace();
        }
    }
}

