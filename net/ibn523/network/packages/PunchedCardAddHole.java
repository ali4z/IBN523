package net.ibn523.network.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.ibn523.network.PacketIDs;
import net.minecraft.src.Packet250CustomPayload;

public class PunchedCardAddHole extends GeneralPacket {	
	public int id;
	public int line;
	public int bit;
		
	public PunchedCardAddHole() {
		super("PunchedCard",PacketIDs.PUNCHEDCARD_ADDHOLE);
	}
	public PunchedCardAddHole(int id, int line, int bit) {
		this();
		this.id = id;
		this.line = line;
		this.bit = bit;
	}

	@Override
	public void writeToData(DataOutputStream data) throws IOException  {
		data.writeInt(id);
		data.writeInt(line);
		data.writeInt(bit);
	}

	@Override
	public void readFromData(DataInputStream data) throws IOException {
		id = data.readInt();
		line = data.readInt();
		bit = data.readInt();
	}
	
	@Override
	public String toString() {
		return "PunchedCardAddHole:["+id+","+line+","+bit+"]";
	}
}
