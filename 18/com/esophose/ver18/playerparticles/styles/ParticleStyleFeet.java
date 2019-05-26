package com.esophose.ver18.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.ver18.playerparticles.PPlayer;
import com.esophose.ver18.playerparticles.styles.api.PParticle;
import com.esophose.ver18.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleFeet implements ParticleStyle {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        return new PParticle[] { new PParticle(location.subtract(0, 0.95, 0), 0.4F, 0.0F, 0.4F, 0.0F) };
    }

    public void updateTimers() {

    }

    public String getName() {
        return "feet";
    }

    public boolean canBeFixed() {
        return true;
    }

}
