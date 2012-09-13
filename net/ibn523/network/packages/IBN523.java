package net.ibn523.network.packages;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.PacketIDs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;

public class IBN523 extends GeneralPacket {	
	public int instructionPointer;
	public String[] code;
	public List<DataPunchedCard> cards;

	public IBN523() {
		super("IBN523",PacketIDs.IBN523);
	}
	
	public IBN523(TileEntityIBN523 tile) {
		this();
		this.instructionPointer = tile.getProcessor().getRegistryValue("IP");
		this.code = tile.getProcessor().getCode();
		this.cards = tile.getAssembler().getPunchCards();
	}

	@Override
	public void writeToData(DataOutputStream data) throws IOException  {
		data.writeInt(instructionPointer);
		if ( code == null ) {
			data.writeInt(0);
		} else {
			data.writeInt(code.length);
			for ( String codeLine: code) {
				GeneralPacket.writeString(codeLine, data);
			}
		}
		
		if ( cards == null ) {
			data.writeInt(0);
		} else {
			data.writeInt(cards.size());
			for ( DataPunchedCard card: cards) {
				data.writeInt(card.getId());
				if ( card.getHoles() == null ) {
					data.writeInt(0);
				} else {
					data.writeInt(card.getHoles().length);
					for ( int i = 0; i < card.getHoles().length; i++ ) {
						data.writeInt(getLineValue(card.getHoles()[i]));
					}
				}
			}
		}
	}

	@Override
	public void readFromData(DataInputStream data) throws IOException {
		instructionPointer = data.readInt();
		int codeLength = data.readInt();
		if ( codeLength > 0 ) {
			code = new String[codeLength];
			for ( int i = 0; i < codeLength; i++ ) {
				code[i] = GeneralPacket.readString(data, 1000);
			}
		}
		cards = new ArrayList<DataPunchedCard>();
		int numCards = data.readInt();
		for ( int i = 0; i < numCards; i++ ) {
			int cardId = data.readInt();
			int holeSize = data.readInt();
			if ( holeSize > 0 ) {
				BitSet[] holes = new BitSet[holeSize];
				for ( int j = 0; j < holeSize; j++ ) {
					holes[j] = setLineValue(data.readInt());
				}
				cards.add(new DataPunchedCard("PunchCard_"+cardId,cardId,holes));
			}
		}
	}
	
	@Override
	public String toString() {
		String out = "IBN523:["+instructionPointer+",";
		
		if ( code != null )
			out += code.length+",";
		else
			out += code+",";
		
		if ( cards != null )
			out += cards.size()+"]";
		else
			out += cards+"]";

		return out;
	}
	

	private int getLineValue(BitSet hole) {
		int lineInt = 0;
		for(int i = 0 ; i < 32; i++)
			if(hole.get(i))
				lineInt |= (1 << i);
		return lineInt;
	}
	
	private BitSet setLineValue(int lineInt) {
		BitSet out = new BitSet();
		int index = 0;
		while (lineInt != 0L) {
			if (lineInt % 2L != 0) {
				out.set(index);
			}
			++index;
			lineInt = lineInt >>> 1;
		}
		return out;
	}
}
