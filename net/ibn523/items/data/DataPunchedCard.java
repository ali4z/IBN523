package net.ibn523.items.data;

import java.util.BitSet;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;

import net.ibn523.network.ServerPacketHandler;
import net.ibn523.network.packages.PunchedCard;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.WorldSavedData;

public class DataPunchedCard extends WorldSavedData {
	private BitSet[] holes;
	private int numBits;
	private int numLines;
	private int id;

	public DataPunchedCard(String par1Str) {
		this(par1Str,Integer.parseInt(par1Str.split("_")[1]));
	}
	public DataPunchedCard(String par1Str, int id) {
		this(par1Str,id,new BitSet[15]);
		for ( int i = 0; i < holes.length; i++ )
			holes[i] = new BitSet(numBits);
		
	}
	public DataPunchedCard(String par1Str, int id, BitSet[] holes) {
		super(par1Str);
		numBits = 8;
		numLines = 15;
		this.id = id;
		this.holes = holes;
	}

	public BitSet[] getHoles() {
		return holes;
	}
	
	public int getNumLines() {
		return numLines;
	}
	
	public int getNumBits() {
		return numBits;
	}
	
	public int getId() {
		return id;
	}
	
	public void addHole(int line, int bit) {
		holes[line].set(bit, true);
		this.markDirty();
	}
	
	public boolean isHoled(int line, int bit) {
		return holes[line].get(bit);
	}
	
	public int getLineValue(int line) {
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
	
	public String toString() {
		StringBuilder out = new StringBuilder("   0  1  2  3  4  5  6  7  8  9  A  B  C  D  E\n");
		for ( int i = 0; i < numBits; i++ ) {
			out.append(Integer.toHexString(i)+"  ");
			for ( int j = 0; j < holes.length; j++ ) {
				if ( !isHoled(j,i) )
					out.append("#  ");
				else
					out.append("O  ");
			}
			out.append("\n");
		}
			
		return out.toString();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		int[] lines = tag.getIntArray("lines");
		if ( lines == null )
			markDirty();
		else
			for(int j = 0; j < lines.length; j++ )
				setLineValue(j,lines[j]);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		int[] lines = new int[holes.length];
		for(int j = 0; j < holes.length; j++ )
			lines[j] = getLineValue(j);
		tag.setIntArray("lines", lines);
	}

	public static DataPunchedCard getData(int id, World world) {
		String index = "PunchCard_"+id;
		WorldSavedData data = world.loadItemData(
				DataPunchedCard.class,
				index
		);
		
		if ( data == null ) {
			data = new DataPunchedCard(index,id);
			world.setItemData(index, data);
			data.markDirty();
		}
		
		if ( data instanceof DataPunchedCard ) 
			return (DataPunchedCard)data;
		else 
			return null;
	}
}

