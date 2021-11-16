package helsinki.assets.master.menu.actions;

import com.google.inject.Inject;

import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import helsinki.security.tokens.compound_master_menu.AssetClassMaster_OpenAssetType_MenuItem_CanAccess_Token;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;

/**
 * DAO implementation for companion object {@link AssetClassMaster_OpenAssetType_MenuItemCo}.
 *
 * @author Developers
 *
 */
@EntityType(AssetClassMaster_OpenAssetType_MenuItem.class)
public class AssetClassMaster_OpenAssetType_MenuItemDao extends CommonEntityDao<AssetClassMaster_OpenAssetType_MenuItem> implements AssetClassMaster_OpenAssetType_MenuItemCo {

    @Inject
    public AssetClassMaster_OpenAssetType_MenuItemDao(final IFilter filter) {
        super(filter);
    }

    @Override
    @SessionRequired
    @Authorise(AssetClassMaster_OpenAssetType_MenuItem_CanAccess_Token.class)
    public AssetClassMaster_OpenAssetType_MenuItem save(AssetClassMaster_OpenAssetType_MenuItem entity) {
        return super.save(entity);
    }

}