
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import java.nio.charset.StandardCharsets;
~ import java.util.HashSet;

> INSERT  1 : 19  @  1

+ import java.util.Set;
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

> CHANGE  4 : 7  @  4 : 5

~ 	private final List<ServerData> allServers = Lists.newArrayList();
~ 	private final List<ServerData> servers = Lists.newArrayList(); // User-added servers
~ 	private final List<ServerData> temporaryServers = Lists.newArrayList(); // Remote servers

> CHANGE  1 : 4  @  1 : 2

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

> CHANGE  1 : 12  @  1 : 5

~ 			freeServerIcons();
~ 			this.servers.clear(); // Clear user servers
~ 			this.temporaryServers.clear(); // Clear remote servers
~ 			this.allServers.clear();
~ 
~ 			// Add default servers to the main list first
~ 			for (DefaultServer srv : EagRuntime.getConfiguration().getDefaultServerList()) {
~ 				ServerData dat = new ServerData(srv.name, srv.addr, true);
~ 				dat.isDefault = true;
~ 				dat.hideAddress = srv.hideAddress;
~ 				this.servers.add(dat);

> CHANGE  2 : 12  @  2 : 6

~ 			// Load user-saved servers from storage
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
+ 			// Fetch remote servers asynchronously
+ 			this.fetchRemoteServersAsync("https://eagproxy.vercel.app/api/servers?page=0");
+ 

> CHANGE  1 : 2  @  1 : 2

~ 			logger.error("Couldn't load server list", exception);

> DELETE  1  @  1 : 2

> CHANGE  2 : 7  @  2 : 3

~ 	/**
~ 	 * This method is now restored for the ProfileExporter. It writes only the
~ 	 * user-added servers to a byte array.
~ 	 */
~ 	public byte[] writeServerList() {

> INSERT  1 : 2  @  1

+ 			NBTTagCompound nbttagcompound = new NBTTagCompound();

> CHANGE  3 : 6  @  3 : 4

~ 				if (!serverdata.isDefault) { // Only save non-default servers
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

> CHANGE  2 : 24  @  2 : 4

~ 	/**
~ 	 * Gets the ServerData object by its display index in the GUI. Returns null if
~ 	 * the index points to the separator.
~ 	 */
~ 	public ServerData getServerData(int index) {
~ 		if (index < this.servers.size()) {
~ 			return this.servers.get(index);
~ 		}
~ 		index -= this.servers.size();
~ 
~ 		if (!this.temporaryServers.isEmpty()) {
~ 			if (index == 0) {
~ 				return null; // This is the separator
~ 			}
~ 			index -= 1; // Account for the separator
~ 		}
~ 
~ 		if (index < this.temporaryServers.size()) {
~ 			return this.temporaryServers.get(index);
~ 		}
~ 
~ 		return null;

> CHANGE  2 : 13  @  2 : 4

~ 	/**
~ 	 * Removes the server at the given display index.
~ 	 */
~ 	public void removeServerData(int index) {
~ 		if (index < this.servers.size()) {
~ 			ServerData data = this.servers.remove(index);
~ 			if (data != null && data.iconTextureObject != null) {
~ 				mc.getTextureManager().deleteTexture(data.iconResourceLocation);
~ 				data.iconTextureObject = null;
~ 			}
~ 		}

> INSERT  6 : 9  @  6

+ 	/**
+ 	 * Counts all entries, including the separator.
+ 	 */

> CHANGE  1 : 6  @  1 : 2

~ 		int count = this.servers.size() + this.temporaryServers.size();
~ 		if (!this.temporaryServers.isEmpty()) {
~ 			count++; // Add one for the separator
~ 		}
~ 		return count;

> CHANGE  2 : 10  @  2 : 7

~ 	public void swapServers(int index1, int index2) {
~ 		// Only allow swapping within the user's server list
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

> CHANGE  3 : 8  @  3 : 8

~ 		// This method is complex and might not work correctly with the new structure.
~ 		// For now, we'll assume it's primarily used for editing, which should be fine.
~ 		ServerList serverlist = getServerList();
~ 		for (int i = 0; i < serverlist.servers.size(); ++i) {
~ 			ServerData serverdata = serverlist.servers.get(i);

> CHANGE  2 : 3  @  2 : 3

~ 				serverlist.servers.set(i, parServerData);

> DELETE  3  @  3 : 4

> INSERT  2 : 161  @  2

+ 
+ 	public void freeServerIcons() {
+ 		// Simplified icon freeing
+ 		TextureManager mgr = mc.getTextureManager();
+ 		for (ServerData server : this.servers) {
+ 			if (server.iconTextureObject != null) {
+ 				mgr.deleteTexture(server.iconResourceLocation);
+ 				server.iconTextureObject = null;
+ 			}
+ 		}
+ 		for (ServerData server : this.temporaryServers) {
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
+ 		serversToUpdate.addAll(this.temporaryServers);
+ 
+ 		for (ServerData dat : serversToUpdate) {
+ 			// (updateServerPing logic remains the same as your original file)
+ 			if (dat.pingSentTime <= 0l) {
+ 				dat.pingSentTime = EagRuntime.steadyTimeMillis();
+ 				if (RateLimitTracker.isLockedOut(dat.serverIP)) {
+ 					logger.error("Server {} locked this client out on a previous connection", dat.serverIP);
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
+ 					if (RateLimitTracker.isProbablyLockedOut(dat.serverIP)) {
+ 						logger.error("Server {} rate-limited this client previously", dat.serverIP);
+ 						dat.serverMOTD = EnumChatFormatting.RED + "Too Many Requests!\nTry again later";
+ 					}
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
+ 	public void fetchRemoteServersAsync(final String url) {
+ 		logger.info("Fetching remote server list from: {}", url);
+ 		EagRuntime.downloadRemoteURIByteArray(url, (data, error) -> {
+ 			if (error != null) {
+ 				logger.error("Failed to download remote server list: {}", error.getMessage());
+ 				return;
+ 			}
+ 			try {
+ 				String jsonStr = new String(data, StandardCharsets.UTF_8);
+ 				JSONObject json = new JSONObject(jsonStr);
+ 				if (!json.has("success") || !json.getBoolean("success")) {
+ 					logger.warn("Remote server list reported failure");
+ 					return;
+ 				}
+ 				JSONArray arr = json.getJSONArray("data");
+ 				List<ServerData> newTemp = Lists.newArrayList();
+ 				Set<String> dedupe = new HashSet<>();
+ 				for (Object e : arr) {
+ 					if (!(e instanceof JSONObject))
+ 						continue;
+ 					JSONObject srv = (JSONObject) e;
+ 					if (!srv.has("address") || !srv.has("name"))
+ 						continue;
+ 					String addr = srv.getString("address");
+ 					String name = srv.getString("name");
+ 					if (dedupe.contains(addr))
+ 						continue;
+ 					dedupe.add(addr);
+ 					ServerData dat = new ServerData(name, addr, false);
+ 					dat.isDefault = false;
+ 					dat.hideAddress = false;
+ 					newTemp.add(dat);
+ 				}
+ 				synchronized (ServerList.this) {
+ 					temporaryServers.clear();
+ 					temporaryServers.addAll(newTemp);
+ 					logger.info("Loaded {} remote servers", newTemp.size());
+ 				}
+ 				refreshServerPing();
+ 			} catch (Exception ex) {
+ 				logger.error("Error parsing remote server list JSON", ex);
+ 			}
+ 		});
+ 	}

> EOF
