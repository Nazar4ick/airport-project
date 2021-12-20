package helsinki.webapp.config.assets;

import static helsinki.common.StandardScrollingConfigs.standardStandaloneScrollingConfig;
import static java.lang.String.format;
import static ua.com.fielden.platform.web.PrefDim.mkDim;

import java.util.Optional;

import com.google.inject.Injector;

import helsinki.assets.Asset;
import helsinki.assets.AssetOwnership;
import helsinki.common.LayoutComposer;
import helsinki.common.StandardActions;
import helsinki.main.menu.assets.MiAssetOwnership;
import ua.com.fielden.platform.web.PrefDim.Unit;
import ua.com.fielden.platform.web.action.CentreConfigurationWebUiConfig.CentreConfigActions;
import ua.com.fielden.platform.web.app.config.IWebUiBuilder;
import ua.com.fielden.platform.web.centre.EntityCentre;
import ua.com.fielden.platform.web.centre.api.EntityCentreConfig;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.impl.EntityCentreBuilder;
import ua.com.fielden.platform.web.interfaces.ILayout.Device;
import ua.com.fielden.platform.web.view.master.EntityMaster;
import ua.com.fielden.platform.web.view.master.api.IMaster;
import ua.com.fielden.platform.web.view.master.api.actions.MasterActions;
import ua.com.fielden.platform.web.view.master.api.impl.SimpleMasterBuilder;

/**
 * {@link AssetOwnership} Web UI configuration.
 *
 * @author Developers
 *
 */
public class AssetOwnershipWebUiConfig {

    public final EntityCentre<AssetOwnership> centre;
    public final EntityMaster<AssetOwnership> master;

    public static AssetOwnershipWebUiConfig register(final Injector injector, final IWebUiBuilder builder) {
        return new AssetOwnershipWebUiConfig(injector, builder);
    }

    private AssetOwnershipWebUiConfig(final Injector injector, final IWebUiBuilder builder) {
        centre = createCentre(injector, builder);
        builder.register(centre);
        master = createMaster(injector);
        builder.register(master);
    }

    /**
     * Creates entity centre for {@link AssetOwnership}.
     *
     * @param injector
     * @return created entity centre
     */
    private EntityCentre<AssetOwnership> createCentre(final Injector injector, final IWebUiBuilder builder) {
        final String layout = LayoutComposer.mkVarGridForCentre(2, 1, 1, 1);

        final EntityActionConfig standardNewAction = StandardActions.NEW_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardDeleteAction = StandardActions.DELETE_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardExportAction = StandardActions.EXPORT_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardEditAction = StandardActions.EDIT_ACTION.mkAction(AssetOwnership.class);
        final EntityActionConfig standardSortAction = CentreConfigActions.CUSTOMISE_COLUMNS_ACTION.mkAction();
        builder.registerOpenMasterAction(AssetOwnership.class, standardEditAction);

        final EntityCentreConfig<AssetOwnership> ecc = EntityCentreBuilder.centreFor(AssetOwnership.class)
                .addFrontAction(standardNewAction)
                .addTopAction(standardNewAction).also()
                .addTopAction(standardDeleteAction).also()
                .addTopAction(standardSortAction).also()
                .addTopAction(standardExportAction)
                .addCrit("asset").asMulti().autocompleter(Asset.class).also()
                .addCrit("startDate").asRange().dateTime().also()
                .addCrit("role").asMulti().text().also()
                .addCrit("organisation").asMulti().text().also()
                .addCrit("bu").asMulti().text()
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withScrollingConfig(standardStandaloneScrollingConfig(0))
                .addProp("asset").order(1).asc().width(100)
                    .withSummary("total_count_", "COUNT(SELF)", format("Count:The total number of matching %ss.", AssetOwnership.ENTITY_TITLE)).also()
                .addProp("startDate").order(2).desc().width(170).also()
                .addProp("role").minWidth(100).also()
                .addProp("organisation").minWidth(100).also()
                .addProp("bu").minWidth(100)
                .addPrimaryAction(standardEditAction)
                .build();

        return new EntityCentre<>(MiAssetOwnership.class, ecc, injector);
    }

    /**
     * Creates entity master for {@link AssetOwnership}.
     *
     * @param injector
     * @return created entity master
     */
    private EntityMaster<AssetOwnership> createMaster(final Injector injector) {
        final String layout = LayoutComposer.mkVarGridForMasterFitWidth(2, 3);

        final IMaster<AssetOwnership> masterConfig = new SimpleMasterBuilder<AssetOwnership>().forEntity(AssetOwnership.class)
                .addProp("asset").asAutocompleter().also()
                .addProp("startDate").asDateTimePicker().also()
                .addProp("role").asSinglelineText().also()
                .addProp("organisation").asSinglelineText().also()
                .addProp("bu").asSinglelineText().also()
                .addAction(MasterActions.REFRESH).shortDesc("Cancel").longDesc("Cancel action")
                .addAction(MasterActions.SAVE)
                .setActionBarLayoutFor(Device.DESKTOP, Optional.empty(), LayoutComposer.mkActionLayoutForMaster())
                .setLayoutFor(Device.DESKTOP, Optional.empty(), layout)
                .setLayoutFor(Device.TABLET, Optional.empty(), layout)
                .setLayoutFor(Device.MOBILE, Optional.empty(), layout)
                .withDimensions(mkDim(LayoutComposer.SIMPLE_TWO_COLUMN_MASTER_DIM_WIDTH, 300, Unit.PX))
                .done();

        return new EntityMaster<>(AssetOwnership.class, masterConfig, injector);
    }
}