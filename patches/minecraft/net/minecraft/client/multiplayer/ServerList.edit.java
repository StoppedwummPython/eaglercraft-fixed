
# Eagler Context Redacted Diff
# Copyright (c) 2026 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import java.nio.charset.StandardCharsets;
~ import java.util.HashSet;

> INSERT  1 : 20  @  1

+ import java.util.Set;
+ import java.util.function.BiConsumer;
+ 
+ import org.json.JSONArray;
+ import org.json.JSONObject;
+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
+ import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
+ import net.lax1dude.eaglercraft.v1_8.internal.EnumServerRateLimit;
+ import net.lax1dude.eaglercraft.v1_8.internal.IClientConfigAdapter.DefaultServer;
+ import net.lax1dude.eaglercraft.v1_8.internal.QueryResponse;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
+ import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;
+ import net.lax1dude.eaglercraft.v1_8.socket.ServerQueryDispatch;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.texture.TextureManager;

> CHANGE  3 : 4  @  3 : 5

~ import net.minecraft.util.EnumChatFormatting;

> CHANGE  4 : 5  @  4 : 5

~ 	private final List<ServerData> allServers = Lists.newArrayList();

> CHANGE  1 : 8  @  1 : 2

~ 	private final List<ServerData> servers = Lists.newArrayList(); // User-added servers
~ 	private final List<ServerData> topServers = Lists.newArrayList(); // TopEaglerServers API
~ 	private final List<ServerData> temporaryServers = Lists.newArrayList(); // Vercel API
~ 
~ 	private static ServerList instance = null;
~ 
~ 	private ServerList(Minecraft mcIn) {

> INSERT  4 : 12  @  4

+ 	public static void initServerList(Minecraft mc) {
+ 		instance = new ServerList(mc);
+ 	}
+ 
+ 	public static ServerList getServerList() {
+ 		return instance;
+ 	}
+ 

> INSERT  1 : 5  @  1

+ 		loadServerList(EagRuntime.getStorage("s"));
+ 	}
+ 
+ 	public void loadServerList(byte[] localStorage) {

> INSERT  1 : 2  @  1

+ 			freeServerIcons();

> CHANGE  1 : 10  @  1 : 4

~ 			this.topServers.clear();
~ 			this.temporaryServers.clear();
~ 			this.allServers.clear();
~ 
~ 			for (DefaultServer srv : EagRuntime.getConfiguration().getDefaultServerList()) {
~ 				ServerData dat = new ServerData(srv.name, srv.addr, true);
~ 				dat.isDefault = true;
~ 				dat.hideAddress = srv.hideAddress;
~ 				this.servers.add(dat);

> CHANGE  2 : 11  @  2 : 6

~ 			if (localStorage != null) {
~ 				NBTTagCompound nbttagcompound = CompressedStreamTools
~ 						.readCompressed(new EaglerInputStream(localStorage));
~ 				if (nbttagcompound != null) {
~ 					NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
~ 					for (int i = 0; i < nbttaglist.tagCount(); ++i) {
~ 						this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
~ 					}
~ 				}

> INSERT  1 : 5  @  1

+ 
+ 			// Fetch both remote lists
+ 			this.fetchTopEaglerServersAsync();
+ 

> CHANGE  1 : 2  @  1 : 2

~ 			logger.error("Couldn't load server list", exception);

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 3

~ 	public byte[] writeServerList() {

> INSERT  1 : 2  @  1

+ 			NBTTagCompound nbttagcompound = new NBTTagCompound();

> CHANGE  3 : 6  @  3 : 4

~ 				if (!serverdata.isDefault) {
~ 					nbttaglist.appendTag(serverdata.getNBTCompound());
~ 				}

> DELETE  1  @  1 : 3

> CHANGE  1 : 5  @  1 : 2

~ 
~ 			EaglerOutputStream bao = new EaglerOutputStream();
~ 			CompressedStreamTools.writeCompressed(nbttagcompound, bao);
~ 			return bao.toByteArray();

> CHANGE  1 : 3  @  1 : 2

~ 			logger.error("Couldn't write server list", exception);
~ 			return null;

> INSERT  1 : 2  @  1

+ 	}

> INSERT  1 : 6  @  1

+ 	public void saveServerList() {
+ 		byte[] data = writeServerList();
+ 		if (data != null) {
+ 			EagRuntime.setStorage("s", data);
+ 		}

> CHANGE  2 : 28  @  2 : 4

~ 	public ServerData getServerData(int index) {
~ 		// 1. Check user-added servers
~ 		if (index < this.servers.size()) {
~ 			return this.servers.get(index);
~ 		}
~ 		index -= this.servers.size();
~ 
~ 		// 2. Handle the separator if we have any remote servers
~ 		if (!this.temporaryServers.isEmpty() || !this.topServers.isEmpty()) {
~ 			if (index == 0)
~ 				return null; // Separator
~ 			index -= 1;
~ 		}
~ 
~ 		// 3. Check Top Servers
~ 		if (index < this.topServers.size()) {
~ 			return this.topServers.get(index);
~ 		}
~ 		index -= this.topServers.size();
~ 
~ 		// 4. Check Vercel Servers
~ 		if (index < this.temporaryServers.size()) {
~ 			return this.temporaryServers.get(index);
~ 		}
~ 
~ 		return null;

> CHANGE  2 : 10  @  2 : 4

~ 	public void removeServerData(int index) {
~ 		if (index < this.servers.size()) {
~ 			ServerData data = this.servers.remove(index);
~ 			if (data != null && data.iconTextureObject != null) {
~ 				mc.getTextureManager().deleteTexture(data.iconResourceLocation);
~ 				data.iconTextureObject = null;
~ 			}
~ 		}

> CHANGE  7 : 12  @  7 : 8

~ 		int count = this.servers.size() + this.topServers.size() + this.temporaryServers.size();
~ 		if (!this.temporaryServers.isEmpty() || !this.topServers.isEmpty()) {
~ 			count++; // Add one for the separator
~ 		}
~ 		return count;

> CHANGE  2 : 9  @  2 : 7

~ 	public void swapServers(int index1, int index2) {
~ 		if (index1 < this.servers.size() && index2 < this.servers.size()) {
~ 			ServerData serverdata = this.servers.get(index1);
~ 			this.servers.set(index1, this.servers.get(index2));
~ 			this.servers.set(index2, serverdata);
~ 			this.saveServerList();
~ 		}

> CHANGE  2 : 6  @  2 : 4

~ 	public void func_147413_a(int index, ServerData parServerData) {
~ 		if (index < this.servers.size()) {
~ 			this.servers.set(index, parServerData);
~ 		}

> CHANGE  3 : 6  @  3 : 8

~ 		ServerList serverlist = getServerList();
~ 		for (int i = 0; i < serverlist.servers.size(); ++i) {
~ 			ServerData serverdata = serverlist.servers.get(i);

> CHANGE  2 : 3  @  2 : 3

~ 				serverlist.servers.set(i, parServerData);

> DELETE  3  @  3 : 4

> INSERT  2 : 182  @  2

+ 
+ 	public void freeServerIcons() {
+ 		TextureManager mgr = mc.getTextureManager();
+ 		List<ServerData> combined = Lists.newArrayList();
+ 		combined.addAll(this.servers);
+ 		combined.addAll(this.topServers);
+ 		combined.addAll(this.temporaryServers);
+ 
+ 		for (ServerData server : combined) {
+ 			if (server.iconTextureObject != null) {
+ 				mgr.deleteTexture(server.iconResourceLocation);
+ 				server.iconTextureObject = null;
+ 			}
+ 		}
+ 	}
+ 
+ 	public void refreshServerPing() {
+ 		List<ServerData> serversToPing = Lists.newArrayList();
+ 		serversToPing.addAll(this.servers);
+ 		serversToPing.addAll(this.topServers);
+ 		serversToPing.addAll(this.temporaryServers);
+ 		for (ServerData dat : serversToPing) {
+ 			if (dat.currentQuery != null) {
+ 				if (dat.currentQuery.isOpen()) {
+ 					dat.currentQuery.close();
+ 				}
+ 				dat.currentQuery = null;
+ 			}
+ 			dat.hasPing = false;
+ 			dat.pingSentTime = -1L;
+ 		}
+ 	}
+ 
+ 	public void updateServerPing() {
+ 		int total = 0;
+ 		List<ServerData> serversToUpdate = Lists.newArrayList();
+ 		serversToUpdate.addAll(this.servers);
+ 		serversToUpdate.addAll(this.topServers);
+ 		serversToUpdate.addAll(this.temporaryServers);
+ 
+ 		for (ServerData dat : serversToUpdate) {
+ 			if (dat.pingSentTime <= 0l) {
+ 				dat.pingSentTime = EagRuntime.steadyTimeMillis();
+ 				if (RateLimitTracker.isLockedOut(dat.serverIP)) {
+ 					dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
+ 					dat.pingToServer = -1l;
+ 					dat.hasPing = true;
+ 					dat.field_78841_f = true;
+ 				} else {
+ 					dat.pingToServer = -2l;
+ 					String addr = AddressResolver.resolveURI(dat.serverIP);
+ 					dat.currentQuery = ServerQueryDispatch.sendServerQuery(addr, "MOTD");
+ 					if (dat.currentQuery == null) {
+ 						dat.pingToServer = -1l;
+ 						dat.hasPing = true;
+ 						dat.field_78841_f = true;
+ 					} else {
+ 						++total;
+ 					}
+ 				}
+ 			} else if (dat.currentQuery != null) {
+ 				dat.currentQuery.update();
+ 				if (!dat.hasPing) {
+ 					++total;
+ 					EnumServerRateLimit rateLimit = dat.currentQuery.getRateLimit();
+ 					if (rateLimit != EnumServerRateLimit.OK) {
+ 						if (rateLimit == EnumServerRateLimit.BLOCKED) {
+ 							RateLimitTracker.registerBlock(dat.serverIP);
+ 						} else if (rateLimit == EnumServerRateLimit.LOCKED_OUT) {
+ 							RateLimitTracker.registerLockOut(dat.serverIP);
+ 						}
+ 						dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
+ 						dat.pingToServer = -1l;
+ 						dat.hasPing = true;
+ 						return;
+ 					}
+ 				}
+ 				if (dat.currentQuery.responsesAvailable() > 0) {
+ 					QueryResponse pkt;
+ 					do {
+ 						pkt = dat.currentQuery.getResponse();
+ 					} while (dat.currentQuery.responsesAvailable() > 0);
+ 					if (pkt.responseType.equalsIgnoreCase("MOTD") && pkt.isResponseJSON()) {
+ 						dat.setMOTDFromQuery(pkt);
+ 						if (!dat.hasPing) {
+ 							dat.pingToServer = pkt.clientTime - dat.pingSentTime;
+ 							dat.hasPing = true;
+ 						}
+ 					}
+ 				}
+ 				if (dat.currentQuery.binaryResponsesAvailable() > 0) {
+ 					byte[] r;
+ 					do {
+ 						r = dat.currentQuery.getBinaryResponse();
+ 					} while (dat.currentQuery.binaryResponsesAvailable() > 0);
+ 					dat.setIconPacket(r);
+ 				}
+ 				if (!dat.currentQuery.isOpen() && dat.pingSentTime > 0l
+ 						&& (EagRuntime.steadyTimeMillis() - dat.pingSentTime) > 2000l && !dat.hasPing) {
+ 					dat.pingToServer = -1l;
+ 					dat.hasPing = true;
+ 				}
+ 			}
+ 			if (total >= 4) {
+ 				break;
+ 			}
+ 		}
+ 	}
+ 
+ 	/**
+ 	 * Fetches TopEaglerServers using corsproxy.io
+ 	 */
+ 	public void fetchTopEaglerServersAsync() {
+ 		String target = "https://topeaglerservers.com/api/search/header?limit=4";
+ 		String proxyUrl = "https://corsproxy.io/?" + target;
+ 
+ 		EagRuntime.downloadRemoteURIByteArray(proxyUrl, (data, error) -> {
+ 			if (error != null || data == null)
+ 				return;
+ 			try {
+ 				String jsonStr = new String(data, StandardCharsets.UTF_8);
+ 				JSONObject json = new JSONObject(jsonStr);
+ 				JSONArray arr = json.getJSONArray("servers");
+ 
+ 				List<ServerData> newTop = Lists.newArrayList();
+ 				for (int i = 0; i < arr.length(); i++) {
+ 					JSONObject srv = arr.getJSONObject(i);
+ 					String name = EnumChatFormatting.GOLD + "[Top] " + EnumChatFormatting.RESET + srv.getString("name");
+ 					String addr = srv.getString("address");
+ 					if (!addr.startsWith("ws"))
+ 						addr = "wss://" + addr;
+ 
+ 					ServerData dat = new ServerData(name, addr, false);
+ 					newTop.add(dat);
+ 				}
+ 
+ 				synchronized (this) {
+ 					this.topServers.clear();
+ 					this.topServers.addAll(newTop);
+ 				}
+ 				refreshServerPing();
+ 			} catch (Exception ex) {
+ 				logger.error("Error parsing TopEaglerServers JSON", ex);
+ 			}
+ 		});
+ 	}
+ 
+ 	public void fetchRemoteServersAsync(final String url) {
+ 		EagRuntime.downloadRemoteURIByteArray(url, (data, error) -> {
+ 			if (error != null || data == null)
+ 				return;
+ 			try {
+ 				String jsonStr = new String(data, StandardCharsets.UTF_8);
+ 				JSONObject json = new JSONObject(jsonStr);
+ 				if (!json.has("success") || !json.getBoolean("success"))
+ 					return;
+ 
+ 				JSONArray arr = json.getJSONArray("data");
+ 				List<ServerData> newTemp = Lists.newArrayList();
+ 				Set<String> dedupe = new HashSet<>();
+ 				for (Object e : arr) {
+ 					if (!(e instanceof JSONObject))
+ 						continue;
+ 					JSONObject srv = (JSONObject) e;
+ 					String addr = srv.getString("address");
+ 					if (dedupe.contains(addr))
+ 						continue;
+ 					dedupe.add(addr);
+ 					newTemp.add(new ServerData(srv.getString("name"), addr, false));
+ 				}
+ 				synchronized (this) {
+ 					temporaryServers.clear();
+ 					temporaryServers.addAll(newTemp);
+ 				}
+ 				refreshServerPing();
+ 			} catch (Exception ex) {
+ 				logger.error("Error parsing remote server list JSON", ex);
+ 			}
+ 		});
+ 	}

> EOF
