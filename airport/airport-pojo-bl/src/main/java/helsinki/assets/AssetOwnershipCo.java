package helsinki.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetOwnership}.
 *
 * @author Developers
 *
 */
public interface AssetOwnershipCo extends IEntityDao<AssetOwnership> {
    
    static final String ERR_REQUIRED = "One of the pwnership properties organisation or business unit must be entered.";

    static final IFetchProvider<AssetOwnership> FETCH_PROVIDER = EntityUtils.fetch(AssetOwnership.class).with(
           "asset", "startDate", "desc", "role", "organisation", "bu");

}
