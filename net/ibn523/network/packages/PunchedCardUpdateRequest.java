package net.ibn523.network.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;

import net.ibn523.network.PacketIDs;
import net.minecraft.src.Packet250CustomPayload;

public class PunchedCardUpdateRequest extends GeneralPacket {	
	public int id;
	
	public PunchedCardUpdateRequest() {
		super("PunchedCard",PacketIDs.PUNCHEDCARD_REQ);
	}
	public PunchedCardUpdateRequest(int id) {
		this();
		this.id = id;
	}

	@Override
	public void writeToData(DataOutputStream data) throws IOException  {
		data.writeInt(id);
	}

	@Override
	public void readFromData(DataInputStream data) throws IOException {
		id = data.readInt();
	}
	
	@Override
	public String toString() {
		return "PunchedCardUpdateRequest:["+id+"]";
	}
}
