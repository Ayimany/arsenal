package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.util.DispersionMethods;

public class PumpActionShotgun extends Shotgun{

    public PumpActionShotgun() {
        super(
                DispersionMethods.NEGATIVE_GAUSSIAN,
                11,
                70,
                4,
                1f,
                1f,
                7f
        );
    }
}
