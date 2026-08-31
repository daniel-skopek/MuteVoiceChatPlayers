package me.eren.muteVoiceChatPlayers;

import litebans.api.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * When the player joins check if they are muted.
 * If so, add them to the muted cache.
 * <p>
 * When they leave, remove them from the cache
 */
public class LogListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MuteVoiceChatPlayers plugin = MuteVoiceChatPlayers.instance();
		UUID uuid = event.getPlayer().getUniqueId();
		String ip = event.getPlayer().getAddress().getAddress().getHostAddress();

		plugin.getServer().getAsyncScheduler().runNow(plugin, (task) -> {
			if (Database.get().isPlayerMuted(uuid, ip)) {
				LitebansVCPlugin.mutePlayer(uuid);
			}
		});
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		LitebansVCPlugin.unmutePlayer(event.getPlayer().getUniqueId());
	}

}
