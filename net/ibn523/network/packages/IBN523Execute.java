package net.ibn523.network.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.network.PacketIDs;
import net.minecraft.src.Packet250CustomPayload;

public class IBN523Execute extends GeneralPacket {
	public int x;
	public int y;
	public int z;

	public IBN523Execute() {
		super("IBN523",PacketIDs.IBN523_EXEC);
	}
	public IBN523Execute(int x, int y, int z) {
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void writeToData(DataOutputStream data) throws IOException  {
		data.writeInt(x);
		data.writeInt(y);
		data.writeInt(z);
	}

	@Override
	public void readFromData(DataInputStream data) throws IOException {
		x = data.readInt();
		y = data.readInt();
		z = data.readInt();
	}
	
	@Override
	public String toString() {
		return "IBN523Execute:["+x+","+y+","+z+"]";
	}
}
