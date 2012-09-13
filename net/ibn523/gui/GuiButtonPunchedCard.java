package net.ibn523.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.ibn523.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

@SideOnly(Side.CLIENT)
public class GuiButtonPunchedCard extends GuiButton {

	public GuiButtonPunchedCard(int i, int j, int k)  {
		super(i, j, k, 12, 12, "");
	}


	@Override
	public void drawButton(Minecraft minecraft, int i, int j) {	
		FontRenderer fontrenderer = minecraft.fontRenderer;

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture(CommonProxy.PCARD_BTN));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawTexturedModalRect(xPosition, yPosition, 0, 0, 12, 12);
        
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof GuiButton && ((GuiButton)other).id == this.id;
	}
}
