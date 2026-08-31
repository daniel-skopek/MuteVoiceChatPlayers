package me.eren.muteVoiceChatPlayers;

import litebans.api.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MuteSyncTask {

	public static void startSyncTask() {
		MuteVoiceChatPlayers plugin = MuteVoiceChatPlayers.instance();

		plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, (task) -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				UUID uuid = player.getUniqueId();
				String ip = player.getAddress().getAddress().getHostAddress();

				boolean isMuted = Database.get().isPlayerMuted(uuid, ip);
				boolean isCurrentlyMuted = LitebansVCPlugin.isMuted(uuid);

				if (isMuted && !isCurrentlyMuted) {
					LitebansVCPlugin.mutePlayer(uuid);
				} else if (!isMuted && isCurrentlyMuted) {
					LitebansVCPlugin.unmutePlayer(uuid);
				}
			}
		}, 10L, 10L, TimeUnit.SECONDS);
	}
}
