package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.PenroseBallDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PenroseBall_EnergyGenerator {

    public static PenroseBall_EnergyGenerator INSTANCE;
    public Set<PenroseBall_DataCell> dataCells = new HashSet<>();
    public static boolean isDirty = false;

    public PenroseBall_EnergyGenerator() {
        reloadPenroseBall();
    }

    public static void init() {
        INSTANCE = new PenroseBall_EnergyGenerator();
    }

    public void onTick() {
        if (isDirty) {
            isDirty = false;
            reloadPenroseBall();
        }
        if (!dataCells.isEmpty()) {
            for (PenroseBall_DataCell dataCell : dataCells) {
                dataCell.onTickAddStoreEnergy();
            }
        }
    }

    public void reloadPenroseBall() {
        for (ArrayList<PenroseBall_DataCell> penroseBallDataCell : PenroseBallDate.values()) {
            dataCells.addAll(penroseBallDataCell);
        }
    }
}
