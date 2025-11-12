
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  2 : 20  @  2 : 13

~ 
~ import com.google.common.base.Splitter;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiNetworkSettingsButton;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenConnectOption;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANConnecting;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerList;
~ import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServer;
~ import net.minecraft.client.audio.PositionedSoundRecord;

> DELETE  3  @  3 : 5

> CHANGE  1 : 3  @  1 : 4

~ import net.minecraft.util.EnumChatFormatting;
~ import net.minecraft.util.ResourceLocation;

> INSERT  1 : 24  @  1

+ /**
+  * + This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source
+  * code.
+  * 
+  * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!" Mod
+  * Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
+  * 
+  * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights
+  * Reserved.
+  * 
+  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
+  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
+  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
+  * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
+  * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
+  * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
+  * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
+  * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
+  * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
+  * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
+  * POSSIBILITY OF SUCH DAMAGE.
+  * 
+  */

> DELETE  2  @  2 : 3

> INSERT  11 : 16  @  11

+ 
+ 	public ServerData getSelectedServer() {
+ 		return selectedServer;
+ 	}
+ 

> DELETE  1  @  1 : 3

> INSERT  1 : 2  @  1

+ 	private int lastServerCount = -1; // Used to detect when the server list has changed

> INSERT  1 : 7  @  1

+ 	private static LANServerList lanServerList = null;
+ 
+ 	public int ticksOpened;
+ 
+ 	private final GuiNetworkSettingsButton relaysButton;
+ 

> INSERT  2 : 6  @  2

+ 		this.relaysButton = new GuiNetworkSettingsButton(this);
+ 		if (lanServerList != null) {
+ 			lanServerList.forceRefresh();
+ 		}

> INSERT  2 : 7  @  2

+ 	/**
+ 	 * + Adds the buttons (and other controls) to the screen in question. Called
+ 	 * when the GUI is displayed and when the window resizes, the buttonList is
+ 	 * cleared beforehand.
+ 	 */

> CHANGE  5 : 6  @  5 : 6

~ 			this.savedServerList = ServerList.getServerList();

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 8

> INSERT  3 : 7  @  3

+ 			if (lanServerList == null) {
+ 				lanServerList = new LANServerList();
+ 			}
+ 			lanServerList.forceRefresh();

> INSERT  7 : 10  @  7

+ 	/**
+ 	 * + Handles mouse input.
+ 	 */

> INSERT  5 : 10  @  5

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		this.serverListSelector.handleTouchInput();
+ 	}
+ 

> INSERT  18 : 21  @  18

+ 	/**
+ 	 * + Called from the main game loop to update the screen.
+ 	 */

> CHANGE  2 : 9  @  2 : 6

~ 
~ 		// Check if the total number of servers (local + remote) has changed
~ 		int currentServerCount = this.savedServerList.countServers();
~ 		if (this.lastServerCount != currentServerCount) {
~ 			this.lastServerCount = currentServerCount;
~ 			// The list has been updated (e.g., by the async fetch), so rebuild the GUI list
~ 			this.serverListSelector.func_148195_a(this.savedServerList);

> CHANGE  2 : 7  @  2 : 3

~ 		this.savedServerList.updateServerPing();
~ 		if (lanServerList.update()) {
~ 			this.selectServer(-1);
~ 		}
~ 		++ticksOpened;

> INSERT  2 : 5  @  2

+ 	/**
+ 	 * + Called when the screen is unloaded. Used to disable keyboard repeat events
+ 	 */

> DELETE  2  @  2 : 8

> CHANGE  2 : 7  @  2 : 3

~ 	/**
~ 	 * + Called by the controls from the buttonList when activated. (Mouse pressed
~ 	 * for buttons)
~ 	 */
~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  19 : 21  @  19 : 22

~ 				this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
~ 				this.mc.displayGuiScreen(new GuiScreenConnectOption(this));

> CHANGE  8 : 13  @  8 : 11

~ 				if (serverdata != null) {
~ 					this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
~ 					this.selectedServer.copyFrom(serverdata);
~ 					this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
~ 				}

> CHANGE  9 : 10  @  9 : 10

~ 	public void refreshServerList() {

> CHANGE  14 : 15  @  14 : 16

~ 			this.refreshServerList();

> INSERT  10 : 13  @  10

+ 				if (!this.selectedServer.enableCookies) {
+ 					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
+ 				}

> CHANGE  5 : 6  @  5 : 7

~ 			this.refreshServerList();

> INSERT  6 : 9  @  6

+ 				if (serverdata.enableCookies && !this.selectedServer.enableCookies) {
+ 					ServerCookieDataStore.clearCookie(this.selectedServer.serverIP);
+ 				}

> CHANGE  4 : 5  @  4 : 6

~ 			this.refreshServerList();

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 3  @  1

+ 	public void cancelDirectConnect() {
+ 		this.directConnect = false;

> CHANGE  2 : 8  @  2 : 3

~ 	/**
~ 	 * + Fired when a key is typed (except F11 which toggles full screen). This is
~ 	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
~ 	 * (character on the key), keyCode (lwjgl Keyboard key code)
~ 	 */
~ 	protected void keyTyped(char parChar1, int parInt1) {

> DELETE  18  @  18 : 27

> CHANGE  11 : 12  @  11 : 12

~ 					} else if (i < this.serverListSelector.getSize() - 1) {

> DELETE  2  @  2 : 11

> INSERT  15 : 19  @  15

+ 	/**
+ 	 * + Draws the screen and all the components in it. Args : mouseX, mouseY,
+ 	 * renderPartialTicks
+ 	 */

> INSERT  7 : 9  @  7

+ 		relaysButton.drawScreen(i, j);
+ 		drawPluginDownloadLink(i, j);

> INSERT  2 : 3  @  2

+ 			GlStateManager.disableLighting();

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 17  @  1

+ 	private void drawPluginDownloadLink(int xx, int yy) {
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 
+ 		String text = "Download EaglerXServer";
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		boolean hover = xx > width - 5 - (w + 5) * 3 / 4 && yy > 1 && xx < width - 2 && yy < 12;
+ 		if (hover) {
+ 			Mouse.showCursor(EnumCursorType.HAND);
+ 		}
+ 
+ 		drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, (width - 1) * 4 / 3 - w - 5, 5,
+ 				hover ? 0xFFEEEE22 : 0xFFCCCCCC);
+ 
+ 		GlStateManager.popMatrix();

> CHANGE  3 : 12  @  3 : 13

~ 		if (this.serverListSelector.func_148193_k() < this.serverListSelector.getOrigSize()) {
~ 			GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0
~ 					? null
~ 					: this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
~ 			if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
~ 				this.connectToServer(((ServerListEntryNormal) guilistextended$iguilistentry).getServerData());
~ 			}
~ 		} else {
~ 			int par1 = this.serverListSelector.func_148193_k() - this.serverListSelector.getOrigSize();

> INSERT  1 : 7  @  1

+ 			if (par1 < lanServerList.countServers()) {
+ 				LANServerList.LanServer var2 = lanServerList.getServer(par1);
+ 				connectToLAN("Connecting to '" + var2.getLanServerMotd() + "'...", var2.getLanServerCode(),
+ 						var2.getLanServerRelay());
+ 			}
+ 		}

> INSERT  6 : 11  @  6

+ 	private void connectToLAN(String text, String code, RelayServer uri) {
+ 		this.mc.loadingScreen.resetProgressAndMessage(text);
+ 		this.mc.displayGuiScreen(new GuiScreenLANConnecting(this, code, uri));
+ 	}
+ 

> CHANGE  7 : 8  @  7 : 9

~ 		if (guilistextended$iguilistentry != null) {

> CHANGE  1 : 3  @  1 : 2

~ 			if (guilistextended$iguilistentry instanceof ServerListEntryNormal
~ 					&& ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData() != null) {

> DELETE  4  @  4 : 5

> DELETE  2  @  2 : 6

> CHANGE  4 : 9  @  4 : 5

~ 	/**
~ 	 * + Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
~ 	 */
~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
~ 		relaysButton.mouseClicked(parInt1, parInt2, parInt3);

> INSERT  2 : 9  @  2

+ 		String text = "Download EaglerXServer";
+ 		int w = mc.fontRendererObj.getStringWidth(text);
+ 		if (parInt1 > width - 5 - (w + 5) * 3 / 4 && parInt2 > 1 && parInt1 < width - 2 && parInt2 < 12) {
+ 			this.mc.getSoundHandler()
+ 					.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 			EagRuntime.openLink("https://lax1dude.net/eaglerxserver");
+ 		}

> INSERT  2 : 6  @  2

+ 	/**
+ 	 * + Called when a mouse button is released. Args : mouseX, mouseY,
+ 	 * releaseButton
+ 	 */

> INSERT  9 : 13  @  9

+ 	static LANServerList getLanServerList() {
+ 		return lanServerList;
+ 	}
+ 

> EOF
