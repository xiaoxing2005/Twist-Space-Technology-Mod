package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.Nxer.TwistSpaceTechnology.common.block.BasicBlocks.MetaBlockCasing01;
import static com.Nxer.TwistSpaceTechnology.util.TextLocalization.Text_SeparatingLine;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

public class TST_FallingSunNo7 extends GTCM_MultiMachineBase<TST_FallingSunNo7> {

    // region Class Constructor

    public TST_FallingSunNo7(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public TST_FallingSunNo7(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TST_FallingSunNo7(mName);
    }

    // end region

    // region default value

    private int Tier = 0;
    public byte glassTier = 0;
    // end region

    // region Processing Logic

    @Override
    public RecipeMap<?> getRecipeMap(){
        return super.getRecipeMap();
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

    // end region

    // region Structure

    private static final String STRUCTURE_PIECE_T1 = "T1";
    private static final String STRUCTURE_PIECE_T2 = "T2";
    private static final String STRUCTURE_PIECE_T3 = "T3";
    private static final String STRUCTURE_PIECE_T4 = "T4";
    private static IStructureDefinition<TST_FallingSunNo7> STRUCTURE_DEFINITION = null;
    private static final String[][] shapeT1 = readStructure("T1");
    private static final String[][] shapeT2 = readStructure("T2");
    private static final String[][] shapeT3 = readStructure("T3");
    private static final String[][] shapeT4 = readStructure("T4");
    private static final String PATH = "src/main/resources/assets/gtnhcommunitymod/structure/";

    private static String[][] readStructure(String FileName){
        ArrayList<String[]> tempList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader( PATH + FileName + ".txt"))) {
            String line;

            // 逐行读取文件内容
            while ((line = br.readLine()) != null) {
                // 去除大括号和双引号，并按逗号分割
                line = line.trim().replaceAll("[{}\"]", "");
                String[] values = line.split(",");
                tempList.add(values);
            }
        } catch (IOException e) {
            TwistSpaceTechnology.LOG.info("Structure File : " + FileName + "Unable to read");
        }

        // 将 ArrayList 转换为二维数组
        String[][] array = new String[tempList.size()][];
        for (int i = 0; i < tempList.size(); i++) {
            array[i] = tempList.get(i);
        }

        return array;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        switch (stackSize.stackSize){
            case 1->{this.buildPiece(STRUCTURE_PIECE_T1, stackSize, hintsOnly, 20, 7, 30);}
            case 2->{this.buildPiece(STRUCTURE_PIECE_T2, stackSize, hintsOnly, 20, 70, 30);}
            case 3->{this.buildPiece(STRUCTURE_PIECE_T3, stackSize, hintsOnly, 20, 70, 55);}
            case 4->{this.buildPiece(STRUCTURE_PIECE_T4, stackSize, hintsOnly, 20, 70, 55);}
            default -> {if (stackSize.stackSize > 4) {
                this.buildPiece(STRUCTURE_PIECE_T4, stackSize, hintsOnly, 20, 70, 55);
            }
            }
        }
    }


    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        switch (stackSize.stackSize){
            case 1->{return survivialBuildPiece(STRUCTURE_PIECE_T1, stackSize, 20, 7, 30, elementBudget, env, false, true);}
            case 2->{return survivialBuildPiece(STRUCTURE_PIECE_T2, stackSize, 20, 70, 30, elementBudget, env, false, true);}
            case 3->{return survivialBuildPiece(STRUCTURE_PIECE_T3, stackSize, 20, 70, 55, elementBudget, env, false, true);}
            case 4->{return survivialBuildPiece(STRUCTURE_PIECE_T4, stackSize, 20, 70, 55, elementBudget, env, false, true);}
            default -> {if (stackSize.stackSize > 4) return survivialBuildPiece(STRUCTURE_PIECE_T4, stackSize, 20, 70, 55, elementBudget, env, false, true);}
        }
        return -1;
    }

    @Override
    public IStructureDefinition<TST_FallingSunNo7> getStructureDefinition() {
        return null;
    }

    // end region

    // spotless:off

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        // #tr Tooltip_FallingSunNo7_MachineType
        // # Elemental Collector / Deep-hot ground pump / Geothermal boilers
        // #zh_CN 元素采集者 | 深热地泵 | 地热锅炉
        tt.addMachineType(TextEnums.tr("Tooltip_BallLightning_MachineType"))
                // #tr Tooltip_BFallingSunNo7_Controller
                // # Controller block for the Falling Sun No7
                // #zh_CN 落日七号的的控制方块
                .addInfo(TextEnums.tr("Tooltip_BallLightning_Controller")) // #tr Tooltip_FallingSunNo7_0
                // # {\ITALIC} " I saw the terracraft, "
                // #zh_CN {\ITALIC} " 在我下面六千多公里深处, "
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_0"))
                // #tr Tooltip_FallingSunNo7_1
                // # {\ITALIC} " anchored more than six thousand kilometers below me at the center of that translucent sphere,"
                // #zh_CN {\ITALIC} " 在这巨大的水晶球中心, "
                .addInfo(TextEnums.tr(  "Tooltip_FallingSunNo7_1"))
                // #tr Tooltip_FallingSunNo7_2
                // # {\ITALIC} " whose hull once bore the name Sunset 6;"
                // #zh_CN {\ITALIC} " 我看到了停汨在那里的"落日六号"地航飞船, "
                .addInfo(TextEnums.tr(  "Tooltip_FallingSunNo7_2"))
                // #tr Tooltip_FallingSunNo7_3
                // # {\ITALIC} " I felt her heartbeat echo up to me through thousands of kilometers."
                // #zh_CN {\ITALIC} " 感受到了从几千公里深的地球中心传出的她的心跳. "
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_3"))
                // #tr Tooltip_FallingSunNo7_4
                // # {\SPACE}
                // #zh_CN {\SPACE}
                .addInfo(TextEnums.tr("Tooltip_FallingSunNo7_4"))
                // #tr Tooltip_FallingSunNo7_5
                // # {\ITALIC}{\WHITE}Venturing to the geographical extremes, exploring the boundaries of celestial bodies!
                // #zh_CN {\ITALIC}{\WHITE}发挥地理的极限, 探索星球的边境!
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_5"))
                // #tr Tooltip_FallingSunNo7_6
                // # The structure requires at least LuV glass and Naquadah Coil.
                // #zh_CN 至少需要LuV玻璃等级和硅岩线圈才可成型
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_6"))
                // #tr Tooltip_FallingSunNo7_7
                // # Comprises three machine levels, each unlocking a different upgrade.
                // #zh_CN 机器拥有3个等级, 依次不同升级,
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_7"))
                // #tr Tooltip_FallingSunNo7_8
                // # With each machine tier upgrade, the lower-tier modes benefit from a 4x speed multiplier.
                // #zh_CN 每升级一次机器等级, 更低级的机器模式获得4倍速.
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_8"))
                // #tr Tooltip_FallingSunNo7_9
                // # It is mandatory to require ME output buses and 64A Energy Warehouse.
                // #zh_CN 强制要求me输出总线和仓室, 能源仓最低电流64A.
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_9"))
                // #tr Tooltip_FallingSunNo7_10
                // # {\GOLD}=== Machine Tier ===
                // #zh_CN {\GOLD}=== 机器等级 ===
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_10"))
                // #tr Tooltip_FallingSunNo7_11
                // # The base structure is Tier 1
                // #zh_CN 基础结构为等级1
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_11"))
                // #tr Tooltip_FallingSunNo7_12
                // # Utilizing Tier 3 structure unlocks level 2
                // #zh_CN 安装附属结构以后解锁等级2
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_12"))
                // #tr Tooltip_FallingSunNo7_13
                // # Tier 3 structure require at least UV glass, Dranconic coil, and UV component assembly line casing.
                // #zh_CN 附属结构至少需要UV玻璃等级、觉醒龙线圈、UV部件装配线外壳.
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_13"))
                // #tr Tooltip_FallingSunNo7_14
                // # 111111 in the control slot and utilizing Tier 4 structure unlocks level 3
                // #zh_CN 在主机内放入星河蓄能稳定立场且使用4级结构解锁等级3
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_14"))
                // #tr Tooltip_FallingSunNo7_15
                // # {\SPACE}
                // #zh_CN 玻璃等级、部件装配线外壳、线圈锁定了最高可执行的电压, 升级机器总体框架及方块以减少配方时间.
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_15"))
                // #tr Tooltip_FallingSunNo7_16
                // # {\SPACE}
                // #zh_CN 耗时系数 = 配方时间 - 60 * ( 可升级方块最低等级 - 3 ) - 5 ^ 结构框架等级
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_16"))
                // #tr Tooltip_FallingSunNo7_17
                // # {\SPACE}
                // #zh_CN {\GOLD}=== 结构等级 ===
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_17"))
                // #tr Tooltip_FallingSunNo7_18
                // # {\SPACE}
                // #zh_CN 结构共分为4级, 具体可查看结构预览
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_18"))
                // #tr Tooltip_FallingSunNo7_19
                // # {\SPACE}
                // #zh_CN 等级越高, 稀有材料产出概率越高
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_19"))
                // #tr Tooltip_FallingSunNo7_20
                // # {\SPACE}
                // #zh_CN 不影响机器可执行的最高电压配方
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_20"))
                // #tr Tooltip_FallingSunNo7_21
                // # {\SPACE}
                // #zh_CN 在控制器内调整参数以控制结构等级
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_21"))
                // #tr Tooltip_FallingSunNo7_22
                // # {\SPACE}
                // #zh_CN {\GOLD}=== 机器模式 ===
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_22"))
                // #tr Tooltip_FallingSunNo7_23
                // # {\SPACE}
                // #zh_CN {\YELLOW} 深地热泵 | 地热锅炉
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_23"))
                // #tr Tooltip_FallingSunNo7_24
                // # {\SPACE}
                // #zh_CN 并行数 = 2 ^ ( 玻璃或机械方块最低等级 - 4 )
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_24"))
                // #tr Tooltip_FallingSunNo7_25
                // # {\SPACE}
                // #zh_CN 仅支持无损超频一次, 可有损超频
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_25"))
                // #tr Tooltip_FallingSunNo7_26
                // # {\SPACE}
                // #zh_CN {\YELLOW} 元素采集者
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_26"))
                // #tr Tooltip_FallingSunNo7_27
                // # {\SPACE}
                // #zh_CN 固定时间, 无法超频, 可使用电路板改变聚焦目标
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_27"))
                // #tr Tooltip_FallingSunNo7_28
                // # {\SPACE}
                // #zh_CN 结构等级3及以上解锁熔融流体产出
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_28"))
                // #tr Tooltip_FallingSunNo7_29
                // # {\SPACE}
                // #zh_CN 允许输入量子反常并使用算力和UU物质聚焦某一产物提升概率,将会延长一定时间
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_29"))
                // #tr Tooltip_FallingSunNo7_30
                // # {\SPACE}
                // #zh_CN UMW以上配方需要引力透镜进行聚焦
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_30"))
                // #tr Tooltip_FallingSunNo7_31
                // # {\SPACE}
                // #zh_CN 锁定DD维度配方将会按照权重随机产出合金并增加配方时间
                .addInfo(TextEnums.tr("Tooltip_FallingSunNo7_31"))
                // #tr Tooltip_FallingSunNo7_32
                // # {\SPACE}
                // #zh_CN 输入粗制压缩时空场发生器可以产出等离子并大幅加长配方时间
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_32"))
                // #tr Tooltip_FallingSunNo7_33
                // # {\SPACE}
                // #zh_CN 部分材料默认无法产出,可通过聚焦提升概率
                .addInfo(TextEnums.tr("Tooltip_FallingSunNo7_33"))
                // #tr Tooltip_FallingSunNo7_34
                // # {\SPACE}
                // #zh_CN 开机时无法关闭导能模式,运行时结构不成型将会立即爆炸并产生大量熔岩
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_34"))
                .addInfo(Text_SeparatingLine)
                // #tr Tooltip_FallingSunNo7_34
                // # {\SPACE}
                // #zh_CN 当结构等级为2时解锁激光仓
                .addInfo(TextEnums.tr("Tooltip_FallingSunNo7_34"))
                // #tr Tooltip_FallingSunNo7_35
                // # {\SPACE}
                // #zh_CN 当结构等级为3且未安装能源仓时进入无线模式, 未安装数据接口时直接调取无线算力
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_35"))
                // #tr Tooltip_FallingSunNo7_36
                // # {\SPACE}
                // #zh_CN 支持多仓
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_36"))
                // #tr Tooltip_FallingSunNo7_37
                // # {\SPACE}
                // #zh_CN {\BLACK}输入英雄之证可能会发生奇妙的反应?
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_37"))
                // #tr Tooltip_FallingSunNo7_38
                // # {\SPACE}
                // #zh_CN {\GOLD}{\ITALIC}特别感谢: 年轮城计划
                .addInfo(TextEnums.tr( "Tooltip_FallingSunNo7_38"));
        return tt;
    }

    // spotless:on

    //            case 1->{return survivialBuildPiece(STRUCTURE_PIECE_T1, stackSize, 20, 7, 30, elementBudget, env, false, true);}
    //            case 2->{return survivialBuildPiece(STRUCTURE_PIECE_T2, stackSize, 20, 70, 30, elementBudget, env, false, true);}
    //            case 3->{return survivialBuildPiece(STRUCTURE_PIECE_T3, stackSize, 20, 70, 55, elementBudget, env, false, true);}
    //            case 4->{return survivialBuildPiece(STRUCTURE_PIECE_T4, stackSize, 20, 70, 55, elementBudget, env, false, true);}
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        this.Tier = 0;
        this.glassTier = 0;

        if (checkPiece(STRUCTURE_PIECE_T1, 20, 7, 30)){
            Tier++;
        }
        if (Tier == 1 && checkPiece(STRUCTURE_PIECE_T2, 20, 70, 30)){
            Tier++;
        }
        if (Tier == 2 && checkPiece(STRUCTURE_PIECE_T3, 20, 70, 55)){
            Tier++;
        }
        if (Tier == 3 && checkPiece(STRUCTURE_PIECE_T4, 20, 70, 55)){
            Tier++;
        }
        return Tier != 0;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture base = casingTexturePages[115][MetaBlockCasing01.getTextureIndexInPage(1)];
        if (side == facing) {
            if (active) return new ITexture[] { base, TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FUSION1_GLOW)
                            .extFacing()
                            .glow()
                            .build() };
            return new ITexture[] { base, TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_OFF)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { base };
    }

}
