package net.ibn523.blocks.data;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.ibn523.business.Assembler;
import net.ibn523.business.Processor;
import net.ibn523.items.ItemPunchedCard;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.ServerPacketHandler;
import net.ibn523.network.packages.IBN523;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.WorldSavedData;

public class TileEntityIBN523 extends TileEntity implements IInventory {
	private Assembler assembler;
	private Processor processor;
	private ItemStack[] cards;
	
	public TileEntityIBN523() {
		super();
		assembler = new Assembler();
		processor = new Processor(1,this);
		cards = new ItemStack[18];
	}
	
	public Assembler getAssembler() {
		return assembler;
	}
	public Processor getProcessor() {
		return processor;
	}
	public ItemStack[] getCards() {
		return cards;
	}

	public boolean isPoweringDirection(int l) {
		switch ( l ) {
			case 0: // D
				return processor.getRegistryValue("DPO") > 0;
			case 1: // U
				return processor.getRegistryValue("UPO") > 0;
			case 2: // S
				return processor.getRegistryValue("SPO") > 0;
			case 3: // N
				return processor.getRegistryValue("NPO") > 0;
			case 4: // E
				return processor.getRegistryValue("EPO") > 0;
			case 5: // W
				return processor.getRegistryValue("WPO") > 0;
		}
		return false;
	}
	public void setPoweringDirection(int l, boolean b) {
		int value = 0;
		if ( b ) value = 1;
		
		switch ( l ) {
			case 0: // D
				processor.setRegistryValue("DPI",value);
				break;
			case 1: // U
				processor.setRegistryValue("UPI",value);
				break;
			case 2: // S
				processor.setRegistryValue("SPI",value);
				break;
			case 3: // N
				processor.setRegistryValue("NPI",value);
				break;
			case 4: // E
				processor.setRegistryValue("EPI",value);
				break;
			case 5: // W
				processor.setRegistryValue("WPI",value);
				break;
		}
	}
	
	@Override
	public int getSizeInventory() {
		return cards.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		if ( i >= getSizeInventory() || i < 0 )
			return null;
		else
			return cards[i];
	}
	
	@Override
	public ItemStack decrStackSize(int index, int quantity) {
		ItemStack stack = getStackInSlot(index);
		if ( stack != null ) {
			if ( stack.stackSize <= quantity ) {
				removeInventorySlotContents(index);
			} else {
				stack = stack.splitStack(quantity);
				if ( stack.stackSize == 0 ) {
					removeInventorySlotContents(index);
				}
			}
		}
		
		return stack;
	}
	
	public void destroy() {
		processor.stop();
	}
	
	private void removeInventorySlotContents(int index) {
		if ( getStackInSlot(index).getItem() instanceof ItemPunchedCard ) {
			processor.stop();
			assembler.remPunchCard(index);
			String code = assembler.assemble();
			processor.setCode(
					code
			);
			IBN523 packet = new IBN523(this);
			ServerPacketHandler.broadcast(packet);
		}
		setInventorySlotContents(index, null);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = getStackInSlot(index);
		if (stack != null) {
			removeInventorySlotContents(index);
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack) {
		if ( index > assembler.getNumberOfCards() ) index = assembler.getNumberOfCards();
		
		if ( 
				itemstack != null && 
				itemstack.getItem() instanceof ItemPunchedCard &&
				MinecraftServer.getServer() != null &&
				MinecraftServer.getServer().worldServerForDimension(0) != null
		) {
			WorldSavedData data = DataPunchedCard.getData(
					itemstack.getItemDamage(),
					MinecraftServer.getServer().worldServerForDimension(0)
			);
			if ( data instanceof DataPunchedCard ) {
				processor.stop();
				assembler.addPunchCard(
						index, 
						(DataPunchedCard) data
				);
				String code = assembler.assemble();
				processor.setCode(
						code
				);
				IBN523 packet = new IBN523(this);
				ServerPacketHandler.broadcast(packet);
			}
		}
		
		this.cards[index] = itemstack;
		
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}


		if ( itemstack == null ) {
			for (int i = 0; i < cards.length; i++) {
				if ( i > 0 && cards[i-1] == null && cards[i] != null ) {
					cards[i-1] = cards[i];
					cards[i] = null;
				}
			}
		}
		
		this.onInventoryChanged();
	}
	
	@Override
	public String getInvName() {
		return "IBN523";
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return (
				worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
				entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D
		);
	}
	
	@Override
	public void openChest() {}
	
	@Override
	public void closeChest() {}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        
        NBTTagList tagList = tagCompound.getTagList("Inventory");
        for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                byte slot = tag.getByte("Slot");
                if (slot >= 0 && slot < cards.length) {
                	setInventorySlotContents(
                			slot,
                			ItemStack.loadItemStackFromNBT(tag)
                	);
                }
        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
                        
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < cards.length; i++) {
                ItemStack stack = cards[i];
                if (stack != null) {
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setByte("Slot", (byte) i);
                        stack.writeToNBT(tag);
                        itemList.appendTag(tag);
                }
        }
        tagCompound.setTag("Inventory", itemList);
	}
}
