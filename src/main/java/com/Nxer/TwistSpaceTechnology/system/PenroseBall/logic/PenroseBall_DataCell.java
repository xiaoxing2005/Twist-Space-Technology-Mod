package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.items.ItemBlackHoleDataDepositor.numberFormat;
import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_WorldSavedData.markDataDirty;

import java.io.Serializable;
import java.math.BigInteger;

public class PenroseBall_DataCell implements Serializable {

    private int PenroseBallLevel;
    private int photon;
    private int index;
    private double BlackHoleAngularMomentum;
    private int BlackHoleMass;
    private double Radius;
    private String ownerName;
    private Boolean useBigInteger;
    private Long LastAddStoreEnergy = 0L;
    private BigInteger LastAddStoreEnergy_BigInteger = BigInteger.ZERO;
    private Long StoreEnergy = 0L;
    private BigInteger StoreEnergy_BigInteger = BigInteger.ZERO;

    public PenroseBall_DataCell(String ownerName, BlackHole blackHole) {
        this.ownerName = ownerName;
        this.BlackHoleAngularMomentum = blackHole.getAngularMomentum();
        this.BlackHoleMass = blackHole.getMass();
        this.photon = 0;
        this.Radius = calculateRadius(blackHole);
        this.useBigInteger = false;
        this.PenroseBallLevel = 1;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void onTickAddStoreEnergy() {
        if (photon != 0) {
            if (!useBigInteger) {
                if (LastAddStoreEnergy == 0) {
                    LastAddStoreEnergy = (long) Math.pow(Integer.MAX_VALUE, 2);
                    addStoreEnergy(LastAddStoreEnergy);
                    markDataDirty();
                } else if (photon > 0) {
                    LastAddStoreEnergy = (long) Math.pow(LastAddStoreEnergy, 2);
                    addStoreEnergy(LastAddStoreEnergy);
                    markDataDirty();
                }
            }
            if ((LastAddStoreEnergy_BigInteger.compareTo(BigInteger.ZERO)) == 0) {
                addStoreEnergy_BigInteger(BigInteger.valueOf((long) Math.pow(Integer.MAX_VALUE, 2)));
                markDataDirty();
            } else if (photon > 0) {
                LastAddStoreEnergy_BigInteger = LastAddStoreEnergy_BigInteger.pow(2);
                addStoreEnergy_BigInteger(LastAddStoreEnergy_BigInteger);
                markDataDirty();
            }
        }
    }

    public String getStoreEnergy() {
        if (useBigInteger) {
            return StoreEnergy_BigInteger.toString();
        }
        return numberFormat.format(StoreEnergy);
    }

    public void setPenroseBallLevel(int penroseBallLevel) {
        PenroseBallLevel = penroseBallLevel;
        markDataDirty();
    }

    public void addPhoton(int amount) {
        this.photon = amount;
        markDataDirty();
    }

    public void reducePhoton(int amount) {
        this.photon = Math.max(0, this.photon - amount);
        markDataDirty();
    }

    public void addStoreEnergy(Long amount) {
        if ((this.StoreEnergy += amount) < 0) {
            useBigInteger = true;
            addStoreEnergy_BigInteger(BigInteger.valueOf(amount));
        } else {
            this.StoreEnergy += amount;
        }
        markDataDirty();
    }

    public void reduceStoreEnergy(Long amount) {
        markDataDirty();
        this.StoreEnergy = Math.max(0, this.StoreEnergy - amount);
    }

    public void addStoreEnergy_BigInteger(BigInteger amount) {
        markDataDirty();
        LastAddStoreEnergy_BigInteger = this.StoreEnergy_BigInteger.add(amount);
    }

    public void reduceStoreEnergy_BigInteger(BigInteger amount) {
        markDataDirty();
        StoreEnergy_BigInteger = this.StoreEnergy_BigInteger.subtract(amount);
        if (StoreEnergy_BigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0) {
            useBigInteger = false;
            StoreEnergy = StoreEnergy_BigInteger.longValue();
        }
    }

    public static final double G = 6.67 * Math.pow(10, -11);
    public static final double c = Math.pow(10, 8);
    public static final double TY = 2.0 * Math.pow(10, 30);

    public static double calculateRadius(BlackHole blackHole) {
        return ((G * blackHole.getMass()) * TY / Math.pow(c, 2))
            + (Math.pow(Math.sqrt((G * blackHole.getMass() * TY / Math.pow(c, 2))), 2))
            - Math.pow(blackHole.getAngularMomentum() * TY / c, 2)
            + blackHole.getPenroseBallRevisedValue();
    }
}
