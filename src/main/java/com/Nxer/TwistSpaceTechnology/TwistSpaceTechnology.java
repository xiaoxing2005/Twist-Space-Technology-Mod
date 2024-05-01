package com.Nxer.TwistSpaceTechnology;

import static com.Nxer.TwistSpaceTechnology.loader.RecipeLoader.loadRecipesServerStarted;

import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Nxer.TwistSpaceTechnology.combat.items.ItemRegister;
import com.Nxer.TwistSpaceTechnology.common.Entity.EntityMountableBlock;
import com.Nxer.TwistSpaceTechnology.common.crop.CropLoader;
import com.Nxer.TwistSpaceTechnology.common.machine.singleBlock.hatch.GT_Hatch_RackComputationMonitor;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.devTools.PathHelper;
import com.Nxer.TwistSpaceTechnology.loader.MachineLoader;
import com.Nxer.TwistSpaceTechnology.loader.MaterialLoader;
import com.Nxer.TwistSpaceTechnology.loader.RecipeLoader;
import com.Nxer.TwistSpaceTechnology.nei.NEIHandler;
import com.Nxer.TwistSpaceTechnology.system.RecipePattern.ExtremeCraftRecipe;
import com.Nxer.TwistSpaceTechnology.util.TextHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import gregtech.api.util.GT_Recipe;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-before:IC2; " + "required-before:gregtech; "
        + "required-before:bartworks; "
        + "required-before:tectech; "
        + "before:miscutils; "
        + "before:dreamcraft;",
    acceptedMinecraftVersions = "[1.7.10]")
public class TwistSpaceTechnology {

    /**
     * <li>The signal of whether in Development Mode.
     * <li>Keep care to set 'false' when dev complete.
     */
    public static final boolean isInDevMode = false;

    /**
     * The absolute Path of your workspace/resources folder.
     * It will be replaced by {@link PathHelper#initResourceAbsolutePath}.
     * If it not work correctly, please operate it manually and disable
     * the{@link PathHelper#initResourceAbsolutePath}.
     */
    public static String DevResource = "";
    /**
     * <p>
     * Set false when auto generation get problems and set DevResource manually.
     * <p>
     * Mind to reset these changes when dev complete.
     */
    public static final boolean useAutoGeneratingDevResourcePath = true;

    public static final String MODID = Tags.MODID;
    public static final String MOD_ID = Tags.MODID;
    public static final String MOD_NAME = Tags.MODNAME;
    public static final String VERSION = Tags.VERSION;
    public static final String RESOURCE_ROOT_ID = "gtnhcommunitymod";

    /**
     * If you need send a message to the Log, call this.
     */
    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @Mod.Instance
    public static TwistSpaceTechnology instance;

    @SidedProxy(
        clientSide = "com.Nxer.TwistSpaceTechnology.ClientProxy",
        serverSide = "com.Nxer.TwistSpaceTechnology.CommonProxy")
    public static CommonProxy proxy;

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // process path
        if (isInDevMode && useAutoGeneratingDevResourcePath) {
            DevResource = PathHelper.initResourceAbsolutePath();
        }
        TextHandler.initLangMap(isInDevMode);

        proxy.preInit(event);
        MaterialLoader.load();// Load MaterialPool
        if (Config.activateCombatStats) {
            ItemRegister.registry();
        }
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MachineLoader.loadMachines();// Load Machines
        GT_Hatch_RackComputationMonitor.run();
        NEIHandler.IMCSender();// NEI reg
        EntityRegistry
            .registerModEntity(EntityMountableBlock.class, "TST:EntityMountableBlock", 1, this, 256, 20, false);
        // dimension provider
        // *unfinished */ DimensionManager.registerProviderType(WorldStats.dimensionProviderID,
        // WorldProviderAlfheim.class, false);
        // enter biomes into dictionary
        // *unfinished */ BiomeBaseAlfheim.registerWithBiomeDictionary();
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        MachineLoader.loadMachinePostInit();
        RecipeLoader.loadRecipesPostInit();// To init GTCM Recipemap

        TextHandler.serializeLangMap(isInDevMode);

        CropLoader.register();
        CropLoader.registerBaseSeed();
        // dimension provider
        // *unfinished */ DimensionManager.registerDimension(WorldStats.dimensionID, WorldStats.dimensionProviderID);
        // TwistSpaceTechnology.LOG.info("test GT.getResourcePath : " + GregTech.getResourcePath("testing"));
    }

    public static Collection<GT_Recipe> temp = new HashSet<>();

    @Mod.EventHandler
    public void completeInit(FMLLoadCompleteEvent event) {
        RecipeLoader.loadRecipes();// Load Recipes
        ExtremeCraftRecipe.initECRecipe();
        // reflect
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
        loadRecipesServerStarted();
    }

}
