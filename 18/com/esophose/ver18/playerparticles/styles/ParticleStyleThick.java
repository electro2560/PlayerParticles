package com.esophose.ver18.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.ver18.playerparticles.PPlayer;
import com.esophose.ver18.playerparticles.styles.api.PParticle;
import com.esophose.ver18.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleThick implements ParticleStyle {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        PParticle[] baseParticles = DefaultStyles.NONE.getParticles(pplayer, location);

        int multiplyingFactor = 15; // Uses the same logic as ParticleStyleNone except multiplies the resulting particles by 15x
        PParticle[] particles = new PParticle[baseParticles.length * multiplyingFactor];
        for (int i = 0; i < baseParticles.length * multiplyingFactor; i++) {
            particles[i] = baseParticles[i % baseParticles.length];
        }

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "thick";
    }
    
    public boolean canBeFixed() {
        return true;
    }

}
