package net.ibn523.gui;

import net.ibn523.CommonProxy;
import net.ibn523.items.data.DataPunchedCard;
import net.ibn523.network.ClientPacketHandler;
import net.ibn523.network.packages.PunchedCardAddHole;
import net.ibn523.network.packages.PunchedCardUpdateRequest;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.RenderHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.server.FMLServerHandler;

@SideOnly(Side.CLIENT)
public class GuiPunchedCard extends GuiScreen {
    private int xSize;
    private int ySize;
    private DataPunchedCard data;
    private int dataId;
    
    public GuiPunchedCard(DataPunchedCard data, int dataId) {
		this.xSize = 253;
		this.ySize = 168;
		this.data = data;
		this.dataId = dataId;
		
		PunchedCardUpdateRequest packet = new PunchedCardUpdateRequest(dataId);
		ClientPacketHandler.sendPacket(packet);
    }
    
    public void setData(DataPunchedCard data) {
    	this.data = data;
    }
    
	@Override
	public void initGui() {
		this.controlList.clear();
		 
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		if ( !data.isHoled(0, 0) )
			controlList.add(new GuiButtonPunchedCard(0, k+29, l+48));
		if ( !data.isHoled(0, 1) )
			controlList.add(new GuiButtonPunchedCard(1, k+29, l+64));
		if ( !data.isHoled(0, 2) )
			controlList.add(new GuiButtonPunchedCard(2, k+29, l+78));
		if ( !data.isHoled(0, 3) )
			controlList.add(new GuiButtonPunchedCard(3, k+29, l+93));
		if ( !data.isHoled(0, 4) )
			controlList.add(new GuiButtonPunchedCard(4, k+29, l+108));
		if ( !data.isHoled(0, 5) )
			controlList.add(new GuiButtonPunchedCard(5, k+29, l+123));
		if ( !data.isHoled(0, 6) )
			controlList.add(new GuiButtonPunchedCard(6, k+29, l+138));
		if ( !data.isHoled(0, 7) )
			controlList.add(new GuiButtonPunchedCard(7, k+29, l+153));

		if ( !data.isHoled(1, 0) )
			controlList.add(new GuiButtonPunchedCard(8, k+41, l+48));
		if ( !data.isHoled(1, 1) )
			controlList.add(new GuiButtonPunchedCard(9, k+41, l+64));
		if ( !data.isHoled(1, 2) )
			controlList.add(new GuiButtonPunchedCard(10, k+41, l+78));
		if ( !data.isHoled(1, 3) )
			controlList.add(new GuiButtonPunchedCard(11, k+41, l+93));
		if ( !data.isHoled(1, 4) )
			controlList.add(new GuiButtonPunchedCard(12, k+41, l+108));
		if ( !data.isHoled(1, 5) )
			controlList.add(new GuiButtonPunchedCard(13, k+41, l+123));
		if ( !data.isHoled(1, 6) )
			controlList.add(new GuiButtonPunchedCard(14, k+41, l+138));
		if ( !data.isHoled(1, 7) )
			controlList.add(new GuiButtonPunchedCard(15, k+41, l+153));

		if ( !data.isHoled(2, 0) )
			controlList.add(new GuiButtonPunchedCard(16, k+53, l+48));
		if ( !data.isHoled(2, 1) )
			controlList.add(new GuiButtonPunchedCard(17, k+53, l+64));
		if ( !data.isHoled(2, 2) )
			controlList.add(new GuiButtonPunchedCard(18, k+53, l+78));
		if ( !data.isHoled(2, 3) )
			controlList.add(new GuiButtonPunchedCard(19, k+53, l+93));
		if ( !data.isHoled(2, 4) )
			controlList.add(new GuiButtonPunchedCard(20, k+53, l+108));
		if ( !data.isHoled(2, 5) )
			controlList.add(new GuiButtonPunchedCard(21, k+53, l+123));
		if ( !data.isHoled(2, 6) )
			controlList.add(new GuiButtonPunchedCard(22, k+53, l+138));
		if ( !data.isHoled(2, 7) )
			controlList.add(new GuiButtonPunchedCard(23, k+53, l+153));

		if ( !data.isHoled(3, 0) )
			controlList.add(new GuiButtonPunchedCard(24, k+73, l+48));
		if ( !data.isHoled(3, 1) )
			controlList.add(new GuiButtonPunchedCard(25, k+73, l+64));
		if ( !data.isHoled(3, 2) )
			controlList.add(new GuiButtonPunchedCard(26, k+73, l+78));
		if ( !data.isHoled(3, 3) )
			controlList.add(new GuiButtonPunchedCard(27, k+73, l+93));
		if ( !data.isHoled(3, 4) )
			controlList.add(new GuiButtonPunchedCard(28, k+73, l+108));
		if ( !data.isHoled(3, 5) )
			controlList.add(new GuiButtonPunchedCard(29, k+73, l+123));
		if ( !data.isHoled(3, 6) )
			controlList.add(new GuiButtonPunchedCard(30, k+73, l+138));
		if ( !data.isHoled(3, 7) )
			controlList.add(new GuiButtonPunchedCard(31, k+73, l+153));

		if ( !data.isHoled(4, 0) )
			controlList.add(new GuiButtonPunchedCard(32, k+85, l+48));
		if ( !data.isHoled(4, 1) )
			controlList.add(new GuiButtonPunchedCard(33, k+85, l+64));
		if ( !data.isHoled(4, 2) )
			controlList.add(new GuiButtonPunchedCard(34, k+85, l+78));
		if ( !data.isHoled(4, 3) )
			controlList.add(new GuiButtonPunchedCard(35, k+85, l+93));
		if ( !data.isHoled(4, 4) )
			controlList.add(new GuiButtonPunchedCard(36, k+85, l+108));
		if ( !data.isHoled(4, 5) )
			controlList.add(new GuiButtonPunchedCard(37, k+85, l+123));
		if ( !data.isHoled(4, 6) )
			controlList.add(new GuiButtonPunchedCard(38, k+85, l+138));
		if ( !data.isHoled(4, 7) )
			controlList.add(new GuiButtonPunchedCard(39, k+85, l+153));

		if ( !data.isHoled(5, 0) )
			controlList.add(new GuiButtonPunchedCard(40, k+97, l+48));
		if ( !data.isHoled(5, 1) )
			controlList.add(new GuiButtonPunchedCard(41, k+97, l+64));
		if ( !data.isHoled(5, 2) )
			controlList.add(new GuiButtonPunchedCard(42, k+97, l+78));
		if ( !data.isHoled(5, 3) )
			controlList.add(new GuiButtonPunchedCard(43, k+97, l+93));
		if ( !data.isHoled(5, 4) )
			controlList.add(new GuiButtonPunchedCard(44, k+97, l+108));
		if ( !data.isHoled(5, 5) )
			controlList.add(new GuiButtonPunchedCard(45, k+97, l+123));
		if ( !data.isHoled(5, 6) )
			controlList.add(new GuiButtonPunchedCard(46, k+97, l+138));
		if ( !data.isHoled(5, 7) )
			controlList.add(new GuiButtonPunchedCard(47, k+97, l+153));

		if ( !data.isHoled(6, 0) )
			controlList.add(new GuiButtonPunchedCard(48, k+117, l+48));
		if ( !data.isHoled(6, 1) )
			controlList.add(new GuiButtonPunchedCard(49, k+117, l+64));
		if ( !data.isHoled(6, 2) )
			controlList.add(new GuiButtonPunchedCard(50, k+117, l+78));
		if ( !data.isHoled(6, 3) )
			controlList.add(new GuiButtonPunchedCard(51, k+117, l+93));
		if ( !data.isHoled(6, 4) )
			controlList.add(new GuiButtonPunchedCard(52, k+117, l+108));
		if ( !data.isHoled(6, 5) )
			controlList.add(new GuiButtonPunchedCard(53, k+117, l+123));
		if ( !data.isHoled(6, 6) )
			controlList.add(new GuiButtonPunchedCard(54, k+117, l+138));
		if ( !data.isHoled(6, 7) )
			controlList.add(new GuiButtonPunchedCard(55, k+117, l+153));

		if ( !data.isHoled(7, 0) )
			controlList.add(new GuiButtonPunchedCard(56, k+129, l+48));
		if ( !data.isHoled(7, 1) )
			controlList.add(new GuiButtonPunchedCard(57, k+129, l+64));
		if ( !data.isHoled(7, 2) )
			controlList.add(new GuiButtonPunchedCard(58, k+129, l+78));
		if ( !data.isHoled(7, 3) )
			controlList.add(new GuiButtonPunchedCard(59, k+129, l+93));
		if ( !data.isHoled(7, 4) )
			controlList.add(new GuiButtonPunchedCard(60, k+129, l+108));
		if ( !data.isHoled(7, 5) )
			controlList.add(new GuiButtonPunchedCard(61, k+129, l+123));
		if ( !data.isHoled(7, 6) )
			controlList.add(new GuiButtonPunchedCard(62, k+129, l+138));
		if ( !data.isHoled(7, 7) )
			controlList.add(new GuiButtonPunchedCard(63, k+129, l+153));

		if ( !data.isHoled(8, 0) )
			controlList.add(new GuiButtonPunchedCard(64, k+141, l+48));
		if ( !data.isHoled(8, 1) )
			controlList.add(new GuiButtonPunchedCard(65, k+141, l+64));
		if ( !data.isHoled(8, 2) )
			controlList.add(new GuiButtonPunchedCard(66, k+141, l+78));
		if ( !data.isHoled(8, 3) )
			controlList.add(new GuiButtonPunchedCard(67, k+141, l+93));
		if ( !data.isHoled(8, 4) )
			controlList.add(new GuiButtonPunchedCard(68, k+141, l+108));
		if ( !data.isHoled(8, 5) )
			controlList.add(new GuiButtonPunchedCard(69, k+141, l+123));
		if ( !data.isHoled(8, 6) )
			controlList.add(new GuiButtonPunchedCard(70, k+141, l+138));
		if ( !data.isHoled(8, 7) )
			controlList.add(new GuiButtonPunchedCard(71, k+141, l+153));

		if ( !data.isHoled(9, 0) )
			controlList.add(new GuiButtonPunchedCard(72, k+160, l+48));
		if ( !data.isHoled(9, 1) )
			controlList.add(new GuiButtonPunchedCard(73, k+160, l+64));
		if ( !data.isHoled(9, 2) )
			controlList.add(new GuiButtonPunchedCard(74, k+160, l+78));
		if ( !data.isHoled(9, 3) )
			controlList.add(new GuiButtonPunchedCard(75, k+160, l+93));
		if ( !data.isHoled(9, 4) )
			controlList.add(new GuiButtonPunchedCard(76, k+160, l+108));
		if ( !data.isHoled(9, 5) )
			controlList.add(new GuiButtonPunchedCard(77, k+160, l+123));
		if ( !data.isHoled(9, 6) )
			controlList.add(new GuiButtonPunchedCard(78, k+160, l+138));
		if ( !data.isHoled(9, 7) )
			controlList.add(new GuiButtonPunchedCard(79, k+160, l+153));

		if ( !data.isHoled(10, 0) )
			controlList.add(new GuiButtonPunchedCard(80, k+172, l+48));
		if ( !data.isHoled(10, 1) )
			controlList.add(new GuiButtonPunchedCard(81, k+172, l+64));
		if ( !data.isHoled(10, 2) )
			controlList.add(new GuiButtonPunchedCard(82, k+172, l+78));
		if ( !data.isHoled(10, 3) )
			controlList.add(new GuiButtonPunchedCard(83, k+172, l+93));
		if ( !data.isHoled(10, 4) )
			controlList.add(new GuiButtonPunchedCard(84, k+172, l+108));
		if ( !data.isHoled(10, 5) )
			controlList.add(new GuiButtonPunchedCard(85, k+172, l+123));
		if ( !data.isHoled(10, 6) )
			controlList.add(new GuiButtonPunchedCard(86, k+172, l+138));
		if ( !data.isHoled(10, 7) )
			controlList.add(new GuiButtonPunchedCard(87, k+172, l+153));

		if ( !data.isHoled(11, 0) )
			controlList.add(new GuiButtonPunchedCard(88, k+184, l+48));
		if ( !data.isHoled(11, 1) )
			controlList.add(new GuiButtonPunchedCard(89, k+184, l+64));
		if ( !data.isHoled(11, 2) )
			controlList.add(new GuiButtonPunchedCard(90, k+184, l+78));
		if ( !data.isHoled(11, 3) )
			controlList.add(new GuiButtonPunchedCard(91, k+184, l+93));
		if ( !data.isHoled(11, 4) )
			controlList.add(new GuiButtonPunchedCard(92, k+184, l+108));
		if ( !data.isHoled(11, 5) )
			controlList.add(new GuiButtonPunchedCard(93, k+184, l+123));
		if ( !data.isHoled(11, 6) )
			controlList.add(new GuiButtonPunchedCard(94, k+184, l+138));
		if ( !data.isHoled(11, 7) )
			controlList.add(new GuiButtonPunchedCard(95, k+184, l+153));

		if ( !data.isHoled(12, 0) )
			controlList.add(new GuiButtonPunchedCard(96, k+205, l+48));
		if ( !data.isHoled(12, 1) )
			controlList.add(new GuiButtonPunchedCard(97, k+205, l+64));
		if ( !data.isHoled(12, 2) )
			controlList.add(new GuiButtonPunchedCard(98, k+205, l+78));
		if ( !data.isHoled(12, 3) )
			controlList.add(new GuiButtonPunchedCard(99, k+205, l+93));
		if ( !data.isHoled(12, 4) )
			controlList.add(new GuiButtonPunchedCard(100, k+205, l+108));
		if ( !data.isHoled(12, 5) )
			controlList.add(new GuiButtonPunchedCard(101, k+205, l+123));
		if ( !data.isHoled(12, 6) )
			controlList.add(new GuiButtonPunchedCard(102, k+205, l+138));
		if ( !data.isHoled(12, 7) )
			controlList.add(new GuiButtonPunchedCard(103, k+205, l+153));

		if ( !data.isHoled(13, 0) )
			controlList.add(new GuiButtonPunchedCard(104, k+217, l+48));
		if ( !data.isHoled(13, 1) )
			controlList.add(new GuiButtonPunchedCard(105, k+217, l+64));
		if ( !data.isHoled(13, 2) )
			controlList.add(new GuiButtonPunchedCard(106, k+217, l+78));
		if ( !data.isHoled(13, 3) )
			controlList.add(new GuiButtonPunchedCard(107, k+217, l+93));
		if ( !data.isHoled(13, 4) )
			controlList.add(new GuiButtonPunchedCard(108, k+217, l+108));
		if ( !data.isHoled(13, 5) )
			controlList.add(new GuiButtonPunchedCard(109, k+217, l+123));
		if ( !data.isHoled(13, 6) )
			controlList.add(new GuiButtonPunchedCard(110, k+217, l+138));
		if ( !data.isHoled(13, 7) )
			controlList.add(new GuiButtonPunchedCard(111, k+217, l+153));

		if ( !data.isHoled(14, 0) )
			controlList.add(new GuiButtonPunchedCard(112, k+229, l+48));
		if ( !data.isHoled(14, 1) )
			controlList.add(new GuiButtonPunchedCard(113, k+229, l+64));
		if ( !data.isHoled(14, 2) )
			controlList.add(new GuiButtonPunchedCard(114, k+229, l+78));
		if ( !data.isHoled(14, 3) )
			controlList.add(new GuiButtonPunchedCard(115, k+229, l+93));
		if ( !data.isHoled(14, 4) )
			controlList.add(new GuiButtonPunchedCard(116, k+229, l+108));
		if ( !data.isHoled(14, 5) )
			controlList.add(new GuiButtonPunchedCard(117, k+229, l+123));
		if ( !data.isHoled(14, 6) )
			controlList.add(new GuiButtonPunchedCard(118, k+229, l+138));
		if ( !data.isHoled(14, 7) )
			controlList.add(new GuiButtonPunchedCard(119, k+229, l+153));
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		//drawDefaultBackground();
		
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		
		GL11.glPushMatrix();
		GL11.glTranslatef(k, l, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		drawGuiContainerForegroundLayer();
		GL11.glPopMatrix();
		
		super.drawScreen(i, j, f);
		
		drawGuiContainerBackgroundLayer(f);
		
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		int line = id/8;
		int bit = id%8;
		PunchedCardAddHole packet = new PunchedCardAddHole(
				dataId,
				line,
				bit
		);
		ClientPacketHandler.sendPacket(packet);
		controlList.remove(guibutton);
	}
	
	@Override
	public void handleKeyboardInput() {
		super.handleKeyboardInput();
		
		if(Keyboard.getEventKeyState()) {
			int inventoryKey = 0;
			for ( Object o: KeyBinding.keybindArray ) {
				if ( ((KeyBinding)o).keyDescription.equals("key.inventory") ) {
					inventoryKey = ((KeyBinding)o).keyCode;
					break;
				}
			}
			if( Keyboard.getEventKey() == inventoryKey || Keyboard.getEventKey() == 28 ) {
				close();
				return;
			}
		}
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState() )
			close();
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	
	private void drawGuiContainerBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture(CommonProxy.PCARD_BG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}
	
	private void drawGuiContainerForegroundLayer(){
		
	}
	
	public void close() {
		mc.displayGuiScreen(null);
		mc.setIngameFocus();
	}
}
