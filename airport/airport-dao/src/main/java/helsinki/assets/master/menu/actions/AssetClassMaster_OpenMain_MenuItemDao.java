package helsinki.assets.master.menu.actions;

import com.google.inject.Inject;

import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import helsinki.security.tokens.compound_master_menu.AssetClassMaster_OpenMain_MenuItem_CanAccess_Token;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;

/**
 * DAO implementation for companion object {@link AssetClassMaster_OpenMain_MenuItemCo}.
 *
 * @author Developers
 *
 */
@EntityType(AssetClassMaster_OpenMain_MenuItem.class)
public class AssetClassMaster_OpenMain_MenuItemDao extends CommonEntityDao<AssetClassMaster_OpenMain_MenuItem> implements AssetClassMaster_OpenMain_MenuItemCo {

    @Inject
    public AssetClassMaster_OpenMain_MenuItemDao(final IFilter filter) {
        super(filter);
    }

    @Override
    @SessionRequired
    @Authorise(AssetClassMaster_OpenMain_MenuItem_CanAccess_Token.class)
    public AssetClassMaster_OpenMain_MenuItem save(AssetClassMaster_OpenMain_MenuItem entity) {
        return super.save(entity);
    }

}