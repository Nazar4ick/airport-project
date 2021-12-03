package helsinki.webapp.config.assets;

import static ua.com.fielden.platform.web.centre.api.context.impl.EntityCentreContextSelector.context;
import static helsinki.common.LayoutComposer.CELL_LAYOUT;
import static helsinki.common.LayoutComposer.FLEXIBLE_LAYOUT_WITH_PADDING;
import static helsinki.common.LayoutComposer.FLEXIBLE_ROW;
import static helsinki.common.LayoutComposer.MARGIN;
import static helsinki.common.StandardActionsStyles.MASTER_CANCEL_ACTION_LONG_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_CANCEL_ACTION_SHORT_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_SAVE_ACTION_LONG_DESC;
import static helsinki.common.StandardActionsStyles.MASTER_SAVE_ACTION_SHORT_DESC;
import static helsinki.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;
import static java.lang.String.format;
import static ua.com.fielden.platform.web.PrefDim.mkDim;
import static ua.com.fielden.platform.web.centre.api.actions.impl.EntityActionBuilder.action;
import static ua.com.fielden.platform.web.layout.api.impl.LayoutBuilder.cell;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.assets.AssetClass;
import helsinki.assets.AssetType;
import helsinki.assets.actions.AssetTypeBatchUpdateForAssetClassAction;
import helsinki.assets.producers.AssetTypeProducer;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;
import helsinki.common.StandardActionsStyles;
import helsinki.main.menu.assets.MiAssetType;
import ua.com.fielden.platform.web.PrefDim.Unit;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.minijs.JsCode;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;

/**
 * {@link AssetType} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetTypeWebUiConfig {

    public final EntityCentre<AssetType> centre;
    public final EntityMaster<AssetType> master;

    public static AssetTypeWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetTypeWebUiConfig(injector, builder);
    }

    private AssetTypeWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetType}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetType> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkGridForCentre(2, 2);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetType.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetType.class, standardEditAction);
        
        final EntityActionConfig topActionToBatchUpdateAssetClasses = action(AssetTypeBatchUpdateForAssetClassAction.class)
                .withContext(context().withSelectedEntities().build())
                .postActionSuccess(() -> new JsCode("self.$.egi.clearPageSelection()"))
                .icon("icons:check-circle")
                .withStyle(StandardActionsStyles.CUSTOM_ACTION_COLOUR)
                .shortDesc("Batch update for Asset Types")
                .longDesc("Batch update property asset class for selected types")
                .build();

        final EntityCentreConfig<AssetType> ecc = EntityCentreBuilder.centreFor(AssetType.class)
                //.runAutomatically()
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction).also()
                .addTopAction(topActionToBatchUpdateAssetClasses)
                .addCrit("this").asMulti().autocompleter(AssetType.class).also()
                .addCrit("active").asMulti().bool().also()
                .addCrit("assetClass").asMulti().autocompleter(AssetClass.class).also()
                .addCrit("desc").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("this").order(1).asc().width(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetType.ENTITY_TITLE)).also()
                .addProp("desc").minWidth(300).also()
                .addProp("assetClass").minWidth(300).also()
                .addProp("active").minWidth(300)
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetType.class, ecc, injector);
    }

    /**
     * Creates entity master for {@link AssetType}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetType> createMaster(final Injector injector) {
        //final String layout = LayoutComposer.mkGridForMasterFitWidth(1, 2);
        final String layout = cell(
                cell(cell(CELL_LAYOUT).repeat(2).withGapBetweenCells(MARGIN)).
                cell(cell(CELL_LAYOUT)).
                cell(cell(CELL_LAYOUT), FLEXIBLE_ROW), FLEXIBLE_LAYOUT_WITH_PADDING).toString();

        final IMaster<AssetType> masterConfig = new SimpleMasterBuilder<AssetType>().forEntity(AssetType.class)
                .addProp("name").asSinglelineText().also()
                .addProp("active").asCheckbox().also()
                .addProp("assetClass").asAutocompleter().also()
                .addProp("desc").asMultilineText().also()
                .addAction(MasterActions.REFRESH).shortDesc(MASTER_CANCEL_ACTION_SHORT_DESC).longDesc(MASTER_CANCEL_ACTION_LONG_DESC)
                .addAction(MasterActions.SAVE).shortDesc(MASTER_SAVE_ACTION_SHORT_DESC).longDesc(MASTER_SAVE_ACTION_LONG_DESC)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH, 380, Unit.PX))
                .done();

        return new EntityMaster<>(AssetType.class, AssetTypeProducer.class, masterConfig, injector);
    }
}