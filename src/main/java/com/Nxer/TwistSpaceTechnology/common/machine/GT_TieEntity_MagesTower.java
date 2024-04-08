package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GT_TieEntity_MagesTowerModuleBase;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.GT_MetaTileEntity_EnhancedMultiBlockBase_EM;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.IGT_HatchAdder;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;

public class GT_TieEntity_MagesTower
        extends GT_MetaTileEntity_EnhancedMultiBlockBase_EM
        implements ISurvivalConstructable, IConstructable {

    private boolean isLoadedChunk;

    private static final int MODULE_CHARGE_INTERVAL = 20;

    protected int motorTier = 1;

    public int getMotorTier() {
        return motorTier;
    }

    @Override
    public void onRemoval() {
        if (mProjectModuleHatches != null && !mProjectModuleHatches.isEmpty()) {
            for (GT_TieEntity_MagesTowerModuleBase projectModule : mProjectModuleHatches) {
                projectModule.disconnect();
            }
        }
        super.onRemoval();
    }

    public GT_TieEntity_MagesTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_TieEntity_MagesTower(String aName) {
        super(aName);
    }

    public void setMotorTier(int tier) {
        motorTier = tier;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()){
            if (!aBaseMetaTileEntity.isAllowedToWork()) {
                // if machine has stopped, stop chunkloading
                GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
                isLoadedChunk = false;
        }else if (!isLoadedChunk) {
                // load a 3x3 area when machine is running
                GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
                int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
                int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        GT_ChunkManager.requestChunkLoad(
                                (TileEntity) aBaseMetaTileEntity,
                                new ChunkCoordIntPair(getChunkX() + offX + i, getChunkZ() + offZ + j));
                    }
                }this.isLoadedChunk = true;
            }
        }
        if (getBaseMetaTileEntity().isAllowedToWork()) {
            if (aTick % MODULE_CHARGE_INTERVAL == 0) {
                if (!mProjectModuleHatches.isEmpty()) {
                    long tEnergy = getEUVar() / mProjectModuleHatches.size() * MODULE_CHARGE_INTERVAL;
                    for (GT_TieEntity_MagesTowerModuleBase projectModule : mProjectModuleHatches) {
                        if (projectModule.getNeededMotorTier() <= motorTier) {
                            projectModule.connect();
                            long tAvailableEnergy = getEUVar();
                            if (tAvailableEnergy > 0) {
                                setEUVar(
                                        Math.max(
                                                0,
                                                tAvailableEnergy - projectModule
                                                        .increaseStoredEU(Math.min(tEnergy, tAvailableEnergy))));
                            }
                        }
                    }
                }
            }
        } else {
            if (!mProjectModuleHatches.isEmpty()) {
                for (GT_TieEntity_MagesTowerModuleBase projectModule : mProjectModuleHatches) {
                    projectModule.disconnect();
                }
            }
        }
        if (mEfficiency < 0) mEfficiency = 0;
        fixAllIssues();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        if (getBaseMetaTileEntity().isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }


    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_PIECE_MAIN_HOR_OFFSET = 6;
    private static final int STRUCTURE_PIECE_MAIN_VERT_OFFSET = 8;
    private static final int STRUCTURE_PIECE_MAIN_DEPTH_OFFSET = 6;
    public ArrayList<GT_TieEntity_MagesTowerModuleBase> mProjectModuleHatches = new ArrayList<>();
    /**
     * Gets structure
     *
     * @return STATIC INSTANCE OF STRUCTURE
     */
    @java.lang.Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return StructureDefinition.<GT_TieEntity_MagesTower>builder()
                .addShape(STRUCTURE_PIECE_MAIN, StructureUtility.transpose(new String[][] {
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              ","              "},
                        {"     ABA      ","              ","              ","              ","              ","A           A ","B           B ","A           A ","              ","              ","              ","              ","     ABA      ","              "},
                        {"     BCB      ","              ","              ","              ","              ","B           B ","C     ~     C ","B           B ","              ","              ","              ","              ","     BCB      ","              "},
                        {"     ABA      ","              ","              ","              ","              ","A     A     A ","B    AAA    B ","A     A     A ","              ","              ","              ","              ","     ABA      ","              "}
                }))
                .addElement('A',
                       StructureUtility.ofBlock(magicCasing,0))
                .addElement('C',
                        GT_HatchElementBuilder.<GT_TieEntity_MagesTower>builder()
                        .atLeast(ProjectModuleElement.ProjectModule)
                        .casingIndex(1536)
                        .dot(1)
                        .buildAndChain(magicCasing,0))
                .addElement('B',
                        GT_HatchElementBuilder.<GT_TieEntity_MagesTower>builder()
                                .atLeast(InputBus, OutputBus)
                                .adder(GT_TieEntity_MagesTower::addToMachineList)
                                .casingIndex(1536)
                                .dot(1)
                                .buildAndChain(magicCasing,0))
                .build();
    }

    public boolean addProjectModuleToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_TieEntity_MagesTowerModuleBase) {
            return mProjectModuleHatches.add((GT_TieEntity_MagesTowerModuleBase) aMetaTileEntity);
        }
        return false;
    }

    @java.lang.Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(
                STRUCTURE_PIECE_MAIN,
                STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
                stackSize,
                hintsOnly);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack){
        boolean isMachineValid = true;
        mProjectModuleHatches.clear();
        if (structureCheck_EM(
                STRUCTURE_PIECE_MAIN,
                STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                STRUCTURE_PIECE_MAIN_DEPTH_OFFSET)){
            fixAllIssues();
            return true;
        }
    return false;
    }

    protected void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) {
            return -1;
        } else {
            return survivialBuildPiece(
                    STRUCTURE_PIECE_MAIN,
                    stackSize,
                    STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                    STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                    STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
                    elementBudget,
                    env,
                    false,
                    true);
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc0"))
                .addInfo("123")
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc6"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc7"))
                .addInfo(GCCoreUtil.translate("ig.elevator.structure.TooComplex"))
                .addInfo(buildAddedBy(LIGHT_PURPLE + "minecraft7771"))
                .addInfo(GCCoreUtil.translate("ig.structure.moreContributors")).addSeparator()
                .beginStructureBlock(35, 43, 35, false)
                .addOtherStructurePart(
                        GCCoreUtil.translate("ig.elevator.structure.ProjectModule"),
                        GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith2Dot"),
                        2)
                .addCasingInfoExactly(GCCoreUtil.translate("tile.DysonSwarmFloor.name"), 800, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 593, 785, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.1.name"), 620, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.2.name"), 360, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.cable.name"), 1, false)
                .addCasingInfoExactly(GCCoreUtil.translate("ig.elevator.structure.FrameNeutronium"), 56, false)
                .addCasingInfoExactly(GCCoreUtil.translate("ig.elevator.structure.Motor"), 88, true)
                .addEnergyHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
        return tt;
    }
    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @java.lang.Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TieEntity_MagesTower(mName);
    }

    public enum ProjectModuleElement implements IHatchElement<GT_TieEntity_MagesTower>{
        ProjectModule(GT_TieEntity_MagesTower::addProjectModuleToMachineList, GT_TieEntity_MagesTowerModuleBase.class){
            @Override
            public long count(GT_TieEntity_MagesTower gtTieEntityMagesTower) {
                return 0;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_TieEntity_MagesTower> adder;

        @SafeVarargs
        ProjectModuleElement(IGT_HatchAdder<GT_TieEntity_MagesTower> adder,
                             Class<? extends IMetaTileEntity>... mteClasses){
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return this.mteClasses;
        }

        @Override
        public IGT_HatchAdder<? super GT_TieEntity_MagesTower> adder() {
            return this.adder;
        }

    }
}
