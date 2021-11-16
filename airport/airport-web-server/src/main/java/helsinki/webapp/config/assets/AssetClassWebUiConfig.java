package helsinki.webapp.config.assets;

import static ua.com.fielden.platform.web.PrefDim.mkDim;
import static helsinki.common.StandardScrollingConfigs.standardEmbeddedScrollingConfig;
import static helsinki.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;
import static helsinki.common.LayoutComposer.CELL_LAYOUT;
import static helsinki.common.LayoutComposer.FLEXIBLE_LAYOUT_WITH_PADDING;
import static helsinki.common.LayoutComposer.FLEXIBLE_ROW;
import static helsinki.common.LayoutComposer.MARGIN;
import static helsinki.common.StandardActions.actionAddDesc;
import static helsinki.common.StandardActions.actionEditDesc;
import static helsinki.common.StandardActionsStyles.MASTER_CANCEL_ACTION_LONG_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_CANCEL_ACTION_SHORT_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_SAVE_ACTION_LONG_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_SAVE_ACTION_SHORT_DESC;
import static java.lang.String.format;
import static ua.com.fielden.platform.dao.AbstractOpenCompoundMasterDao.enhanceEmbededCentreQuery;
import static ua.com.fielden.platform.entity_centre.review.DynamicQueryBuilder.createConditionProperty;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.assets.AssetClass;
import helsinki.assets.AssetType;
import helsinki.main.menu.assets.MiAssetClass;
import helsinki.main.menu.assets.MiAssetClassMaster_AssetType;
import helsinki.assets.master.menu.actions.AssetClassMaster_OpenAssetType_MenuItem;
import helsinki.assets.ui_actions.OpenAssetClassMasterAction;
import helsinki.assets.ui_actions.producers.OpenAssetClassMasterActionProducer;
import helsinki.assets.master.menu.actions.AssetClassMaster_OpenMain_MenuItem;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.compound.Compound;
import ua.com.fielden.platform.web.view.master.api.compound.impl.CompoundMasterBuilder;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.PrefDim;
import ua.com.fielden.platform.web.PrefDim.Unit;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import static ua.com.fielden.platform.web.centre.api.context.impl.EntityCentreContextSelector.context;
import static ua.com.fielden.platform.web.layout.api.impl.LayoutBuilder.cell;

import ua.com.fielden.platform.web.centre.CentreContext;
import ua.com.fielden.platform.web.centre.IQueryEnhancer;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.ICompleted;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.IWhere0;
import ua.com.fielden.platform.web.view.master.EntityMaster;
/**
 * {@link AssetClass} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetClassWebUiConfig {

    private final Injector injector;

    public final EntityCentre<AssetClass> centre;
    public final EntityMaster<AssetClass> main;
    public final EntityMaster<OpenAssetClassMasterAction> compoundMaster;
    public final EntityActionConfig editAssetClassAction; // should be used on embedded centres instead of a standard EDIT action
    public final EntityActionConfig newAssetClassWithMasterAction; // should be used on embedded centres instead of a standrad NEW action
    private final EntityActionConfig newAssetClassAction;

    public static AssetClassWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetClassWebUiConfig(injector, builder);
    }

    private AssetClassWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        this.injector = injector;
        
        final PrefDim dims = mkDim(960, 640, Unit.PX);
        editAssetClassAction = Compound.openEdit(OpenAssetClassMasterAction.class, AssetClass.ENTITY_TITLE, actionEditDesc(AssetClass.ENTITY_TITLE), dims);
        newAssetClassWithMasterAction = Compound.openNewWithMaster(OpenAssetClassMasterAction.class, "add-circle-outline", AssetClass.ENTITY_TITLE, actionAddDesc(AssetClass.ENTITY_TITLE), dims);
        newAssetClassAction = Compound.openNew(OpenAssetClassMasterAction.class, "add-circle-outline", AssetClass.ENTITY_TITLE, actionAddDesc(AssetClass.ENTITY_TITLE), dims);
        builder.registerOpenMasterAction(AssetClass.class, editAssetClassAction);
        
        centre = createCentre(injector, builder, editAssetClassAction);
        builder.register(centre);

        main = createMain();
        builder.register(main);

        compoundMaster = CompoundMasterBuilder.<AssetClass, OpenAssetClassMasterAction>create(injector, builder)
            .forEntity(OpenAssetClassMasterAction.class)
            .withProducer(OpenAssetClassMasterActionProducer.class)
            .addMenuItem(AssetClassMaster_OpenMain_MenuItem.class)
                .icon("icons:picture-in-picture")
                .shortDesc(OpenAssetClassMasterAction.MAIN)
                .longDesc(AssetClass.ENTITY_TITLE + " main")
                .withView(main)
            .also()
            .addMenuItem(AssetClassMaster_OpenAssetType_MenuItem.class)
                .icon("icons:view-module")
                .shortDesc(OpenAssetClassMasterAction.ASSETTYPES)
                .longDesc(AssetClass.ENTITY_TITLE + " " + OpenAssetClassMasterAction.ASSETTYPES)
                .withView(createAssetTypeCentre())
            .done();
        builder.register(compoundMaster);
    }

    /**
     * Creates entity master for {@link AssetClass}.
     *
     * @return
     */
    private EntityMaster<AssetClass> createMain() {
        final String layout = cell(
                cell(cell(CELL_LAYOUT).repeat(2).withGapBetweenCells(MARGIN)).
                cell(cell(CELL_LAYOUT), FLEXIBLE_ROW), FLEXIBLE_LAYOUT_WITH_PADDING).toString();
                

        final IMaster<AssetClass> masterConfig = new SimpleMasterBuilder<AssetClass>().forEntity(AssetClass.class)
                .addProp("name").asSinglelineText().also()
                .addProp("active").asCheckbox().also()
                .addProp("desc").asMultilineText().also()
                .addAction(MasterActions.REFRESH).shortDesc(MASTER_CANCEL_ACTION_SHORT_DESC).longDesc(MASTER_CANCEL_ACTION_LONG_DESC)
                .addAction(MasterActions.SAVE).shortDesc(MASTER_SAVE_ACTION_SHORT_DESC).longDesc(MASTER_SAVE_ACTION_LONG_DESC)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 350, Unit.PX))
                .done();

        return new EntityMaster<>(AssetClass.class, masterConfig, injector);
    }

    private EntityCentre<AssetType> createAssetTypeCentre() {
        final Class<AssetType> root = AssetType.class;
        final String layout = LayoutComposer.mkVarGridForCentre(2, 1);

        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardNewAction = StandardActions.NEW_WITH_MASTER_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_EMBEDDED_CENTRE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();

        final EntityCentreConfig<AssetType> ecc = EntityCentreBuilder.centreFor(root)
                .runAutomatically()
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(AssetType.class).also()
                .addCrit("active").asMulti().bool().also()
                .addCrit("desc").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().width(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetType.ENTITY_TITLE)).also()
                .addProp("desc").minWidth(300).also()
                .addProp("active").minWidth(50)
                .addPrimaryAction(standardEditAction)
                .setQueryEnhancer(AssetClassMaster_AssetTypeCentre_QueryEnhancer.class, context().withMasterEntity().build())
                .build();

        return new EntityCentre<>(MiAssetClassMaster_AssetType.class, ecc, injector);
    }

    private static class AssetClassMaster_AssetTypeCentre_QueryEnhancer implements IQueryEnhancer<AssetType> {
        @Override
        public ICompleted<AssetType> enhanceQuery(final IWhere0<AssetType> where, final Optional<CentreContext<AssetType, ?>> context) {
            return enhanceEmbededCentreQuery(where, createConditionProperty("assetClass"), context.get().getMasterEntity().getKey());
        }
    }
    
    /**
     * Creates entity centre for {@link AssetClass}.
     *
     * @param injector
     * @param editAssetClassAction 
     * @return created entity centre
     */
    private EntityCentre<AssetClass> createCentre(final Injector injector, final IWebUiBuilder builder, final EntityActionConfig editAssetClassAction) {
        final String layout = LayoutComposer.mkVarGridForCentre(2, 1);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetClass.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();

        final EntityCentreConfig<AssetClass> ecc = EntityCentreBuilder.centreFor(AssetClass.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("this").asMulti().autocompleter(AssetClass.class).also()
                .addCrit("active").asMulti().bool().also()
                .addCrit("desc").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().width(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetClass.ENTITY_TITLE)).also()
                .addProp("desc").minWidth(300).also()
                .addProp("active").minWidth(300)
                .addPrimaryAction(editAssetClassAction)
                .build();

        return new EntityCentre<>(MiAssetClass.class, ecc, injector);
    }

}