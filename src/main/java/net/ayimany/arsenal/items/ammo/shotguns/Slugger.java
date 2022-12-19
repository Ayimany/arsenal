package net.ayimany.arsenal.items.ammo.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;

public class Slugger extends AmmoUnit {

    public Slugger() {
        super("Slugger Shell", Context.SHOTGUN, 20, 10, 1);
        this.stability = 0;
    }

}
