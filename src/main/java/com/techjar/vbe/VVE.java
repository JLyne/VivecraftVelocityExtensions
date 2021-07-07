package com.techjar.vbe;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Plugin(id="vivecraft-velocity-extensions", name="Vivecraft Velocity Extensions")
public class VVE {
	public static final ChannelIdentifier CHANNEL = MinecraftChannelIdentifier.create("vivecraft", "data");
	private final ProxyServer proxy;
	private final Logger logger;

	public static Map<UUID, Boolean> vivePlayers = new HashMap<>();

	@Inject
	public VVE(ProxyServer proxy, Logger logger) {
		this.proxy = proxy;
		this.logger = logger;
	}

	@Subscribe
	public void onProxyInitialised(ProxyInitializeEvent event) {
		proxy.getChannelRegistrar().register(CHANNEL);
	}

	@Subscribe
	public void onPlayerQuit(DisconnectEvent event) {
		vivePlayers.remove(event.getPlayer().getUniqueId());
	}

	@Subscribe
	public void onPluginMessage(PluginMessageEvent event) {
		ChannelIdentifier channel = event.getIdentifier();

		if(!channel.equals(CHANNEL)) {
			return;
		}

		if(event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			player.sendPluginMessage(channel, event.getData());
		} else if (event.getSource() instanceof Player) {
			byte[] data = event.getData();

			if(data.length > 0 && data[0] == 0) {
				ByteArrayInputStream byin = new ByteArrayInputStream(data);
				DataInputStream da = new DataInputStream(byin);
				InputStreamReader is = new InputStreamReader(da);
				BufferedReader br = new BufferedReader(is);

				try {
					String version = br.readLine();
					vivePlayers.put(((Player) event.getSource()).getUniqueId(), !version.contains("NONVR"));
					logger.info("Player is using vivecraft. VR? " + !version.contains("NONVR"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			((Player) event.getSource()).getCurrentServer()
					.ifPresent((ServerConnection connection) -> connection.sendPluginMessage(channel, event.getData()));
		}
	}
}
