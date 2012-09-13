package net.ibn523.gui;

import net.ibn523.blocks.data.ContainerIBN523;
import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.items.ItemPunchedCard;
import net.ibn523.items.data.DataPunchedCard;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.WorldSavedData;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.Player;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch ( ID ) {
			case 0: // Punched Card
				break;
			case 1: // IBN523
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if( tileEntity instanceof TileEntityIBN523 ) {
					return new ContainerIBN523(player.inventory, (TileEntityIBN523) tileEntity);
				}
				break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch ( ID ) {
			case 0: // Punched Card
				DataPunchedCard data = DataPunchedCard.getData(x,world);
				return new GuiPunchedCard(data,x);
			case 1: // IBN523
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				if( tileEntity instanceof TileEntityIBN523 ) {
					return new GuiIBN523(player.inventory, (TileEntityIBN523) tileEntity);
				}
				break;
		}
		return null;
	}

}
