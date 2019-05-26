package com.esophose.ver18.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.ver18.playerparticles.PPlayer;
import com.esophose.ver18.playerparticles.styles.api.PParticle;
import com.esophose.ver18.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSpiral implements ParticleStyle {

    private float stepX = 0;

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int stepY = -60; stepY < 60; stepY += 10) {
            double dx = -(Math.cos(((stepX + stepY) / 90) * Math.PI * 2)) * 0.8;
            double dy = stepY / 45D;
            double dz = -(Math.sin(((stepX + stepY) / 90) * Math.PI * 2)) * 0.8;
            particles.add(new PParticle(new Location(location.getWorld(), location.getX() + dx, location.getY() + dy, location.getZ() + dz)));
        }
        return particles.toArray(new PParticle[particles.size()]);
    }

    public void updateTimers() {
        stepX++;
    }

    public String getName() {
        return "spiral";
    }

    public boolean canBeFixed() {
        return true;
    }

}
