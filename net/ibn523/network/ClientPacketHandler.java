package net.ibn523.network;

import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.gui.GuiIBN523;
import net.ibn523.gui.GuiPunchedCard;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.packages.GeneralPacket;
import net.ibn523.network.packages.IBN523;
import net.ibn523.network.packages.IBN523UpdateRequest;
import net.ibn523.network.packages.PunchedCard;
import net.ibn523.network.packages.PunchedCardAddHole;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler extends CommonPacketHandler {

	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
		if ( player instanceof EntityClientPlayerMP ) {
			GeneralPacket p = null;
			switch (GeneralPacket.getIDFromPacket(packet) ) {
				case PacketIDs.IBN523:
					p = new IBN523();
					p.fromPacket(packet);
					System.out.println("ClientPacketHandler: received: "+p);
					handleIBN523((IBN523) p);
					break;
				case PacketIDs.PUNCHEDCARD:
					p = new PunchedCard();
					p.fromPacket(packet);
					System.out.println("ClientPacketHandler: received: "+p);
					handlePunchedCard((PunchedCard) p);
					break;
				default:
					System.out.println("ClientPacketHandler: received: UNHANDLED! ("+GeneralPacket.getIDFromPacket(packet)+")");
					break;
			}
		}
	}
	
	public void handlePunchedCard(PunchedCard packet) {
		// Assemble Item ID and index.
		int id = packet.id;
		String index = "PunchCard_"+id;

		// Assemble data object
		DataPunchedCard data = new DataPunchedCard(index,id,packet.holes);
		
		// Update data on the screen, initGui will flush the holes.
		if ( Minecraft.getMinecraft().currentScreen instanceof GuiPunchedCard ) {
			((GuiPunchedCard)Minecraft.getMinecraft().currentScreen).setData(data);
			Minecraft.getMinecraft().currentScreen.initGui();
		}
	}
	
	public void handleIBN523(IBN523 packet) {
		if ( Minecraft.getMinecraft().currentScreen instanceof GuiIBN523 ) {
			((GuiIBN523)Minecraft.getMinecraft().currentScreen).setData(
					packet.instructionPointer,
					packet.code,
					packet.cards
			);
			Minecraft.getMinecraft().currentScreen.initGui();
		}
	}
	
	public static void sendPacket(GeneralPacket packet) {
		System.out.println("ClientPacketHandler: send: "+packet);
		FMLClientHandler.instance().sendPacket(packet.toPacket());
	}
}
