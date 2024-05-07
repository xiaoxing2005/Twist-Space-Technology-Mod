package com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.BlackHoleDate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import crazypants.enderio.Log;

public class PenroseBall_WorldSavedData extends WorldSavedData {

    public static PenroseBall_WorldSavedData INSTANCE;

    private static final String DATA_NAME = "TST_PenroseBallWorldSavedData";
    private static final String PenroseBallNBTTag = "TST_PenroseBall_MapNBTTag";

    private static void loadInstance(World world) {

        BlackHoleDate.clear();

        MapStorage storage = world.mapStorage;
        INSTANCE = (PenroseBall_WorldSavedData) storage.loadData(PenroseBall_WorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new PenroseBall_WorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public PenroseBall_WorldSavedData() {
        super(DATA_NAME);
    }

    @SuppressWarnings("unused")
    public PenroseBall_WorldSavedData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        try {
            byte[] ba = nbtTagCompound.getByteArray(PenroseBallNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            BlackHoleDate = (Map<UUID, ArrayList<BlackHole>>) data;
        } catch (IOException | ClassNotFoundException e) {
            Log.error(PenroseBallNBTTag + " FAILED");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(BlackHoleDate);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(PenroseBallNBTTag, data);
        } catch (IOException e) {
            Log.error(PenroseBallNBTTag + " SAVE FAILED");
        }
    }
}
