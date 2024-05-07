package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic_t;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.BlackHoleDate;

import java.util.ArrayList;
import java.util.UUID;

import com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.BlackHole;
import com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_WorldSavedData;

public class PenroseBallManager {

    private PenroseBallManager() {}

    public static void strongCheckOrAddUser(UUID player_uuid) {
        if (!BlackHoleDate.containsKey(player_uuid)) {
            BlackHoleDate.put(player_uuid, new ArrayList<>());
        }
    }

    public static void addPenroseBallToPlayer(UUID player_uuid, BlackHole blackHole) {
        strongCheckOrAddUser(player_uuid);
        try {
            PenroseBall_WorldSavedData.INSTANCE.markDirty();
        } catch (Exception e) {
            System.out.println("Unable to mark the Penrose ball data store as Dirty");
        }
        BlackHoleDate.get(player_uuid)
            .add(blackHole);
    }
}
