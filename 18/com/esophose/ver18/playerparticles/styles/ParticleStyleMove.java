package com.esophose.ver18.playerparticles.styles;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.esophose.ver18.playerparticles.PPlayer;
import com.esophose.ver18.playerparticles.manager.ConfigManager;
import com.esophose.ver18.playerparticles.manager.ParticleManager;
import com.esophose.ver18.playerparticles.manager.PermissionManager;
import com.esophose.ver18.playerparticles.styles.api.PParticle;
import com.esophose.ver18.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleMove implements ParticleStyle, Listener {
    
    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        return DefaultStyles.NONE.getParticles(pplayer, location);
    }

    public void updateTimers() {
        
    }

    public String getName() {
        return "move";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        PPlayer pplayer = ConfigManager.getInstance().getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.MOVE) {
            if (PermissionManager.hasStylePermission(e.getPlayer(), DefaultStyles.MOVE)) {
                Location loc = e.getPlayer().getLocation();
                loc.setY(loc.getY() + 0.05);
                ParticleManager.displayParticles(pplayer, DefaultStyles.MOVE.getParticles(pplayer, loc));
            }
        }
    }

}
