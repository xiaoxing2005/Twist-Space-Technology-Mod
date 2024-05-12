package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_EnergyGenerator;

public class DSP_Event {

    public static void onTick() {
        DSP_SolarSailDecreaser.INSTANCE.onTick();
        PenroseBall_EnergyGenerator.INSTANCE.onTick();
    }
}
