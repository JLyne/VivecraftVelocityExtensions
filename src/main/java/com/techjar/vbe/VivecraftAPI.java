package com.techjar.vbe;

import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class VivecraftAPI {
	public static boolean isVive(Player p) {
		return p != null && VVE.vivePlayers.containsKey(p.getUniqueId());
	}

	public static boolean isVR(Player p) {
		return p != null && VVE.vivePlayers.containsKey(p.getUniqueId()) && VVE.vivePlayers.get(p.getUniqueId());
	}

	public static boolean isVive(UUID uuid) {
		return uuid != null && VVE.vivePlayers.containsKey(uuid);
	}

	public static boolean isVR(UUID uuid) {
		return uuid != null && VVE.vivePlayers.containsKey(uuid) && VVE.vivePlayers.get(uuid);
	}
}
