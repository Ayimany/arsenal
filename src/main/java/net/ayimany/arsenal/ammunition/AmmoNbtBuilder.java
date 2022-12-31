package net.ayimany.arsenal.ammunition;

public class AmmoNbtBuilder {
    AmmoNbt nbt;

    public AmmoNbtBuilder() {
        this.nbt = new AmmoNbt();
    }

    public AmmoNbtBuilder name(String name) {
        this.nbt.putString("Name", name);
        return this;
    }

    public AmmoNbt build() {
        return nbt;
    }

}
