package com.esophose.playerparticles.particles;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.esophose.playerparticles.util.NMSUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.styles.api.PParticle;
import org.bukkit.metadata.MetadataValue;

@SuppressWarnings("deprecation")
public enum ParticleEffect {

    // Ordered and named by their Minecraft 1.13 internal names
    AMBIENT_ENTITY_EFFECT("SPELL_MOB_AMBIENT", "SPELL_MOB_AMBIENT", ParticleProperty.COLORABLE),
    ANGRY_VILLAGER("VILLAGER_ANGRY", "VILLAGER_ANGRY"),
    BARRIER("BARRIER", "BARRIER"),
    BLOCK("BLOCK_CRACK", "BLOCK_CRACK", ParticleProperty.REQUIRES_MATERIAL_DATA),
    BUBBLE("WATER_BUBBLE", "WATER_BUBBLE"),
    BUBBLE_COLUMN_UP("BUBBLE_COLUMN_UP", null),
    BUBBLE_POP("BUBBLE_POP", null),
    CAMPFIRE_COSY_SMOKE("CAMPFIRE_COSY_SMOKE", null),
    CAMPFIRE_SIGNAL_SMOKE("CAMPFIRE_SIGNAL_SMOKE", null),
    CLOUD("CLOUD", "CLOUD"),
    COMPOSTER("COMPOSTER", null),
    CRIT("CRIT", "CRIT"),
    CURRENT_DOWN("CURRENT_DOWN", null),
    DAMAGE_INDICATOR("DAMAGE_INDICATOR", "DAMAGE_INDICATOR"),
    DOLPHIN("DOLPHIN", null),
    DRAGON_BREATH("DRAGON_BREATH", "DRAGON_BREATH"),
    DRIPPING_LAVA("DRIP_LAVA", "DRIP_LAVA"),
    DRIPPING_WATER("DRIP_WATER", "DRIP_WATER"),
    DUST("REDSTONE", "REDSTONE", ParticleProperty.COLORABLE),
    // ELDER_GUARDIAN("MOB_APPEARANCE", "MOB_APPEARANCE"), // No thank you
    ENCHANT("ENCHANTMENT_TABLE", "ENCHANTMENT_TABLE"),
    ENCHANTED_HIT("CRIT_MAGIC", "CRIT_MAGIC"),
    END_ROD("END_ROD", "END_ROD"),
    ENTITY_EFFECT("SPELL_MOB", "SPELL_MOB", ParticleProperty.COLORABLE),
    EXPLOSION("EXPLOSION_LARGE", "EXPLOSION_LARGE"),
    EXPLOSION_EMITTER("EXPLOSION_HUGE", "EXPLOSION_HUGE"),
    FALLING_DUST("FALLING_DUST", "FALLING_DUST", ParticleProperty.REQUIRES_MATERIAL_DATA),
    FALLING_LAVA("FALLING_LAVA", null),
    FALLING_WATER("FALLING_WATER", null),
    FIREWORK("FIREWORKS_SPARK", "FIREWORKS_SPARK"),
    FISHING("WATER_WAKE", "WATER_WAKE"),
    FLAME("FLAME", "FLAME"),
    // FLASH("FLASH", null), // Also no thank you
    FOOTSTEP(null, "FOOTSTEP"), // Removed in Minecraft 1.13 :(
    HAPPY_VILLAGER("VILLAGER_HAPPY", "VILLAGER_HAPPY"),
    HEART("HEART", "HEART"),
    INSTANT_EFFECT("SPELL_INSTANT", "SPELL_INSTANT"),
    ITEM("ITEM_CRACK", "ITEM_CRACK", ParticleProperty.REQUIRES_MATERIAL_DATA),
    ITEM_SLIME("SLIME", "SLIME"),
    ITEM_SNOWBALL("SNOWBALL", "SNOWBALL"),
    LANDING_LAVA("LANDING_LAVA", null),
    LARGE_SMOKE("SMOKE_LARGE", "SMOKE_LARGE"),
    LAVA("LAVA", "LAVA"),
    MYCELIUM("TOWN_AURA", "TOWN_AURA"),
    NAUTILUS("NAUTILUS", null),
    NOTE("NOTE", "NOTE", ParticleProperty.COLORABLE),
    POOF("EXPLOSION_NORMAL", "EXPLOSION_NORMAL"), // The 1.13 combination of explode and showshovel
    PORTAL("PORTAL", "PORTAL"),
    RAIN("WATER_DROP", "WATER_DROP"),
    SMOKE("SMOKE_NORMAL", "SMOKE_NORMAL"),
    SNEEZE("SNEEZE", null),
    SPELL("SPELL", "SPELL"), // The Minecraft internal name for this is actually "effect", but that's the command name, so it's SPELL for the plugin instead
    SPIT("SPIT", "SPIT"),
    SPLASH("WATER_SPLASH", "WATER_SPLASH"),
    SQUID_INK("SQUID_INK", null),
    SWEEP_ATTACK("SWEEP_ATTACK", "SWEEP_ATTACK"),
    TOTEM_OF_UNDYING("TOTEM", "TOTEM"),
    UNDERWATER("SUSPENDED_DEPTH", "SUSPENDED_DEPTH"),
    WITCH("SPELL_WITCH", "SPELL_WTICH");

    public static boolean VERSION_13; // This is a particle unique to Minecraft 1.13, this is a reliable way of telling what server version is running
    private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<>();
    private static Constructor<?> DustOptions_CONSTRUCTOR;
    private static Method createBlockData_METHOD;
    private final Particle internalEnum;
    private final List<ParticleProperty> properties;

    // Initialize map for quick name and id lookup
    // Initialize Minecraft 1.13 related variables
    static {
        for (ParticleEffect effect : values())
            if (effect.isSupported())
                NAME_MAP.put(effect.getName(), effect);

        if (NMSUtil.getVersionNumber() > 12) {
            VERSION_13 = true;
            try {
                DustOptions_CONSTRUCTOR = Particle.REDSTONE.getDataType().getConstructor(Color.class, float.class);
                createBlockData_METHOD = Material.class.getMethod("createBlockData");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            DustOptions_CONSTRUCTOR = null;
            createBlockData_METHOD = null;
            VERSION_13 = false;
        }
    }

    /**
     * Construct a new particle effect
     * 
     * @param enumName Name of the Particle Enum when the server version is greater than or equal to 1.13
     * @param enumNameLegacy Name of the Particle Enum when the server version is less than 1.13
     * @param properties Properties of this particle effect
     */
    ParticleEffect(String enumName, String enumNameLegacy, ParticleProperty... properties) {
        this.properties = Arrays.asList(properties);

        Particle matchingEnum = null;
        for (Particle particle : Particle.values()) {
            if (particle.name().equals(enumName) || particle.name().equals(enumNameLegacy)) {
                matchingEnum = particle;
                break;
            }
        }

        this.internalEnum = matchingEnum; // Will be null if this server's version doesn't support this particle type
    }

    /**
     * Returns the name of this particle effect
     * 
     * @return The name
     */
    public String getName() {
        return this.name().toLowerCase();
    }

    /**
     * Gets the Spigot Particle Enum version of this ParticleEffect
     * 
     * @return The Spigot Particle Enum this ParticleEffect represents
     */
    public Particle getSpigotEnum() {
        return this.internalEnum;
    }

    /**
     * Determine if this particle effect has a specific property
     * 
     * @param property The property to check
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return this.properties.contains(property);
    }

    /**
     * Determine if this particle effect is supported by your current server
     * version
     * 
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        return this.internalEnum != null;
    }

    /**
     * Returns a ParticleEffect List of all supported effects for the server version
     * 
     * @return Supported effects
     */
    public static List<ParticleEffect> getSupportedEffects() {
        List<ParticleEffect> effects = new ArrayList<>();
        for (ParticleEffect pe : values())
            if (pe.isSupported()) 
                effects.add(pe);
        return effects;
    }

    /**
     * Returns the particle effect with the given name
     * 
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromName(String name) {
        for (Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }
    
    /**
     * Invokes the correct spawn method for the particle information given
     * 
     * @param particle The ParticlePair, given the effect/style/data
     * @param pparticle The particle spawn information
     * @param isFixedEffect If the particle is spawned from a fixed effect
     * @param owner The player that owns the particles
     */
    public static void display(ParticlePair particle, PParticle pparticle, boolean isFixedEffect, Player owner) {
        ParticleEffect effect = particle.getEffect();
        int count = pparticle.isDirectional() ? 0 : 1;
        
        if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            effect.display(particle.getSpawnMaterial(), pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), 1, pparticle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), isFixedEffect, owner);
        } else if (effect.hasProperty(ParticleProperty.COLORABLE)) {
            effect.display(particle.getSpawnColor(), pparticle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), isFixedEffect, owner);
        } else {
            effect.display(pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), count, pparticle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), isFixedEffect, owner);
        }
    }

    /**
     * Displays a particle effect
     * 
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isFixedEffect If the particle is spawned from a fixed effect
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect requires additional data
     */
    public void display(double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isFixedEffect, Player owner) throws ParticleDataException {
        if (this.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }

        for (Player player : this.getPlayersInRange(center, isFixedEffect, owner)) {
            player.spawnParticle(this.internalEnum, center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
        }
    }

    /**
     * Displays a single particle which is colored
     * 
     * @param color Color of the particle
     * @param center Center location of the effect
     * @param isFixedEffect If the particle is spawned from a fixed effect
     * @param owner The player that owns the particles
     * @throws ParticleColorException If the particle effect is not colorable or the color type is incorrect
     */
    public void display(ParticleColor color, Location center, boolean isFixedEffect, Player owner) throws ParticleColorException {
        if (!this.hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }

        if (this == DUST && VERSION_13) { // DUST uses a special data object for spawning in 1.13
            OrdinaryColor dustColor = (OrdinaryColor) color;
            Object dustData = null;
            try { // The DustData class doesn't exist in Minecraft versions less than 1.13... so this is disgusting... but it works great
                dustData = DustOptions_CONSTRUCTOR.newInstance(Color.fromRGB(dustColor.getRed(), dustColor.getGreen(), dustColor.getBlue()), 1); // Wait, you can change the size of these now??? AWESOME! I might implement this in the future!
            } catch (Exception ignored) { }

            for (Player player : this.getPlayersInRange(center, isFixedEffect, owner)) {
                player.spawnParticle(this.internalEnum, center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, dustData);
            }
        } else {
            for (Player player : this.getPlayersInRange(center, isFixedEffect, owner)) {
                // Minecraft clients require that you pass a non-zero value if the Red value should be zero
                player.spawnParticle(this.internalEnum, center.getX(), center.getY(), center.getZ(), 0, this == ParticleEffect.DUST && color.getValueX() == 0 ? Float.MIN_VALUE : color.getValueX(), color.getValueY(), color.getValueZ(), 1);
            }
        }
    }

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     * 
     * @param spawnMaterial Material of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isFixedEffect If the particle is spawned from a fixed effect
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    public void display(Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isFixedEffect, Player owner) throws ParticleDataException {
        if (!this.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }

        Object extraData = null;
        if (this.internalEnum.getDataType().getTypeName().equals("org.bukkit.block.data.BlockData")) {
            try { // The Material.createBlockData() method doesn't exist in Minecraft versions less than 1.13... so this is disgusting... but it works great
                extraData = createBlockData_METHOD.invoke(spawnMaterial);
            } catch (Exception ignored) { }
        } else if (this.internalEnum.getDataType() == ItemStack.class) {
            extraData = new ItemStack(spawnMaterial);
        } else if (this.internalEnum.getDataType() == MaterialData.class) {
            extraData = new MaterialData(spawnMaterial); // Deprecated, only used in versions < 1.13
        }

        for (Player player : this.getPlayersInRange(center, isFixedEffect, owner))
            player.spawnParticle(this.internalEnum, center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, extraData);
    }

    /**
     * Gets a List of Players within the particle display range
     * 
     * @param center The center of the radius to check around
     * @param isFixedEffect If the particle is spawned from a fixed effect
     * @param owner The player that owns the particles
     * @return A List of Players within the particle display range
     */
    private List<Player> getPlayersInRange(Location center, boolean isFixedEffect, Player owner) {
        List<Player> players = new ArrayList<>();
        int range = !isFixedEffect ? PSetting.PARTICLE_RENDER_RANGE_PLAYER.getInt() : PSetting.PARTICLE_RENDER_RANGE_FIXED_EFFECT.getInt();

        for (PPlayer pplayer : ParticleManager.getPPlayers()) {
            Player p = pplayer.getPlayer();
            if (!isFixedEffect && !this.canSee(p, owner))
                continue;

            if (p != null && pplayer.canSeeParticles() && p.getWorld().equals(center.getWorld()) && center.distanceSquared(p.getLocation()) <= range * range) {
                players.add(p);
            }
        }

        return players;
    }

    /**
     * Checks if a player can see another player
     *
     * @param player The player
     * @param target The target
     * @return True if player can see target, otherwise false
     */
    private boolean canSee(Player player, Player target) {
        if (player == null || target == null)
            return true;

        for (MetadataValue meta : target.getMetadata("vanished"))
            if (meta.asBoolean())
                return false;

        return player.canSee(target);
    }

    /**
     * Represents the property of a particle effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public enum ParticleProperty {
        /**
         * The particle effect requires block or item material data to be displayed
         */
        REQUIRES_MATERIAL_DATA,
        /**
         * The particle effect uses the offsets as color values
         */
        COLORABLE
    }

    /**
     * Represents the color for effects like {@link ParticleEffect#ENTITY_EFFECT},
     * {@link ParticleEffect#AMBIENT_ENTITY_EFFECT}, {@link ParticleEffect#DUST}
     * and {@link ParticleEffect#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static abstract class ParticleColor {
        /**
         * Returns the value for the offsetX field
         * 
         * @return The offsetX value
         */
        public abstract float getValueX();

        /**
         * Returns the value for the offsetY field
         * 
         * @return The offsetY value
         */
        public abstract float getValueY();

        /**
         * Returns the value for the offsetZ field
         * 
         * @return The offsetZ value
         */
        public abstract float getValueZ();
    }

    /**
     * Represents the color for effects like {@link ParticleEffect#ENTITY_EFFECT},
     * {@link ParticleEffect#AMBIENT_ENTITY_EFFECT} and {@link ParticleEffect#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class OrdinaryColor extends ParticleColor {
        private final int red;
        private final int green;
        private final int blue;

        /**
         * Construct a new ordinary color
         * 
         * @param red Red value of the RGB format
         * @param green Green value of the RGB format
         * @param blue Blue value of the RGB format
         * @throws IllegalArgumentException If one of the values is lower than 0
         *             or higher than 255
         */
        public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
            if ((red == 999 && green == 999 && blue == 999) || (red == 998 && green == 998 && blue == 998)) { // Allow rainbow and random values
                this.red = red;
                this.green = green;
                this.blue = blue;
            } else {
                if (red < 0) {
                    throw new IllegalArgumentException("The red value is lower than 0");
                }
                if (red > 255) {
                    throw new IllegalArgumentException("The red value is higher than 255");
                }
                this.red = red;
                if (green < 0) {
                    throw new IllegalArgumentException("The green value is lower than 0");
                }
                if (green > 255) {
                    throw new IllegalArgumentException("The green value is higher than 255");
                }
                this.green = green;
                if (blue < 0) {
                    throw new IllegalArgumentException("The blue value is lower than 0");
                }
                if (blue > 255) {
                    throw new IllegalArgumentException("The blue value is higher than 255");
                }
                this.blue = blue;
            }
        }

        /**
         * Returns the red value of the RGB format
         * 
         * @return The red value
         */
        public int getRed() {
            return this.red;
        }

        /**
         * Returns the green value of the RGB format
         * 
         * @return The green value
         */
        public int getGreen() {
            return this.green;
        }

        /**
         * Returns the blue value of the RGB format
         * 
         * @return The blue value
         */
        public int getBlue() {
            return this.blue;
        }

        /**
         * Returns the red value divided by 255
         * 
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            if (this.red == 999 || this.red == 998) return 0F;
            return (float) this.red / 255F;
        }

        /**
         * Returns the green value divided by 255
         * 
         * @return The offsetY value
         */
        @Override
        public float getValueY() {
            if (this.green == 999 || this.green == 998) return 0F;
            return (float) this.green / 255F;
        }

        /**
         * Returns the blue value divided by 255
         * 
         * @return The offsetZ value
         */
        @Override
        public float getValueZ() {
            if (this.blue == 999 || this.blue == 998) return 0F;
            return (float) this.blue / 255F;
        }
    }

    /**
     * Represents the color for the {@link ParticleEffect#NOTE} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class NoteColor extends ParticleColor {
        private final int note;

        /**
         * Construct a new note color
         * 
         * @param note Note id which determines color
         * @throws IllegalArgumentException If the note value is lower than 0 or
         *             higher than 24
         */
        public NoteColor(int note) throws IllegalArgumentException {
            if (note == 99 || note == 98) { // Allow rainbow and random values
                this.note = note;
            } else {
                if (note < 0) {
                    throw new IllegalArgumentException("The note value is lower than 0");
                }
                if (note > 24) {
                    throw new IllegalArgumentException("The note value is higher than 24");
                }
                this.note = note;
            }
        }

        /**
         * Returns the note value
         * 
         * @return The note value
         */
        public int getNote() {
            return this.note;
        }

        /**
         * Returns the note value divided by 24
         * 
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return (float) this.note / 24F;
        }

        /**
         * Returns zero because the offsetY value is unused
         * 
         * @return zero
         */
        @Override
        public float getValueY() {
            return 0;
        }

        /**
         * Returns zero because the offsetZ value is unused
         * 
         * @return zero
         */
        @Override
        public float getValueZ() {
            return 0;
        }

    }

    /**
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect requires data and has none or vice-versa or if the data
     * type is incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         * 
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect is not colorable or if the particle color type is
     * incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    private static final class ParticleColorException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737485L;

        /**
         * Construct a new particle color exception
         * 
         * @param message Message that will be logged
         */
        public ParticleColorException(String message) {
            super(message);
        }
    }

}
