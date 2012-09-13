package net.ibn523.network.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;

import net.ibn523.network.PacketIDs;
import net.minecraft.src.Packet250CustomPayload;

public class PunchedCard extends GeneralPacket {	
	public int id;
	public BitSet[] holes;
	
	public PunchedCard() {
		super("PunchedCard",PacketIDs.PUNCHEDCARD);
	}
	public PunchedCard(int id, BitSet[] holes) {
		this();
		this.id = id;
		this.holes = holes;
	}

	@Override
	public void writeToData(DataOutputStream data) throws IOException  {
		data.writeInt(id);
		data.writeInt(holes.length);
		for ( int i = 0; i < holes.length; i++ ) {
			data.writeInt(getLineValue(i));
		}
	}

	@Override
	public void readFromData(DataInputStream data) throws IOException {
		id = data.readInt();
		int holeSize = data.readInt();
		holes = new BitSet[holeSize];
		for ( int i = 0; i < holeSize; i++ ) {
			setLineValue(i,data.readInt());
		}
	}
	
	private int getLineValue(int line) {
		int lineInt = 0;
		for(int i = 0 ; i < 32; i++)
			if(holes[line].get(i))
				lineInt |= (1 << i);
		return lineInt;
	}
	
	private void setLineValue(int line, int lineInt) {
		holes[line] = new BitSet();
		int index = 0;
		while (lineInt != 0L) {
			if (lineInt % 2L != 0) {
				holes[line].set(index);
			}
			++index;
			lineInt = lineInt >>> 1;
		}
	}
	
	@Override
	public String toString() {
		return "PunchedCard:["+id+","+holes+"]";
	}
}
