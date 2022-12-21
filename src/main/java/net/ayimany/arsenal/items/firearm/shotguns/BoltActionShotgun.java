package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.util.DispersionMethods;

public class BoltActionShotgun extends Shotgun {

    public BoltActionShotgun() {
        super(
                DispersionMethods.NEGATIVE_GAUSSIAN,
                20,
                60,
                5,
                1.15f,
                2.5f,
                6.0f
        );
    }
}
