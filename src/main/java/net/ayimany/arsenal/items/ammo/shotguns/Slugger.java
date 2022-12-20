package net.ayimany.arsenal.items.ammo.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;

/**
 * Singular bullet, Packs a strong punch. 20 damage.
 **/
public class Slugger extends AmmoUnit {

    public Slugger() {
        super("Slugger Shell", Context.SHOTGUN, 20, 1, 4);
        this.stability = 0;
    }

}
