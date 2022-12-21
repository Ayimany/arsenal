package net.ayimany.arsenal.util;

import java.util.Random;

public class DispersionMethods {

    public static Random random = new Random();

    public static final DispersionMethod NEGATIVE_GAUSSIAN = (factor, count) ->
        -(factor * (float)Math.pow(Math.E, -(Math.pow(random.nextInt(count * 3), 2) / (2f * count * count)))) + factor;

}
