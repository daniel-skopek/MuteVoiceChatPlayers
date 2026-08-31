package me.eren.muteVoiceChatPlayers;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.*;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LitebansVCPlugin implements VoicechatPlugin {

	private static final Set<UUID> mutedPlayers = ConcurrentHashMap.newKeySet();

	public static void mutePlayer(UUID uuid) {
		mutedPlayers.add(uuid);
	}

	public static void unmutePlayer(UUID uuid) {
		mutedPlayers.remove(uuid);
	}

	public static boolean isMuted(UUID uuid) {
		return mutedPlayers.contains(uuid);
	}

	@Override
	public String getPluginId() {
		return "litebans-integration";
	}

	@Override
	public void registerEvents(EventRegistration registration) {
		registration.registerEvent(MicrophonePacketEvent.class, (event) -> {
			VoicechatConnection conn = event.getSenderConnection();
			if (conn == null || conn.getPlayer() == null) {
				// player is offline, likely
				return;
			}

			UUID senderUuid = conn.getPlayer().getUuid();
			if (event.isCancellable() && mutedPlayers.contains(senderUuid)) {
				event.cancel();
			}
		});
	}

}
