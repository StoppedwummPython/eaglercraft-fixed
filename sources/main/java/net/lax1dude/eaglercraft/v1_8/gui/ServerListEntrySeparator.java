package net.lax1dude.eaglercraft.v1_8.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.*;

/**
 * This is a new class that represents a non-interactive separator in the server list.
 * It's used to visually divide the user's saved servers from the remote server list.
 */
public class ServerListEntrySeparator implements GuiListExtended.IGuiListEntry {

    private final Minecraft mc;

    public ServerListEntrySeparator() {
        this.mc = Minecraft.getMinecraft();
    }

    /**
     * Draws the separator text centered in the list slot.
     */
    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        String text = I18n.format("multiplayer.remoteServers");
        int stringWidth = this.mc.fontRendererObj.getStringWidth(text);
        this.mc.fontRendererObj.drawString(text, x + listWidth / 2 - stringWidth / 2, y + slotHeight / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2, 0x808080);
    }

    /**
     * Returns false so this entry can't be selected or clicked.
     */
    @Override
    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        return false;
    }

    /**
     * Does nothing when the mouse is released.
     */
    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        // No action
    }

    /**
     * Does nothing. This entry cannot be "selected".
     */
    @Override
    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        // No action
    }
}