package net.ibn523.network.packages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ibn523.network.PacketIDs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public abstract class GeneralPacket {
	public int packetId;
	public String domain;
	
	public GeneralPacket(String domain, int id) {
		this.domain = domain;
		this.packetId = id;
	}
	
	public Packet250CustomPayload toPacket() {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream data = new DataOutputStream(bytes);
			data.writeInt(packetId);
			writeToData(data);
			Packet250CustomPayload packet = new Packet250CustomPayload(domain,bytes.toByteArray());
			return packet;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void fromPacket(Packet250CustomPayload packet) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			packetId = data.readInt();
			domain = packet.channel;
			readFromData(data);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static int getIDFromPacket(Packet250CustomPayload packet) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			return data.readInt();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}
	
	public abstract void writeToData(DataOutputStream data) throws IOException ;
	public abstract void readFromData(DataInputStream data) throws IOException ;
	
	

	public static void writeString(String par0Str, DataOutputStream par1DataOutputStream) throws IOException {
		if (par0Str.length() > 32767) {
			throw new IOException("String too big");
		} else {
			par1DataOutputStream.writeShort(par0Str.length());
			par1DataOutputStream.writeChars(par0Str);
		}
	}
	
	public static String readString(DataInputStream par0DataInputStream, int par1) throws IOException {
		short var2 = par0DataInputStream.readShort();

		if (var2 > par1) {
			throw new IOException(
					"Received string length longer than maximum allowed ("
							+ var2 + " > " + par1 + ")");
		} else if (var2 < 0) {
			throw new IOException(
					"Received string length is less than zero! Weird string!");
		} else {
			StringBuilder var3 = new StringBuilder();

			for (int var4 = 0; var4 < var2; ++var4) {
				var3.append(par0DataInputStream.readChar());
			}

			return var3.toString();
		}
	}
}
