package com.Nxer.TwistSpaceTechnology.common.machine;

import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.ModName;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.StructureTooComplex;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.kentington.thaumichorizons.common.ThaumicHorizons.blockRecombinator;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static tb.init.TBBlocks.dustBlock;
import static thaumcraft.common.config.ConfigBlocks.*;
import static tuhljin.automagy.blocks.ModBlocks.translucent;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics.GTCM_ProcessingLogic;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileNodeEnergized;

public class GT_TieEntily_LargeArcaneMatterTransmutationTower extends
    GTCM_MultiMachineBase<GT_TieEntily_LargeArcaneMatterTransmutationTower> implements ISidedInventory, IConstructable {

    // region Class Constructor

    public GT_TieEntily_LargeArcaneMatterTransmutationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TieEntily_LargeArcaneMatterTransmutationTower(String aName) {
        super(aName);
    }

    // end region

    // region Processing Logic

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new GTCM_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                // setOverclock(isEnablePerfectOverclock() ? 2 : 1, isEnablePerfectOverclock() ? 2 : 3);
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 1;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTCMRecipe.LargeArcaneMatterTransmutationTowerRecipe;
    }

    // region end

    // region Structure

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final int horizontalOffSet = 22;
    private final int verticalOffSet = 35;
    private final int depthOffSet = 20;
    public TileNodeEnergized NodeEnergized;
    public TileCrucible Crucible;
    // spotless:off

    private static final String[][] shape = new String[][]{
            {"       ","       ","  DJD  ","  JNJ  ","  DJD  ","       ","       "},
            {"       "," JDJDJ "," D   D "," J   J "," D   D "," JDJDJ ","       "},
            {"JDDDDDJ","DD   DD","D     D","J     J","D     D","DD   DD","JDDJDDJ"},
            {"       "," FEEEF "," E   E "," E   E "," E   E "," FEEEF ","       "},
            {"       "," FNNNF "," N   N "," N   N "," N   N "," FNNNF ","       "},
            {"       "," FNNNF "," N   N "," N S N "," N   N "," FNNNF ","       "},
            {"       "," FNNNF "," NCCCN "," NCCCN "," NCCCN "," FNNNF ","       "},
            {"  EKE  "," KCCCK ","ECCCCCE","KCCHCCK","ECCCCCE"," KCCCK ","  EKE  "},
            {"  NNN  "," N   N ","N III N","N IRI N","N III N"," N   N ","  NNN  "},
            {"  NNN  "," N   N ","N     N","N  L   ","N     N"," N   N ","  NNN  "},
            {"  NNN  "," N   N ","N III N","N IQI  ","N III N"," N   N ","  NNN  "},
            {"  GGG  "," GBJBG ","GBABJBG","GJBKBJG","GBABABG"," GBJBG ","  GGG  "},
            {"       "," BNNNB "," N   N "," N O N "," N   N "," BNNNB ","       "},
            {"       "," BNNNB "," N   N "," N M N "," N   N "," BNNNB ","       "},
            {"       "," BNNNB "," NIIIN "," NITIN "," NIIIN "," BNNNB ","       "},
            {"       "," FF~FF "," FGGGF "," FGGGF "," FGGGF "," FFFFF ","       "}
    };

    // spotless:on

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
    public IStructureDefinition<GT_TieEntily_LargeArcaneMatterTransmutationTower> getStructureDefinition() {
        return StructureDefinition.<GT_TieEntily_LargeArcaneMatterTransmutationTower>builder()
            .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
            .addElement('A', ofBlock(blockCosmeticOpaque, 1))
            .addElement('B', ofBlock(blockCosmeticSolid, 1))
            .addElement('C', ofBlock(blockCosmeticSolid, 4))
            .addElement('D', ofBlock(blockCosmeticSolid, 6))
            .addElement('E', ofBlock(blockCosmeticSolid, 7))
            .addElement('F', ofBlock(blockCosmeticSolid, 9))
            .addElement('G', ofBlock(blockMetalDevice, 3))
            .addElement('H', ofBlock(Blocks.redstone_block, 0))
            .addElement('I', ofBlock(blockStairsArcaneStone, 0))
            .addElement('J', ofBlock(translucent, 1))
            .addElement('K', ofBlock(dustBlock, 0))
            .addElement(
                'L',
                ofChain(
                    ofTileAdder(GT_TieEntily_LargeArcaneMatterTransmutationTower::addNodeEnergized, Blocks.air, 0),
                    ofBlock(Blocks.air, 0)))
            .addElement(
                'M',
                ofChain(
                    ofTileAdder(GT_TieEntily_LargeArcaneMatterTransmutationTower::addCrucible, blockMetalDevice, 0),
                    ofBlock(blockMetalDevice, 0)))
            .addElement('N', ofBlock(blockCosmeticOpaque, 2))
            .addElement('O', ofBlock(blockRecombinator, 0))
            .addElement('Q', ofBlock(blockStoneDevice, 10))
            .addElement('R', ofBlock(blockStoneDevice, 11))
            .addElement('S', ofBlock(Blocks.beacon, 0))
            .addElement('T', ofBlock(blockAiry, 1))
            .build();
    }

    public final boolean addCrucible(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileCrucible) {
            if (Crucible == null) {
                this.Crucible = (TileCrucible) aTileEntity;
                return true;
            }
        }
        return false;
    }

    public final boolean addNodeEnergized(TileEntity aTileEntity) {
        if (aTileEntity instanceof TileNodeEnergized) {
            if (NodeEnergized == null) {
                this.NodeEnergized = (TileNodeEnergized) aTileEntity;
                return true;
            }
        }
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(
            // #tr Tooltip_IndustrialMagicMatrix_MachineType
            // # Controller block for the Industrial Magic Matrix
            // #zh_CN §0工业注魔矩阵
            TextEnums.tr("Tooltip_IndustrialMagicMatrix_MachineType"))
            // #tr Tooltip_IndustrialMagicMatrix_Controller
            // # Magic Matrix
            // #zh_CN 工业注魔矩阵的控制器方块
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_Controller"))
            // #tr Tooltip_IndustrialMagicMatrix_00
            // # Please use the Infusion Supplier to supply Essence!
            // #zh_CN 请使用注魔供应器供给源质！
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_00"))
            // #tr Tooltip_IndustrialMagicMatrix_01
            // # When you stare into the void, the void stares at you.
            // #zh_CN §c§n当你凝视虚空时，虚空也在凝视你。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_01"))
            // #tr Tooltip_IndustrialMagicMatrix_02
            // # Some say it's a miracle of a mystical envoy,
            // #zh_CN 有人说这是神秘使的神迹，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_02"))
            // #tr Tooltip_IndustrialMagicMatrix_03
            // # Others say that it is a cult creature of a sorcerer.
            // #zh_CN 也有人说这是邪术使的§c§0邪祟造物。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_03"))
            // #tr Tooltip_IndustrialMagicMatrix_04
            // # But who cares?!
            // #zh_CN 不过谁在意呢？！
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_04"))
            // #tr Tooltip_IndustrialMagicMatrix_05
            // # Needless to say, its incredible principle is fascinating...
            // #zh_CN §k毋庸置疑的是它那不可思议的原理令人十分入迷...
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_05"))
            // #tr Tooltip_IndustrialMagicMatrix_06
            // # Because of the pollution of technology,
            // #zh_CN 由于被科技所污染，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_06"))
            // #tr Tooltip_IndustrialMagicMatrix_07
            // # It is unable to perform active infusions.
            // #zh_CN 它无法进行具有活性的注魔。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_07"))
            // #tr Tooltip_IndustrialMagicMatrix_08
            // # Parallelism depends on the level of the structure block.
            // #zh_CN 并行取决于结构方块的等级。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_08"))
            // #tr Tooltip_IndustrialMagicMatrix_09
            // # Do an 4/2 overclock.Turn on lossless overclocking after reaching the maximum acceleration rate.
            // #zh_CN 进行4/2超频。达到最高加速倍率后开启无损超频.
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_09"))
            // #tr Tooltip_IndustrialMagicMatrix_10
            // # Use Charged Nodes to get acceleration rewards,
            // #zh_CN §b使用充能节点以获得加速奖励§7，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_10"))
            // #tr Tooltip_IndustrialMagicMatrix_11
            // # However, when the number of nodes is less than six,
            // #zh_CN 但节点数量不足六个时,
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_11"))
            // #tr Tooltip_IndustrialMagicMatrix_12
            // # the processing time will be carried out (recipe time * number of missing nodes * 1.75).
            // #zh_CN 将会额外计算(配方时间*节点缺失数量*1.75)的加工时长，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_12"))
            // #tr Tooltip_IndustrialMagicMatrix_13
            // # The matrix will take the largest element of each of the six nodes and calculate the average.
            // #zh_CN 矩阵将会取这六个节点中每个最大的要素并计算平均值，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_13"))
            // #tr Tooltip_IndustrialMagicMatrix_14
            // # The closer the quantities between the six primitive elements are, the higher the multiplication factor
            // will be.
            // #zh_CN 六种原始要素之间的数量越接近倍率系数就会越高。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_14"))
            // #tr Tooltip_IndustrialMagicMatrix_15
            // # The actual acceleration magnification is ((0.4+0.45exp(-0.05Variance)+
            // #zh_CN 实际加速倍率为{\SPACE}{\AQUA}((0.4+0.45exp(-0.05Variance) +
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_15"))
            // #tr Tooltip_IndustrialMagicMatrix_16
            // # 0.15(ln(1+exp(-Variance)/ln2)) * (Mean / 500).
            // #zh_CN {\SPACE}{\SPACE}{\AQUA}0.15(ln(1+exp(-Variance)/ln2)) * (Mean / 500)
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_16"))
            // #tr Tooltip_IndustrialMagicMatrix_17
            // # Variance is the variance of the largest element in the six nodes,
            // #zh_CN Variance为六个节点里最大要素的方差，
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_17"))
            // #tr Tooltip_IndustrialMagicMatrix_18
            // # Mean is the average.
            // #zh_CN Mean则为平均数。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_18"))
            // #tr Tooltip_IndustrialMagicMatrix_19
            // # When the type of the six elements is not the six original elements,
            // #zh_CN 当六个要素的种类不为六种原始要素时,
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_19"))
            // #tr Tooltip_IndustrialMagicMatrix_20
            // # Each missing one adds a fixed 1 second to the time.
            // #zh_CN 每缺少一种就固定增加1秒耗时。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_20"))
            // #tr Tooltip_IndustrialMagicMatrix_21
            // # Gain up to 1145.14%% acceleration multiplier.
            // #zh_CN 最高获得 1145.14%% 的加速倍数。
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_21"))
            // #tr Tooltip_IndustrialMagicMatrix_22
            // # Putting EssentiaCell_Creative in the controller GUI doesn't cost essentia, but if it's a hero's
            // proof,maybe a little bit of an incredible change...
            // #zh_CN 在控制器GUI放入魔导源质元件则无需消耗源质，但如果是某位英雄的证明或许会发生一点不可思议的变化...
            .addInfo(TextEnums.tr("Tooltip_IndustrialMagicMatrix_22"))
            .addSeparator()
            .addInfo(StructureTooComplex)
            .addInfo(BLUE_PRINT_INFO)
            // #tr Tooltip_IndustrialMagicMatrix_23
            // # Infusion Provider
            // #zh_CN 注魔供应器
            // #tr Tooltip_IndustrialMagicMatrix_23.1
            // # §bAny magic mechanical block
            // #zh_CN §b任意魔法机械方块
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialMagicMatrix_23"),
                TextEnums.tr("Tooltip_IndustrialMagicMatrix_23.1"))
            // #tr Tooltip_IndustrialMagicMatrix_24
            // # §bAny magic mechanical block
            // #zh_CN §b任意魔法机械方块
            .addInputBus(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
            .addOutputBus(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
            .addMaintenanceHatch(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
            .addEnergyHatch(TextEnums.tr("Tooltip_IndustrialMagicMatrix_24"))
            // #tr Tooltip_IndustrialMagicMatrix_25
            // # Essentia diffusion unit
            // #zh_CN 源质扩散单元
            // #tr Tooltip_IndustrialMagicMatrix_25.1
            // # Each level provides 4^tier parallel
            // #zh_CN §b每级提供4^tier的并行
            .addOtherStructurePart(
                TextEnums.tr("Tooltip_IndustrialMagicMatrix_25"),
                TextEnums.tr("Tooltip_IndustrialMagicMatrix_25.1"))
            .toolTipFinisher(ModName);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    // region end

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_TieEntily_LargeArcaneMatterTransmutationTower(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { TextureFactory.of(blockStoneDevice, 2), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { TextureFactory.of(blockStoneDevice, 2), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { TextureFactory.of(blockStoneDevice, 2) };
    }
}
