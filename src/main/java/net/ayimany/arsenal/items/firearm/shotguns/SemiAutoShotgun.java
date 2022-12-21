package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.util.DispersionMethods;

public class SemiAutoShotgun extends Shotgun {

    public SemiAutoShotgun() {
        super(
                DispersionMethods.NEGATIVE_GAUSSIAN,
                5,
                75,
                12,
                0.75f,
                0.75f,
                10f
        );
    }
}
