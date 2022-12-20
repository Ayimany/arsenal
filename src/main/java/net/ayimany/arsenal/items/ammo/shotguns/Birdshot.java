package net.ayimany.arsenal.items.ammo.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;

/**
 * 16 weak pellets. 1 damage. Point-blank should yield 16 damage.
 **/
public class Birdshot extends AmmoUnit {

    public Birdshot() {
        super("Birdshot", Context.SHOTGUN, 1, 16, 1);
    }

}
