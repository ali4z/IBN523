package net.ibn523.network;

import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.packages.GeneralPacket;
import net.ibn523.network.packages.IBN523;
import net.ibn523.network.packages.IBN523Execute;
import net.ibn523.network.packages.IBN523UpdateRequest;
import net.ibn523.network.packages.PunchedCard;
import net.ibn523.network.packages.PunchedCardAddHole;
import net.ibn523.network.packages.PunchedCardUpdateRequest;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler extends CommonPacketHandler {

	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
		if ( player instanceof EntityPlayerMP ) {
			GeneralPacket p = null;
			switch (GeneralPacket.getIDFromPacket(packet) ) {
				case PacketIDs.IBN523_REQ:
					p = new IBN523UpdateRequest();
					p.fromPacket(packet);
					System.out.println("ServerPacketHandler: received: "+p);
					handleIBN523Request((EntityPlayerMP) player, (IBN523UpdateRequest) p);
					break;
				case PacketIDs.IBN523_EXEC:
					p = new IBN523Execute();
					p.fromPacket(packet);
					System.out.println("ServerPacketHandler: received: "+p);
					handleIBN523Execute((EntityPlayerMP) player, (IBN523Execute) p);
					break;
				case PacketIDs.PUNCHEDCARD_REQ:
					p = new PunchedCardUpdateRequest();
					p.fromPacket(packet);
					System.out.println("ServerPacketHandler: received: "+p);
					handlePunchedCardRequest((EntityPlayerMP) player,(PunchedCardUpdateRequest) p);
					break;
				case PacketIDs.PUNCHEDCARD_ADDHOLE:
					p = new PunchedCardAddHole();
					p.fromPacket(packet);
					System.out.println("ServerPacketHandler: received: "+p);
					handlePunchedCardAddHole((EntityPlayerMP) player,(PunchedCardAddHole) p);
					break;
				default:
					System.out.println("ServerPacketHandler: received: UNHANDLED! ("+GeneralPacket.getIDFromPacket(packet)+")");
					break;
			}
		}
	}
	
	public void handlePunchedCardAddHole(EntityPlayerMP player, PunchedCardAddHole packet) {
		// Fetch data.
		DataPunchedCard data = DataPunchedCard.getData(
				packet.id,
				player.worldObj
		);
		// Add hole.
		data.addHole(
				packet.line, 
				packet.bit
		);
	}
	
	public void handlePunchedCardRequest(EntityPlayerMP player, PunchedCardUpdateRequest packet) {
		// Fetch data
		DataPunchedCard data = DataPunchedCard.getData(packet.id,player.worldObj);
		// Assemble and send reply packet.
		send(
				player,
				new PunchedCard(
						packet.id,
						data.getHoles()
				)
		);
	}
	
	public void handleIBN523Request(EntityPlayerMP player, IBN523UpdateRequest packet) {
		// Fetch data
		TileEntity data = player.worldObj.getBlockTileEntity(
				packet.x, 
				packet.y, 
				packet.z
		);
		if ( data instanceof TileEntityIBN523 ) {
			// Assemble and send reply packet.
			send(
					player,
					new IBN523((TileEntityIBN523)data)
			);
		}
	}
	
	public void handleIBN523Execute(EntityPlayerMP player, IBN523Execute packet) {
		// Fetch data
		TileEntity data = player.worldObj.getBlockTileEntity(
				packet.x, 
				packet.y, 
				packet.z
		);
		if ( data instanceof TileEntityIBN523 ) {
			((TileEntityIBN523)data).getProcessor().start();
		}
	}
	
	public static void send(EntityPlayerMP player, GeneralPacket packet) {
		System.out.println("ServerPacketHandler: send: "+player.username+": "+packet);
		player.serverForThisPlayer.sendPacketToPlayer(packet.toPacket());
	}
	
	public static void broadcast(GeneralPacket packet) {
		System.out.println("ServerPacketHandler: broadcast: "+packet);
		World[] worlds = DimensionManager.getWorlds();
		for (int i = 0; i < worlds.length; i++) {
			for (int j = 0; j < worlds[i].playerEntities.size(); j++) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) worlds[i].playerEntities.get(j);
				send(entityplayermp,packet);
			}
		}
	}
}
