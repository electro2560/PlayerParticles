package com.esophose.ver18.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.ver18.playerparticles.PPlayer;
import com.esophose.ver18.playerparticles.styles.api.PParticle;
import com.esophose.ver18.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBeam implements ParticleStyle {

    private float step = 0;
    private boolean reversed = false;

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        int points = 16;
        double radius = 1;
        double slice = 2 * Math.PI / points;
        PParticle[] particles = new PParticle[points];
        for (int i = 0; i < points; i++) {
            double angle = slice * i;
            double newX = location.getX() + radius * Math.cos(angle);
            double newY = location.getY() + (step / 10) - 1;
            double newZ = location.getZ() + radius * Math.sin(angle);
            particles[i] = new PParticle(new Location(location.getWorld(), newX, newY, newZ));
        }
        return particles;
    }

    public void updateTimers() {
        if (!reversed) step++;
        else step--;

        if (step >= 30) {
            reversed = true;
        } else if (step <= 0) {
            reversed = false;
        }
    }

    public String getName() {
        return "beam";
    }

    public boolean canBeFixed() {
        return true;
    }

}
