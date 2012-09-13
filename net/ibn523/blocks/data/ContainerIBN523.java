package net.ibn523.blocks.data;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerIBN523 extends Container {
	private TileEntityIBN523 tileentity;

	public ContainerIBN523 (InventoryPlayer playerInventory, TileEntityIBN523 te){
		tileentity = te;
		tileentity.openChest();

		for (int i = 0; i < tileentity.getSizeInventory() / 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(
						tileentity, 
						j + i * 9, 
						8 + j * 18, 
						22 + i * 18
				));
			}
		}
		
		bindPlayerInventory(playerInventory);
		
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileentity.isUseableByPlayer(player);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(
						inventoryPlayer,
						j + i * 9 + 9,
						8 + j * 18, 
						84 +56 + i * 18)
				);
			}
		}
			
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(
					inventoryPlayer, 
					i, 
					8 + i * 18, 
					142+56
			));
		}
	}

	@Override
	public ItemStack transferStackInSlot(int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			//merges the item into player inventory since its in the tileEntity
			if (slot == 0) {
				if (!mergeItemStack(stackInSlot, 1,
					inventorySlots.size(), true)) {
					return null;
				}
				//places it into the tileEntity is possible since its in the player inventory
			} else if (!mergeItemStack(stackInSlot, 0, 1, false)) {
				return null;
			}
			
			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}
		}
		
		return stack;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		tileentity.closeChest();
	}
}
