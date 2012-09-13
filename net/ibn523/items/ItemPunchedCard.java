package net.ibn523.items;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.ibn523.CommonProxy;
import net.ibn523.IBN523;
import net.ibn523.items.data.DataPunchedCard;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemMapBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet131MapData;
import net.minecraft.src.World;
import net.minecraft.src.WorldSavedData;

public class ItemPunchedCard extends Item {

	public ItemPunchedCard(int par1) {
		super(par1);
		setMaxStackSize(1);
		setTabToDisplayOn(CreativeTabs.tabRedstone);
		setIconIndex(0);
		setItemName("punchcard");
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.ITEMS_PNG;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.openGui(IBN523.instance, 0, world, itemstack.getItemDamage(), 0, 0);
		return itemstack;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.setItemDamage(world.getUniqueDataId(this.getItemName()));
		
		String index = "PunchCard_"+itemstack.getItemDamage();
		WorldSavedData data = DataPunchedCard.getData(itemstack.getItemDamage(), world);
		world.setItemData(index, data);
		data.markDirty();
	}
}
