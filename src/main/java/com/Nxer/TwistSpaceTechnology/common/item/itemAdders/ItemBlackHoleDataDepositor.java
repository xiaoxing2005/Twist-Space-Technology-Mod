package com.Nxer.TwistSpaceTechnology.common.item.itemAdders;

import java.util.ArrayList;

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
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();
    private int[] NBT;
    private static ArrayList<int[]> List;

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

    public static ItemStack addBlackHoleNBT(ItemStack itemStack, int index, int Mass, int AngularMomentum) {
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt.getTagList("BlackHoleList", 10) != null) {
            NBTTagList list = new NBTTagList();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("index", index);
            tag.setInteger("mass", Mass);
            tag.setInteger("angularmomentum", AngularMomentum);
            list.appendTag(tag);
            nbt.getTagList("BlackHoleList", 10)
                .appendTag(tag);
            return itemStack;
        }
        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound NBT = new NBTTagCompound();
        tag.setInteger("index", index);
        tag.setInteger("mass", Mass);
        tag.setInteger("angularmomentum", AngularMomentum);
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
            for (int i = 0; i < List.size(); i++) {
                int finalI = i;
                builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    NBT = List.get(finalI);
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
        if (nbt != null) {
            if (nbt.getTagList("BlackHoleList", 10) != null) {
                List = new ArrayList<>();
                NBTTagList list = nbt.getTagList("BlackHoleList", 10);
                for (int i = 0; i < list.tagCount(); i++) {
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    List.add(
                        new int[] { tag.getInteger("index"), tag.getInteger("mass"),
                            tag.getInteger("angularmomentum") });
                }
            }
            return true;
        }
        return false;
    }

    public ModularWindow createAnotherWindow(EntityPlayer player) {
        ItemStack itemStack = player.getHeldItem();
        NBTTagCompound nbt = itemStack.getTagCompound();
        ModularWindow.Builder builder = ModularWindow.builder(120, 120);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        if (NBT != null) {
            return builder.setBackground(ModularUITextures.VANILLA_BACKGROUND)
                .widget(
                    ButtonWidget.closeWindowButton(true)
                        .setPos(85, 5))
                .widget(
                    new DynamicTextWidget(() -> new Text("Mass : " + numberFormat.format(NBT[1])))
                        .setDefaultColor(EnumChatFormatting.WHITE)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(5, 40))
                .widget(
                    new DynamicTextWidget(() -> new Text("AngularMomentum :" + numberFormat.format(NBT[2])))
                        .setDefaultColor(EnumChatFormatting.WHITE)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(5, 20))
                .widget(
                    new VanillaButtonWidget().setDisplayString("Lock")
                        .setOnClick((clickData, widget) -> {
                            if (!widget.isClient()) {
                                setLock(itemStack, NBT[0]);
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
