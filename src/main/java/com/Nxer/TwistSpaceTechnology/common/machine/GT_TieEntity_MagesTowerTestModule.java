package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GT_TieEntity_MagesTowerModuleBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.recipe.IGRecipeMaps;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.ModuleOverclockDescriber;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static gregtech.api.enums.GT_Values.V;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;

public abstract class GT_TieEntity_MagesTowerTestModule extends GT_TieEntity_MagesTowerModuleBase implements IOverclockDescriptionProvider {

    private static final INameFunction<GT_TieEntity_MagesTowerTestModule> PARALLEL_SETTING_NAME = (base, p) -> GCCoreUtil
            .translate("gt.blockmachines.multimachine.project.ig.assembler.cfgi.0"); // Parallels
    /** Status of the parallel setting */
    private static final IStatusFunction<GT_TieEntity_MagesTowerTestModule> PARALLEL_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 100, base.getMaxParallels());

    /** Power object used for displaying in NEI */
    protected final OverclockDescriber overclockDescriber;

    Parameters.Group.ParameterIn parallelSetting;

    public GT_TieEntity_MagesTowerTestModule(int aID, String aName, String aNameRegional, int tTier, int tModuleTier,
                                     int tMinMotorTier, int bufferSizeMultiplier) {
        super(aID, aName, aNameRegional, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
    }

    public GT_TieEntity_MagesTowerTestModule(String aName, int tTier, int tModuleTier, int tMinMotorTier,
                                     int bufferSizeMultiplier) {
        super(aName, tTier, tModuleTier, tMinMotorTier, bufferSizeMultiplier);
        overclockDescriber = new ModuleOverclockDescriber((byte) tTier, tModuleTier);
    }

    protected abstract int getMaxParallels();

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblerRecipes;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[tTier]);
        logic.setAvailableAmperage(getMaxParallels());
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic(){
            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(1);
                setOverclock(2, 2);
                // setOverclock(isEnablePerfectOverclock() ? 2 : 1, isEnablePerfectOverclock() ? 2 : 3);
                return super.process();
            }
        }.setAmperageOC(false).setMaxParallelSupplier(() -> Math.min(getMaxParallels(), (int) parallelSetting.get()));
    }

    @Override
    protected void parametersInstantiation_EM() {
        super.parametersInstantiation_EM();
        Parameters.Group hatch_0 = parametrization.getGroup(0, false);
        parallelSetting = hatch_0.makeInParameter(0, getMaxParallels(), PARALLEL_SETTING_NAME, PARALLEL_STATUS);
    }

    protected static Textures.BlockIcons.CustomIcon engraving;

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        } else if (facing.getRotation(ForgeDirection.UP) == side || facing.getRotation(ForgeDirection.DOWN) == side) {
            return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE),
                    new TT_RenderedExtendedFacingTexture(engraving) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TileEntitySpaceElevator.CASING_INDEX_BASE) };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        engraving = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_SIDE_ASSEMBLER_MODULE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public boolean protectsExcessItem() {
        return !eSafeVoid;
    }

    @Override
    public boolean protectsExcessFluid() {
        return !eSafeVoid;
    }

    @Nullable
    @Override
    public OverclockDescriber getOverclockDescriber() {
        return overclockDescriber;
    }

    public static class GT_TieEntity_MagesTowerTestModuleMK1 extends GT_TieEntity_MagesTowerTestModule {

        protected static final int MODULE_VOLTAGE_TIER = 9;

        protected static final int MODULE_TIER = 1;

        protected static final int MINIMUM_MOTOR_TIER = 1;

        protected static final int MAX_PARALLELS = 4;

        public GT_TieEntity_MagesTowerTestModuleMK1(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }


        public GT_TieEntity_MagesTowerTestModuleMK1(String aName) {
            super(aName, MODULE_VOLTAGE_TIER, MODULE_TIER, MINIMUM_MOTOR_TIER, MAX_PARALLELS);
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new GT_TieEntity_MagesTowerTestModuleMK1(mName);
        }

        protected int getMaxParallels() {
            return MAX_PARALLELS;
        }

        @Override
        protected GT_Multiblock_Tooltip_Builder createTooltip() {
            final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
            tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.module.name"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.desc0"))
                    .addInfo(
                            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                                    + GCCoreUtil
                                    .translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc1"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.desc2"))
                    .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.motorT1")).addSeparator()
                    .beginStructureBlock(1, 5, 2, false)
                    .addCasingInfoMin(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 0, false)
                    .addInputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputBus(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addInputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .addOutputHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                    .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
            return tt;
        }
    }
}
