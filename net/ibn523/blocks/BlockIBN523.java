package net.ibn523.blocks;

import java.util.Random;

import net.ibn523.CommonProxy;
import net.ibn523.IBN523;
import net.ibn523.blocks.data.TileEntityIBN523;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockIBN523 extends BlockContainer {

	public BlockIBN523(int i) {
		super(i, 0, Material.circuits);
		setCreativeTab(CreativeTabs.tabRedstone);
		setBlockName("IBN523");
		setHardness(2.0F);
		setResistance(5.0F);
	}
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int what, float the, float fook, float isthis) {
		if ( world.getBlockTileEntity(i,j,k) == null ) return false;
		
		entityplayer.openGui(IBN523.instance, 1, world, i, j, k);
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving placer) {
		int orient = BlockPistonBase.determineOrientation(world, i, j, k, (EntityPlayer)placer);
		world.setBlockMetadataWithNotify(i, j, k, orient);
	}
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if ( tileEntity instanceof TileEntityIBN523 ) {
			((TileEntityIBN523)tileEntity).destroy();
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if ( !(tileEntity instanceof TileEntityIBN523) ) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;
		
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			
			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				
				EntityItem entityItem = new EntityItem(
						world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.itemID, item.stackSize, item.getItemDamage())
				);
				
				if (item.hasTagCompound()) {
					entityItem.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}
				
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		notifyNeighbors(world,i,j,k);
	}
	public static void notifyNeighbors(World world, int i, int j, int k) {
		world.notifyBlocksOfNeighborChange(i, j, k, 0);
		world.notifyBlocksOfNeighborChange(i-1, j, k, 0);
		world.notifyBlocksOfNeighborChange(i+1, j, k, 0);
		world.notifyBlocksOfNeighborChange(i, j-1, k, 0);
		world.notifyBlocksOfNeighborChange(i, j+1, k, 0);
		world.notifyBlocksOfNeighborChange(i, j, k-1, 0);
		world.notifyBlocksOfNeighborChange(i, j, k+1, 0);
	}
	@Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		TileEntity tileEntity = world.getBlockTileEntity(i,j,k);
		if ( tileEntity instanceof TileEntityIBN523 ) {
			((TileEntityIBN523) tileEntity).setPoweringDirection(
					1,
					world.isBlockProvidingPowerTo(i, j - 1, k, 0)
			);
			((TileEntityIBN523) tileEntity).setPoweringDirection(
					0,
					world.isBlockProvidingPowerTo(i, j + 1, k, 1)
			);

			((TileEntityIBN523) tileEntity).setPoweringDirection(
					3,
					world.isBlockProvidingPowerTo(i, j, k - 1, 2)
			);
			((TileEntityIBN523) tileEntity).setPoweringDirection(
					2,
					world.isBlockProvidingPowerTo(i, j, k + 1, 3)
			);

			((TileEntityIBN523) tileEntity).setPoweringDirection(
					5,
					world.isBlockProvidingPowerTo(i - 1, j, k, 4)
			);
			((TileEntityIBN523) tileEntity).setPoweringDirection(
					4,
					world.isBlockProvidingPowerTo(i + 1, j, k, 5)
			);
		}
	}
	
	@Override 
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}
	
	@Override
    public boolean canProvidePower() {
		return true;
	}
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
    	TileEntity entity = iblockaccess.getBlockTileEntity(i, j, k);
    	if ( entity instanceof TileEntityIBN523 ) {
	    	return ((TileEntityIBN523)entity).isPoweringDirection(l);
    	}
    	return false;
	}
	@Override
    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world,i,j,k,l);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityIBN523();
	}
	@Override
	public int getBlockTextureFromSideAndMetadata(int l, int orient) {
		if ( l > 1 &&  orient > 1 ) {
			switch ( orient ) {
				case 2:
					switch ( l ) {
						case 2:
							return 3;
						case 3:
							return 2;
						case 4:
							return 5;
						case 5:
							return 4;
					}
				case 3:
					switch ( l ) {
					case 2:
						return 2;
					case 3:
						return 3;
					case 4:
						return 4;
					case 5:
						return 5;
				}
				case 4:
					switch ( l ) {
					case 2:
						return 5;
					case 3:
						return 4;
					case 4:
						return 3;
					case 5:
						return 2;
				}
				case 5:
					switch ( l ) {
					case 2:
						return 4;
					case 3:
						return 5;
					case 4:
						return 2;
					case 5:
						return 3;
				}
			}
		}
		
		return l;
	}
	
	@Override
	public int getBlockTexture(IBlockAccess world, int i, int j, int k, int l) {
		int orient = world.getBlockMetadata(i, j, k);
		return getBlockTextureFromSideAndMetadata(l,orient);
	}
	
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random rnd) {
		/*
		float randomTick = rnd.nextFloat();
		if ( randomTick < 0.1 ) {
			randomTick = rnd.nextFloat();
			if ( randomTick < 0.1 ) {
				randomTick = rnd.nextFloat();
				if ( randomTick < 0.1 ) {
					randomTick = rnd.nextFloat();
					if ( randomTick < 0.1 ) {
						randomTick = rnd.nextFloat();
						if ( randomTick < 0.5 ) {
							ci++;
						} else {
							cc++;
						}
					} else {
						cc++;
					}
				} else {
					cc++;
				}
			} else {
				cc++;
			}
		} else {
			cc++;
		}
		System.out.println(cc+":"+ci);
		*/
	}
}
