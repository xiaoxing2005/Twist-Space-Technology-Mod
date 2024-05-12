package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic_t;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.PenroseBallDate;

import java.util.ArrayList;
import java.util.UUID;

import com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_DataCell;
import com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.PenroseBall_WorldSavedData;

public class PenroseBallManager {

    private PenroseBallManager() {}

    public static void strongCheckOrAddUser(UUID player_uuid) {
        if (!PenroseBallDate.containsKey(player_uuid)) {
            PenroseBallDate.put(player_uuid, new ArrayList<>());
        }
    }

    public static void addPenroseBallToPlayer(UUID player_uuid, PenroseBall_DataCell dataCell) {
        strongCheckOrAddUser(player_uuid);
        try {
            PenroseBall_WorldSavedData.INSTANCE.markDirty();
        } catch (Exception e) {
            System.out.println("Unable to mark the Penrose ball data store as Dirty");
        }
        PenroseBallDate.get(player_uuid)
            .add(dataCell);
    }
}
