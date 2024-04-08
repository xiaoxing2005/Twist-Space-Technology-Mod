package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses;

import com.Nxer.TwistSpaceTechnology.common.machine.GT_TieEntity_MagesTower;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.GT_MetaTileEntity_EnhancedMultiBlockBase_EM;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import javax.annotation.Nonnull;

import static goodgenerator.loader.Loaders.magicCasing;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.Energy;

public class GT_TieEntity_MagesTowerModuleBase extends GT_MetaTileEntity_EnhancedMultiBlockBase_EM {

    protected static long EU_BUFFER_BASE_SIZE = 160008000L;
    protected final long euBufferSize;
    protected final int tTier;
    protected final int tModuleTier;
    protected final int tMinMotorTier;
    protected boolean isConnected = false;
    Parameters.Group.ParameterOut energyDisplay;
    private static final INameFunction<GT_TieEntity_MagesTowerModuleBase> ENERGY_DISPLAY_NAME = (base, p) -> GCCoreUtil
            .translate("gt.blockmachines.multimachine.project.ig.cfgo.0"); // Stored Energy
    private static final IStatusFunction<GT_TieEntity_MagesTowerModuleBase> ENERGY_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(
                    p.get(),
                    1,
                    (double) base.maxEUStore() / 2D,
                    (double) base.maxEUStore() * 2D,
                    (double) base.maxEUStore() * 2D);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_TieEntity_MagesTowerModuleBase> STRUCTURE_DEFINITION = StructureDefinition
            .<GT_TieEntity_MagesTowerModuleBase>builder()
            .addShape(
                    STRUCTURE_PIECE_MAIN,
                    StructureUtility.transpose(
                            new String[][]{
                                    {"AAA"},
                                    {"A~A"},
                                    {"AAA"}
                            }))
            .addElement(
                    'A',
                    GT_StructureUtility.ofHatchAdderOptional(
                            GT_TieEntity_MagesTowerModuleBase::addClassicToMachineList,
                            1536,
                            1,
                            magicCasing,
                            0)
                    ).build();

    protected GT_TieEntity_MagesTowerModuleBase(int aID, String aName, String aNameRegional, int tTier, int tModuleTier,
                                                int tMinMotorTier) {
        super(aID, aName, aNameRegional);
        this.tTier = tTier;
        this.tModuleTier = tModuleTier;
        this.tMinMotorTier = tMinMotorTier;
        euBufferSize = EU_BUFFER_BASE_SIZE * (1L << (tTier - 7));
        useLongPower = true;
    }

    protected GT_TieEntity_MagesTowerModuleBase( int aID, String aName, String aNameRegional,int tTier, int tModuleTier,
        int tMinMotorTier, int bufferSizeMultiplier){
            super(aID, aName, aNameRegional);
            this.tTier = tTier;
            this.tModuleTier = tModuleTier;
            this.tMinMotorTier = tMinMotorTier;
            euBufferSize = EU_BUFFER_BASE_SIZE * (1L << (tTier - 7)) * bufferSizeMultiplier;
            useLongPower = true;
        }
    protected GT_TieEntity_MagesTowerModuleBase(String aName, int tTier, int tModuleTier, int tMinMotorTier) {
        super(aName);
        this.tTier = tTier;
        this.tModuleTier = tModuleTier;
        this.tMinMotorTier = tMinMotorTier;
        euBufferSize = EU_BUFFER_BASE_SIZE * (1L << (tTier - 7));
        useLongPower = true;
    }
    protected GT_TieEntity_MagesTowerModuleBase(String aName, int tTier, int tModuleTier, int tMinMotorTier,
        int bufferSizeMultiplier) {
            super(aName);
            this.tTier = tTier;
            this.tModuleTier = tModuleTier;
            this.tMinMotorTier = tMinMotorTier;
            euBufferSize = EU_BUFFER_BASE_SIZE * (1L << (tTier - 7)) * bufferSizeMultiplier;
            useLongPower = true;
        }

    public static final CheckRecipeResult Notconnected = SimpleCheckRecipeResult
            .ofFailurePersistOnShutdown("Not connected");


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (aTick % 400 == 0) fixAllIssues();
            if (aTick % 20 == 0) energyDisplay.set(getEUVar());
            if (mEfficiency < 0) mEfficiency = 0;
            if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                stopMachine();
            }
        }super.onPostTick(aBaseMetaTileEntity,aTick);

    }


    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("isConnected", this.isConnected);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.isConnected = aNBT.getBoolean("isConnected");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EUuse = EUtEffective * Amperes;
        if (EUuse == 0L) {
            return true;
        } else {
            if (EUuse < 0L) {
                EUuse = -EUuse;
            }
            if (EUuse <= this.getEUVar()) {
                this.setEUVar(this.getEUVar() - EUuse);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public long maxEUStore() {
        return euBufferSize;
    }

    public long increaseStoredEU(long maximumIncrease) {
        if (getBaseMetaTileEntity() == null) {
            return 0;
        }
        connect();
        long increasedEU = Math
                .min(getBaseMetaTileEntity().getEUCapacity() - getBaseMetaTileEntity().getStoredEU(), maximumIncrease);
        return getBaseMetaTileEntity().increaseStoredEnergyUnits(increasedEU, false) ? increasedEU : 0;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    protected void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    public void connect() {
        isConnected = true;
        onMachineBlockUpdate();
    }

    public void disconnect() {
       this.isConnected = false;
       onMachineBlockUpdate();
    }

    @Override
    protected void chargeController_EM(IGregTechTileEntity aBaseMetaTileEntity) {

    }

    public int getTier() {
        return tTier;
    }

    public int getNeededMotorTier() {
        return tMinMotorTier;
    }

    @Override
    public long getMaxInputVoltage() {
        return gregtech.api.enums.GT_Values.V[tTier];
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fixAllIssues();
        if (structureCheck_EM(STRUCTURE_PIECE_MAIN, 1, 1, 0)){
            return isConnected;
        }return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE) };
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(IG_UITextures.PICTURE_ELEVATOR_LOGO_DARK).setSize(18, 18)
                        .setPos(173, 74));
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0, false);
        energyDisplay = hatch_0.makeOutParameter(0, 0, ENERGY_DISPLAY_NAME, ENERGY_STATUS);
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }
}
