package com.Nxer.TwistSpaceTechnology.system.PenroseBall.items;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.BlackHole.getBlackHole;
import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.BlackHole.willBlackHoleIntoPenroseBall;
import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.logic.DataStorageMaps.PenroseBallDate;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.util.TextHandler;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.IItemWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.VanillaButtonWidget;

public class ItemBlackHoleDataDepositor extends Item implements IItemWithModularUI {

    ItemStackHandler phantomInventory = new ItemStackHandler(2);
    public static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    private int INDEX;

    public ItemBlackHoleDataDepositor(String aName, String aMetaName, CreativeTabs aCreativeTabs) {
        super();
        this.setCreativeTab(aCreativeTabs);
        this.setUnlocalizedName(aMetaName);
        this.setMaxStackSize(1);
        TextHandler.texter(aName, this.getUnlocalizedName() + ".name");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            UIInfos.PLAYER_HELD_ITEM_UI
                .open(playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return itemStackIn;
    }

    public static ItemStack addBlackHoleNBT(ItemStack itemStack, int distance, int Mass, double AngularMomentum) {
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt != null && nbt.getTagList("BlackHoleList", 10) != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(
                "index",
                nbt.getTagList("BlackHoleList", 10)
                    .tagCount());
            tag.setInteger("Mass", Mass);
            tag.setInteger("Distance", distance);
            tag.setDouble("AngularMomentum", AngularMomentum);
            nbt.getTagList("BlackHoleList", 10)
                .appendTag(tag);
            return itemStack;
        }
        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound NBT = new NBTTagCompound();
        tag.setInteger("index", 0);
        tag.setInteger("Mass", Mass);
        tag.setInteger("Distance", distance);
        tag.setDouble("AngularMomentum", AngularMomentum);
        list.appendTag(tag);
        NBT.setTag("BlackHoleList", list);
        itemStack.setTagCompound(NBT);
        return itemStack;
    }

    public static void setLock(ItemStack itemStack, int lock) {
        itemStack.stackTagCompound.setInteger("Lock", lock);
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext, ItemStack heldStack) {
        phantomInventory.setStackInSlot(1, GTCMItemList.PowerChair.get(1));
        ModularWindow.Builder builder = ModularWindow.builder(320, 240);
        builder.setBackground(AdaptableUITexture.of("gtnhcommunitymod", "textures/gui/gui.png", 320, 240, 1));
        buildContext.addSyncedWindow(1, this::createAnotherWindow);
        NBTTagCompound nbt = heldStack.getTagCompound();
        if (getBlack(nbt)) {
            for (int i = 0; i < heldStack.getTagCompound()
                .getTagList("BlackHoleList", 10)
                .tagCount(); i++) {
                int finalI = i;
                builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    INDEX = finalI;
                    if (!widget.isClient()) {
                        widget.getContext()
                            .openSyncedWindow(1);
                    }
                })
                    .setPos(i * 160, i * 125)
                    .setSize(16, 16)
                    .setInternalName("Test")
                    .setBackground(UITexture.fullImage("gtnhcommunitymod", "textures/gui/blackhole.png")));
            }
        }
        return builder.build();
    }

    private boolean getBlack(NBTTagCompound nbt) {
        return nbt != null;
    }

    public ModularWindow createAnotherWindow(EntityPlayer player) {
        ItemStack itemStack = player.getHeldItem();
        NBTTagCompound nbt = itemStack.getTagCompound()
            .getTagList("BlackHoleList", 10)
            .getCompoundTagAt(INDEX);
        int Mass = nbt.getInteger("Mass");
        int Distance = nbt.getInteger("Distance");
        double AngularMomentum = nbt.getDouble("AngularMomentum");
        int index = nbt.getInteger("index");
        ModularWindow.Builder builder = ModularWindow.builder(120, 120);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        if (!(INDEX < 0)) {
            return builder.setBackground(ModularUITextures.VANILLA_BACKGROUND)
                .widget(
                    ButtonWidget.closeWindowButton(true)
                        .setPos(85, 5))
                .widget(
                    new DynamicTextWidget(
                        () -> new Text(
                            "??? : " + PenroseBallDate.get(player.getPersistentID())
                                .get(0)
                                .getStoreEnergy())).setDefaultColor(EnumChatFormatting.WHITE)
                                    .setTextAlignment(Alignment.CenterLeft)
                                    .setPos(5, 50))
                .widget(
                    new DynamicTextWidget(() -> new Text("Mass : " + numberFormat.format(Mass)))
                        .setDefaultColor(EnumChatFormatting.WHITE)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(5, 40))
                .widget(
                    new DynamicTextWidget(() -> new Text("Distance :" + numberFormat.format(Distance)))
                        .setDefaultColor(EnumChatFormatting.WHITE)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(5, 20))
                .widget(
                    new DynamicTextWidget(() -> new Text("AngularMomentum :" + numberFormat.format(AngularMomentum)))
                        .setDefaultColor(EnumChatFormatting.WHITE)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(5, 30))
                .widget(
                    new VanillaButtonWidget().setDisplayString("Lock")
                        .setOnClick((clickData, widget) -> {
                            if (!widget.isClient()) {
                                setLock(itemStack, INDEX);
                                willBlackHoleIntoPenroseBall(
                                    player.getPersistentID(),
                                    getBlackHole(
                                        itemStack.getTagCompound()
                                            .getTagList("BlackHoleList", 10)
                                            .getCompoundTagAt(index)),
                                    player.getDisplayName());
                            }
                        })
                        .setPos(40, 0)
                        .setSize(32, 16)
                        .setInternalName("lock"))

                .build();
        }
        return builder.build();
    }
}
