package alternativemods.alternativeoredrop.gui.adjust;

import alternativemods.alternativeoredrop.AlternativeOreDrop;
import alternativemods.alternativeoredrop.events.ClientTickHandler;
import alternativemods.alternativeoredrop.gui.GuiAdjustRegister;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;

/**
 * Created by Du5tin on 27.12.2014.
 */
public class GuiAdjustScrollingEntry implements GuiListExtended.IGuiListEntry {

    private RenderBlocks renderBlocksRi = new RenderBlocks();
    private String oreName;
    private List<AlternativeOreDrop.OreRegister> ores;
    private GuiAdjustRegister parent;
    private boolean selected;

    public GuiAdjustScrollingEntry(String oreName, List<AlternativeOreDrop.OreRegister> ores, GuiAdjustRegister parent) {
        this.oreName = oreName;
        this.ores = ores;
        this.parent = parent;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tes, int mouseX, int mouseY, boolean isSelected) {
        FontRenderer fr = parent.mc.fontRenderer;
        parent.mc.fontRenderer.drawString(oreName, x + 40, y + 6, 0xFFFFFF);

        AlternativeOreDrop.OreRegister reg = ores.get(0);
        ItemStack is = new ItemStack((Item) Item.itemRegistry.getObject(reg.modId + ":" + reg.itemName), 1, reg.damage);
        if(selected)
            renderIn3D(is, x + 8, y + slotHeight / 2 - 8);
        else {
            RenderHelper.disableStandardItemLighting();
            RenderItem.getInstance().renderItemIntoGUI(fr, parent.mc.renderEngine, is, x + 8, y + slotHeight / 2 - 8, true);
        }
    }

    private void renderIn3D(ItemStack itemstack, int x, int y) {
        
        //Code is pretty much copy-pasted from renderItemIntoGUI
        //You probably forgot the translation bit & the texture
        parent.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Block block = Block.getBlockFromItem(itemstack.getItem());

        if (block.getRenderBlockPass() != 0) {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        } else {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(x - 2, y + 3, RenderItem.getInstance().zLevel - 3F);
        GL11.glScalef(10, 10, 10);
        GL11.glTranslatef(1F, 0.5F, 1F);
        GL11.glScalef(1, 1, -1);
        GL11.glRotatef(210, 1, 0, 0);
        
        //Alter the multiplier to change the speed
        float rotation = (ClientTickHandler.clientTicks + parent.partialTicks) * 5;
        GL11.glRotatef(rotation, 0, 1, 0);
        
        GL11.glRotatef(-90, 0, 1, 0);
        this.renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1);

        if (block.getRenderBlockPass() == 0) {
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean mousePressed(int id, int x, int y, int mouseEvent, int relX, int relY) {
        return false;
    }

    @Override
    public void mouseReleased(int id, int x, int y, int mouseEvent, int relX, int relY) {

    }
}
