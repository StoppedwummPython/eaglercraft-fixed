
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

> INSERT  4 : 5  @  4

+ 	private final List<ServerData> allServers = Lists.newArrayList();

> INSERT  1 : 2  @  1

+ 	private final List<ServerData> temporaryServers = Lists.newArrayList();

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

> CHANGE  1 : 10  @  1 : 5

~ 			freeServerIcons();
~ 			this.temporaryServers.clear();
~ 			this.allServers.clear();
~ 			this.fetchRemoteServersAsync("https://eagproxy.vercel.app/api/servers?page=0");
~ 			for (DefaultServer srv : EagRuntime.getConfiguration().getDefaultServerList()) {
~ 				ServerData dat = new ServerData(srv.name, srv.addr, true);
~ 				dat.isDefault = true;
~ 				dat.hideAddress = srv.hideAddress;
~ 				this.allServers.add(dat);

> CHANGE  2 : 8  @  2 : 3

~ 			if (localStorage != null) {
~ 				NBTTagCompound nbttagcompound = CompressedStreamTools
~ 						.readCompressed(new EaglerInputStream(localStorage));
~ 				if (nbttagcompound == null) {
~ 					return;
~ 				}

> CHANGE  1 : 7  @  1 : 3

~ 				NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
~ 
~ 				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
~ 					ServerData srv = ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i));
~ 					this.allServers.add(srv);
~ 				}

> INSERT  1 : 2  @  1

+ 

> CHANGE  1 : 4  @  1 : 2

~ 			logger.error("Couldn't load server list", exception);
~ 		} finally {
~ 			refreshServerPing();

> INSERT  5 : 12  @  5

+ 		byte[] data = writeServerList();
+ 		if (data != null) {
+ 			EagRuntime.setStorage("s", data);
+ 		}
+ 	}
+ 
+ 	public byte[] writeServerList() {

> CHANGE  3 : 8  @  3 : 5

~ 			for (int i = 0, l = this.servers.size(); i < l; ++i) {
~ 				ServerData serverdata = this.servers.get(i);
~ 				if (!serverdata.isDefault) {
~ 					nbttaglist.appendTag(serverdata.getNBTCompound());
~ 				}

> CHANGE  4 : 9  @  4 : 5

~ 
~ 			EaglerOutputStream bao = new EaglerOutputStream();
~ 			CompressedStreamTools.writeCompressed(nbttagcompound, bao);
~ 			return bao.toByteArray();
~ 

> CHANGE  1 : 3  @  1 : 2

~ 			logger.error("Couldn't save server list", exception);
~ 			return null;

> DELETE  1  @  1 : 2

> CHANGE  3 : 8  @  3 : 4

~ 		if (parInt1 < this.servers.size()) {
~ 			return this.servers.get(parInt1);
~ 		} else {
~ 			return this.temporaryServers.get(parInt1 - this.servers.size());
~ 		}

> CHANGE  3 : 16  @  3 : 4

~ 		if (parInt1 < this.servers.size()) {
~ 			ServerData data = this.servers.remove(parInt1);
~ 			if (data != null && data.iconTextureObject != null) {
~ 				mc.getTextureManager().deleteTexture(data.iconResourceLocation);
~ 				data.iconTextureObject = null;
~ 			}
~ 		} else {
~ 			ServerData data = this.temporaryServers.remove(parInt1 - this.servers.size());
~ 			if (data != null && data.iconTextureObject != null) {
~ 				mc.getTextureManager().deleteTexture(data.iconResourceLocation);
~ 				data.iconTextureObject = null;
~ 			}
~ 		}

> INSERT  6 : 10  @  6

+ 	public void addTemporaryServerData(ServerData parServerData) {
+ 		this.temporaryServers.add(parServerData);
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 2

~ 		return this.servers.size() + this.temporaryServers.size();

> CHANGE  3 : 9  @  3 : 7

~ 		if (parInt1 < this.servers.size() && parInt2 < this.servers.size()) {
~ 			ServerData serverdata = this.getServerData(parInt1);
~ 			this.servers.set(parInt1, this.getServerData(parInt2));
~ 			this.servers.set(parInt2, serverdata);
~ 			this.saveServerList();
~ 		}

> CHANGE  3 : 8  @  3 : 4

~ 		if (parInt1 < this.servers.size()) {
~ 			this.servers.set(parInt1, parServerData);
~ 		} else {
~ 			this.temporaryServers.set(parInt1 - this.servers.size(), parServerData);
~ 		}

> INSERT  17 : 201  @  17

+ 
+ 	public void freeServerIcons() {
+ 		TextureManager mgr = mc.getTextureManager();
+ 		for (int i = 0, l = allServers.size(); i < l; ++i) {
+ 			ServerData server = allServers.get(i);
+ 			if (server.iconTextureObject != null) {
+ 				mgr.deleteTexture(server.iconResourceLocation);
+ 				server.iconTextureObject = null;
+ 			}
+ 		}
+ 		for (int i = 0, l = temporaryServers.size(); i < l; ++i) {
+ 			ServerData server = temporaryServers.get(i);
+ 			if (server.iconTextureObject != null) {
+ 				mgr.deleteTexture(server.iconResourceLocation);
+ 				server.iconTextureObject = null;
+ 			}
+ 		}
+ 	}
+ 
+ 	public void refreshServerPing() {
+ 		this.servers.clear();
+ 		this.servers.addAll(this.allServers);
+ 
+ 		List<ServerData> serversToPing = Lists.newArrayList();
+ 		serversToPing.addAll(this.servers);
+ 		serversToPing.addAll(this.temporaryServers);
+ 
+ 		for (int i = 0, l = serversToPing.size(); i < l; ++i) {
+ 			ServerData dat = serversToPing.get(i);
+ 			if (dat.currentQuery != null) {
+ 				if (dat.currentQuery.isOpen()) {
+ 					dat.currentQuery.close();
+ 				}
+ 				dat.currentQuery = null;
+ 			}
+ 			dat.hasPing = false;
+ 			dat.pingSentTime = -1l;
+ 		}
+ 	}
+ 
+ 	public void updateServerPing() {
+ 		int total = 0;
+ 		List<ServerData> serversToUpdate = Lists.newArrayList();
+ 		serversToUpdate.addAll(this.servers);
+ 		serversToUpdate.addAll(this.temporaryServers);
+ 
+ 		for (int i = 0, l = serversToUpdate.size(); i < l; ++i) {
+ 			ServerData dat = serversToUpdate.get(i);
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
+ 	// === NEW CODE BELOW ===
+ 
+ 	public void fetchRemoteServersAsync(final String url) {
+ 		logger.info("Fetching remote server list from: {}", url);
+ 
+ 		// This method is now compatible with TeaVM as it does not create any new
+ 		// threads.
+ 		// The underlying implementation of downloadRemoteURIByteArray is responsible
+ 		// for
+ 		// asynchronous execution on both desktop and web platforms.
+ 		EagRuntime.downloadRemoteURIByteArray(url, (data, error) -> {
+ 			if (error != null) {
+ 				logger.error("Failed to download remote server list: {}", error.getMessage());
+ 				return;
+ 			}
+ 
+ 			try {
+ 				String jsonStr = new String(data, StandardCharsets.UTF_8);
+ 				JSONObject json = new JSONObject(jsonStr);
+ 
+ 				if (!json.has("success") || !json.getBoolean("success")) {
+ 					logger.warn("Remote server list reported failure");
+ 					return;
+ 				}
+ 
+ 				JSONArray arr = json.getJSONArray("data");
+ 				List<ServerData> newTemp = Lists.newArrayList();
+ 				Set<String> dedupe = new HashSet<>();
+ 
+ 				for (Object e : arr) {
+ 					if (!(e instanceof JSONObject))
+ 						continue;
+ 					JSONObject srv = (JSONObject) e;
+ 					if (!srv.has("address") || !srv.has("name"))
+ 						continue;
+ 
+ 					String addr = srv.getString("address");
+ 					String name = srv.getString("name");
+ 
+ 					if (dedupe.contains(addr))
+ 						continue;
+ 					dedupe.add(addr);
+ 
+ 					ServerData dat = new ServerData(name, addr, false);
+ 					dat.isDefault = false;
+ 					dat.hideAddress = false;
+ 					newTemp.add(dat);
+ 				}
+ 
+ 				synchronized (ServerList.this) {
+ 					temporaryServers.clear();
+ 					temporaryServers.addAll(newTemp);
+ 					logger.info("Loaded {} remote servers", newTemp.size());
+ 				}
+ 
+ 				// Refresh local + remote ping data
+ 				refreshServerPing();
+ 
+ 			} catch (Exception ex) {
+ 				logger.error("Error parsing remote server list JSON", ex);
+ 			}
+ 		});
+ 	}

> EOF
