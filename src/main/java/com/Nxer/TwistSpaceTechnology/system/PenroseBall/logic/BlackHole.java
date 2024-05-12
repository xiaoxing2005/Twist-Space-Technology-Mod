package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.PenroseBallDate;
import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_WorldSavedData.markDataDirty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public class BlackHole implements Serializable {

    private final int Mass;
    private final double AngularMomentum;
    private final int Distance;
    private double PenroseBallRevisedValue;
    private boolean isPenroseBall;
    private PenroseBall_DataCell penroseBallDataCell;

    public BlackHole(int Mass, double AngularMomentum, int Distance) {
        this.Mass = Mass;
        this.AngularMomentum = AngularMomentum;
        this.Distance = Distance;
        this.PenroseBallRevisedValue = 1.0d;
        this.isPenroseBall = false;
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

    public double getPenroseBallRevisedValue() {
        return PenroseBallRevisedValue;
    }

    public static void willBlackHoleIntoPenroseBall(UUID uuid, BlackHole blackHole, String ownerName) {
        if (PenroseBallDate.containsKey(uuid)) {
            blackHole.penroseBallDataCell = new PenroseBall_DataCell(ownerName, blackHole);
            blackHole.penroseBallDataCell.setIndex(
                PenroseBallDate.get(uuid)
                    .size() + 1);
        } else {
            PenroseBallDate.put(uuid, new ArrayList<>());
            blackHole.penroseBallDataCell = new PenroseBall_DataCell(ownerName, blackHole);
            blackHole.penroseBallDataCell.setIndex(0);
        }
        PenroseBallDate.get(uuid)
            .add(blackHole.penroseBallDataCell);
        PenroseBall_EnergyGenerator.isDirty = true;
        markDataDirty();
    }

}
