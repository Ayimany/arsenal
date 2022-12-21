package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.util.DispersionMethods;

public class SingleShotShotgun extends Shotgun {

    public SingleShotShotgun() {
        super(
                DispersionMethods.NEGATIVE_GAUSSIAN,
                30,
                60,
                1,
                1.5f,
                3f,
                6f
        );
    }
}
