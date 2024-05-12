package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DataStorageMaps {

    /**
     * Mapping table of UUID and Penrose ball
     * <li>Key: The player's UUID
     * <li>Value: Penrose ball owned by the player
     */
    public static Map<UUID, ArrayList<PenroseBall_DataCell>> PenroseBallDate = new HashMap<>();

}
