package helsinki.assets;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.dao.IEntityDao;

/**
 * Companion object for entity {@link AssetFinDet}.
 *
 * @author Developers
 *
 */
public interface AssetFinDetCo extends IEntityDao<AssetFinDet> {

    static final IFetchProvider<AssetFinDet> FETCH_PROVIDER = EntityUtils.fetch(AssetFinDet.class).with(
        "key", "commissionDate", "disposalDate", "initCost");

}
