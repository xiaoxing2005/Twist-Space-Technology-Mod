package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

public class BlackHole {

    private final int Mass;
    private final double AngularMomentum;
    private final int Distance;

    public BlackHole(int Mass, double AngularMomentum, int Distance) {
        this.Mass = Mass;
        this.AngularMomentum = AngularMomentum;
        this.Distance = Distance;
    }

    public static BlackHole RandomBlackHole() {
        Random rand = new Random();
        int Mass = rand.nextInt(100 - 3 + 1) + 3;
        double AngularMomentum = Math.random();
        int Distance = rand.nextInt(10000 - 3000 + 1) + 3000;
        return new BlackHole(Mass, AngularMomentum, Distance);
    }

    public static BlackHole getBlackHole(NBTTagCompound nbt) {
        return new BlackHole(nbt.getInteger("Mass"), nbt.getDouble("AngularMomentum"), nbt.getInteger("Distance"));
    }

    // Returns a black hole in the form of NBTTagCompound
    public NBTTagCompound NBTBlackHole() {
        NBTTagCompound Tag = new NBTTagCompound();
        Tag.setInteger("Mass", this.Mass);
        Tag.setDouble("AngularMomentum", this.AngularMomentum);
        Tag.setInteger("Distance", this.Distance);
        return Tag;
    }

    public int getMass() {
        return Mass;
    }

    public double getAngularMomentum() {
        return AngularMomentum;
    }

    public int getDistance() {
        return Distance;
    }
}
