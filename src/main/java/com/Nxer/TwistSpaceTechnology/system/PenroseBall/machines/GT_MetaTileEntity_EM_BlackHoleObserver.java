package com.Nxer.TwistSpaceTechnology.system.PenroseBall.machines;

import static com.Nxer.TwistSpaceTechnology.system.PenroseBall.items.ItemBlackHoleDataDepositor.addBlackHoleNBT;
import static com.github.technus.tectech.loader.TecTechConfig.POWERLESS_MODE;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_HatchElementBuilder;

public class GT_MetaTileEntity_EM_BlackHoleObserver extends GT_MetaTileEntity_MultiblockBase_EM
    implements ISurvivalConstructable {

    private long computationRemaining, computationRequired;
    private int Mass;
    private double AngularMomentum;
    private int distance;

    public GT_MetaTileEntity_EM_BlackHoleObserver(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_BlackHoleObserver(String aName) {
        super(aName);
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing_EM() {
        if (getControllerSlot() != null) {
            ItemStack controllerStack = getControllerSlot();
            if (controllerStack.stackTagCompound == null
                || controllerStack.stackTagCompound.getTagList("BlackHoleList", 10)
                    .tagCount() < 16) {
                if (controllerStack.isItemEqual(GTCMItemList.BlackHoleDataDepositor.get(1))) {
                    computationRemaining = computationRequired = 2000_000_000;
                    mMaxProgresstime = 200;
                    mEfficiencyIncrease = 10000;
                    eRequiredData = 123133112131231L;
                    lEUt = 0;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
            }
        }
        computationRequired = computationRemaining = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    private long getMaxInputData() {
        return 20000;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                if (getControllerSlot() == null) {
                    stopMachine();
                }
            }
        }
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (getControllerSlot() != null) {
            ItemStack itemStack = getControllerSlot();
            NBTTagCompound nbt = itemStack.stackTagCompound;
            if (nbt == null || nbt.getTagList("BlackHoleList", 10)
                .tagCount() < 16) {
                CalculatedData();
                addBlackHoleNBT(itemStack, this.distance, this.Mass, this.AngularMomentum);
            }
        }
    }

    private void CalculatedData() {
        Random rand = new Random();
        this.Mass = rand.nextInt(100 - 3 + 1) + 3;
        this.AngularMomentum = Math.random();
        this.distance = rand.nextInt(10000 - 3000 + 1) + 3000;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        computationRequired = computationRemaining = 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            return true;
        }
        if (getControllerSlot() != null && mProgresstime == 20) {
            computationRemaining -= Math.min(20000, eAvailableData);
            mProgresstime = 0;
            return super.onRunningTick(aStack);
        } else if (!isAllowedToWork()) stopMachine();
        return true;
    }

    @Override
    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        long euFlow = getPowerFlow() * eAmpereFlow; // quick scope sign
        if (allowProduction && euFlow > 0) {
            addEnergyOutput_EM(getPowerFlow() * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
        } else if (euFlow <= 0) {
            if (euFlow == 0) {
                return false;
            }
            if (POWERLESS_MODE) {
                return true;
            }
            if (!drainEnergyInput(
                getPowerFlow() * getMaxEfficiency(aStack) / Math.max(1000L, mEfficiency),
                eAmpereFlow)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 1;
    private final int verticalOffSet = 1;
    private final int depthOffSet = 0;

    // spotless:off

    private static final String[][] shape =
            new String[][]{
            {"AAA"," A ","   "},
            {"A~A","A A"," A "},
            {"AAA"," A ","   "}
    };

    // spotless:on

    private static IStructureDefinition<GT_MetaTileEntity_EM_BlackHoleObserver> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_EM_BlackHoleObserver> getStructure_EM() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_EM_BlackHoleObserver>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement(
                    'A',
                    ofChain(
                        GT_HatchElementBuilder.<GT_MetaTileEntity_EM_BlackHoleObserver>builder()
                            .adder(GT_MetaTileEntity_EM_BlackHoleObserver::addToMachineList)
                            .casingIndex(1536)
                            .dot(1)
                            .buildAndChain(Blocks.stone, 1),
                        ofBlock(Blocks.stone, 0)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (structureCheck_EM(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            fixAllIssues();
            return true;
        }
        return false;
    }

    private void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_BlackHoleObserver(mName);
    }
}
