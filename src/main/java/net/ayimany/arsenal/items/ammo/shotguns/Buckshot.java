package net.ayimany.arsenal.items.ammo.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;

/**
 * 8 powerful pellets shoot out. 3 damage each. Point-blank shots should yield 24 total damage.
 **/
public class Buckshot extends AmmoUnit {

    public Buckshot() {
        super("Buckshot", Context.SHOTGUN, 3, 8, 2);
    }

}
