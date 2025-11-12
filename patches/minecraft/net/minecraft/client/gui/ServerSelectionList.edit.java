
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 8  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
+ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerList;
+ import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
+ import net.lax1dude.eaglercraft.v1_8.gui.ServerListEntrySeparator;

> CHANGE  1 : 2  @  1 : 6

~ import net.minecraft.client.multiplayer.ServerData;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.resources.I18n;

> CHANGE  3 : 5  @  3 : 6

~ 	// This list now holds different types of entries, including separators
~ 	private final List<GuiListExtended.IGuiListEntry> listEntries = Lists.newArrayList();

> INSERT  2 : 7  @  2

+ 	// LAN logic can remain mostly the same
+ 	private final ServerListEntryNormal serverListEntryLAN;
+ 
+ 	private int userServerCount = 0;
+ 

> INSERT  4 : 18  @  4

+ 		// This LAN entry logic seems complex and specific, we'll keep it but adapt it.
+ 		this.serverListEntryLAN = new ServerListEntryNormal(owner, null) {
+ 			@Override
+ 			public void drawEntry(int i, int j, int k, int l, int var5, int i1, int j1, boolean flag) {
+ 			}
+ 
+ 			@Override
+ 			public boolean mousePressed(int i, int var2, int var3, int var4, int j, int k) {
+ 				if (ServerSelectionList.this.selectedSlotIndex != i) {
+ 					super.field_148298_f = 0;
+ 				}
+ 				return super.mousePressed(i, var2, var3, var4, Math.max(j, 32), k);
+ 			}
+ 		};

> INSERT  2 : 3  @  2

+ 	@Override

> CHANGE  1 : 3  @  1 : 11

~ 		if (i < this.listEntries.size()) {
~ 			return this.listEntries.get(i);

> INSERT  1 : 5  @  1

+ 		// Fallback for LAN logic if it's still needed
+ 		i -= this.listEntries.size();
+ 		// The original logic for LAN entries seems to start after the main list
+ 		return serverListEntryLAN;

> INSERT  2 : 9  @  2

+ 	protected int getOrigSize() {
+ 		// This now refers to the size of the combined server list (user + remote +
+ 		// separator)
+ 		return this.listEntries.size();
+ 	}
+ 
+ 	@Override

> CHANGE  1 : 2  @  1 : 2

~ 		return this.listEntries.size() + GuiMultiplayer.getLanServerList().countServers();

> INSERT  6 : 7  @  6

+ 	@Override

> INSERT  8 : 11  @  8

+ 	/**
+ 	 * Rebuilds the list, creating server entries and separators as needed.
+ 	 */

> CHANGE  1 : 2  @  1 : 2

~ 		this.listEntries.clear();

> CHANGE  2 : 10  @  2 : 3

~ 			ServerData serverData = parServerList.getServerData(i);
~ 			if (serverData != null) {
~ 				// This is a regular server entry
~ 				this.listEntries.add(new ServerListEntryNormal(this.owner, serverData));
~ 			} else {
~ 				// This is the separator
~ 				this.listEntries.add(new ServerListEntrySeparator());
~ 			}

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 4  @  1

+ 	@Override
+ 	protected int getScrollBarX() {
+ 		return super.getScrollBarX() + 30;

> CHANGE  2 : 6  @  2 : 4

~ 	@Override
~ 	public int getListWidth() {
~ 		return super.getListWidth() + 85;
~ 	}

> CHANGE  1 : 6  @  1 : 4

~ 	// The rest of the drawing logic from your provided file
~ 	@Override
~ 	protected void drawSelectionBox(int mouseXIn, int mouseYIn, int parInt3, int parInt4, int i) {
~ 		super.drawSelectionBox(mouseXIn, mouseYIn, parInt3, parInt4, i + 1);
~ 	}

> INSERT  1 : 12  @  1

+ 	@Override
+ 	protected void drawSlot(int entryID, int mouseXIn, int mouseYIn, int parInt4, int parInt5, int parInt6) {
+ 		if (entryID < getOrigSize()) {
+ 			// This will now correctly draw ServerListEntryNormal AND
+ 			// ServerListEntrySeparator
+ 			super.drawSlot(entryID, mouseXIn, mouseYIn, parInt4, parInt5, parInt6);
+ 		} else if (entryID < getSize()) {
+ 			this.func_77248_b(entryID, mouseXIn, mouseYIn, parInt4);
+ 		} else {
+ 			this.func_77249_c(entryID, mouseXIn, mouseYIn, parInt4);
+ 		}

> CHANGE  2 : 14  @  2 : 4

~ 	private void func_77248_b(int par1, int par2, int par3, int par4) {
~ 		LANServerList.LanServer var6 = GuiMultiplayer.getLanServerList().getServer(par1 - getOrigSize());
~ 		this.owner.drawString(this.owner.fontRendererObj, I18n.format("lanServer.title"), par2 + 2, par3 + 1, 16777215);
~ 		this.owner.drawString(this.owner.fontRendererObj, var6.getLanServerMotd(), par2 + 2, par3 + 12, 8421504);
~ 
~ 		if (this.owner.mc.gameSettings.hideServerAddress) {
~ 			this.owner.drawString(this.owner.fontRendererObj, I18n.format("selectServer.hiddenAddress"), par2 + 2,
~ 					par3 + 12 + 11, 3158064);
~ 		} else {
~ 			this.owner.drawString(this.owner.fontRendererObj, var6.getLanServerCode(), par2 + 2, par3 + 12 + 11,
~ 					0x558822);
~ 		}

> CHANGE  2 : 32  @  2 : 4

~ 	private void func_77249_c(int par1, int par2, int par3, int par4) {
~ 		if (!LANServerController.supported())
~ 			return;
~ 		if (RelayManager.relayManager.count() == 0) {
~ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("noRelay.noRelay1"),
~ 					this.owner.width / 2, par3 + 6, 16777215);
~ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("noRelay.noRelay2"),
~ 					this.owner.width / 2, par3 + 18, 0xFFAAAAAA);
~ 		} else {
~ 			this.owner.drawCenteredString(this.owner.fontRendererObj, I18n.format("lanServer.scanning"),
~ 					this.owner.width / 2, par3 + 6, 16777215);
~ 			String var6;
~ 
~ 			switch (this.owner.ticksOpened / 3 % 4) {
~ 			case 0:
~ 			default:
~ 				var6 = "O o o";
~ 				break;
~ 
~ 			case 1:
~ 			case 3:
~ 				var6 = "o O o";
~ 				break;
~ 
~ 			case 2:
~ 				var6 = "o o O";
~ 			}
~ 
~ 			this.owner.drawCenteredString(this.owner.fontRendererObj, var6, this.owner.width / 2, par3 + 18, 8421504);
~ 		}

> EOF
