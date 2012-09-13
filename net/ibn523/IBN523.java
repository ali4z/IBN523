package net.ibn523;

import java.lang.annotation.Annotation;

import net.ibn523.blocks.BlockIBN523;
import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.gui.GuiHandler;
import net.ibn523.items.ItemPunchedCard;
import net.ibn523.network.ClientPacketHandler;
import net.ibn523.network.ServerPacketHandler;
import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
		modid="IBN523", 
		name="IBN523", 
		version="0.0.1"
)
@NetworkMod(
		clientSideRequired=true, 
		serverSideRequired=false,
		clientPacketHandlerSpec = @SidedPacketHandler(
				channels = {"PunchedCard","IBN523"},
				packetHandler = ClientPacketHandler.class
		),
		serverPacketHandlerSpec  = @SidedPacketHandler(
				channels = {"PunchedCard","IBN523"},
				packetHandler = ServerPacketHandler.class
		)
		
		
)

public class IBN523 {
	@Instance
	public static IBN523 instance;
	
	@SidedProxy(
			clientSide="net.ibn523.client.ClientProxy",
			serverSide="net.ibn523.CommonProxy"
	)
	
	public static CommonProxy proxy;

	public final static Block ibn523 = new BlockIBN523(236);
	public final static Item punchedCard = new ItemPunchedCard(6337);
	public final static GuiHandler guiHandler = new GuiHandler();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
		GameRegistry.registerBlock(ibn523);
		
	}
	@Init
	public void init(FMLInitializationEvent event) {
		LanguageRegistry.addName(punchedCard, "Punched Card");
		
		GameRegistry.registerTileEntity(TileEntityIBN523.class, "IBN523");
		LanguageRegistry.addName(ibn523, "IBN523");
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		GameRegistry.addRecipe(
				new ItemStack(punchedCard,1),
				"x", 'x', Item.paper
		);
		
		GameRegistry.addRecipe(
				new ItemStack(ibn523,1),
				"xx", 'x', Block.dirt
		);
		
		GameRegistry.addRecipe(
				new ItemStack(Item.redstone,64),
				"x", 'x', Block.dirt
		);
		GameRegistry.addRecipe(
				new ItemStack(Block.lever,64),
				"x", 'x', Item.redstone
		);
	}
}
