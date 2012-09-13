package net.ibn523.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.ibn523.CommonProxy;
import net.ibn523.blocks.data.ContainerIBN523;
import net.ibn523.blocks.data.TileEntityIBN523;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.ClientPacketHandler;
import net.ibn523.network.packages.IBN523Execute;
import net.ibn523.network.packages.IBN523UpdateRequest;
import net.ibn523.network.packages.PunchedCardUpdateRequest;
import net.minecraft.src.Container;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiIBN523 extends GuiContainer {
	private InventoryPlayer inventoryPlayer;
	private TileEntityIBN523 tileentity;
	private long antiSpamTimer;

	public GuiIBN523(InventoryPlayer inventoryPlayer, TileEntityIBN523 tileentity) {
		super(new ContainerIBN523(inventoryPlayer,tileentity));
		this.inventoryPlayer = inventoryPlayer;
		this.tileentity = tileentity;
		xSize = 256;
		ySize = 222;
		
		IBN523UpdateRequest packet = new IBN523UpdateRequest(
				tileentity.xCoord,
				tileentity.yCoord,
				tileentity.zCoord
		);
		ClientPacketHandler.sendPacket(packet);
	}

	@Override
	public void initGui() {
		super.initGui();

		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		controlList.add(new GuiButton(100, k+7, l+90, 162, 20, "Execute"));
	}
		
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		long now = System.currentTimeMillis();
		if ( now-antiSpamTimer > 100 ) {
			switch ( guibutton.id ) {
				case 100:
					IBN523Execute packet = new IBN523Execute(
							tileentity.xCoord,
							tileentity.yCoord,
							tileentity.zCoord
					);
					ClientPacketHandler.sendPacket(packet);
					break;
			}
		}
		antiSpamTimer = now;
	}
	
    public void setData(int ip, String[] code, List<DataPunchedCard> cards) {
    	tileentity.getProcessor().setRegistryValue("IP", ip);
    	tileentity.getProcessor().setCode(code);
    	tileentity.getAssembler().setPunchCards(cards);
    }
    
	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString("IBN523", 8, 6, 4210752);
		drawASM();
	}
	
	public void drawASM() {
		int poi = tileentity.getProcessor().getRegistryValue("IP");
		String[] code = tileentity.getProcessor().getCode();
		if ( code == null ) return;
		
		int i = 0;
		if ( code.length > 19 && poi > 0 ) i = poi-1;
		int k = 19;
		if ( code.length < 19 ) k = code.length;
		for ( int j = 0; j < k; j++ ) {
			if ( i+j == poi ) {
				drawRect(
						(width/2)-36-2,
						(height/2)-87+(j*10),
						(width/2)-36+70,
						(height/2)-87+(j*10)+8,
						0xff327000
				);
			}
			fontRenderer.drawString(
					code[i+j],
					(width/2)-36,
					(height/2)-87+(j*10), 0x64e000
			);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		//draw your Gui here, only thing you need to change is the path
		int texture = mc.renderEngine.getTexture(CommonProxy.IBN523_BG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
