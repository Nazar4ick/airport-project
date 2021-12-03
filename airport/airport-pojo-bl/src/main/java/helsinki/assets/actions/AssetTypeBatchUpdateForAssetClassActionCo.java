package helsinki.assets.actions;

import helsinki.assets.AssetType;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.utils.EntityUtils;

/**
 * Companion object for entity {@link AssetTypeBatchUpdateForAssetClassAction}.
 *
 * @author Developers
 *
 */
public interface AssetTypeBatchUpdateForAssetClassActionCo extends IEntityDao<AssetTypeBatchUpdateForAssetClassAction> {
    static final IFetchProvider<AssetTypeBatchUpdateForAssetClassAction> FETCH_PROVIDER = EntityUtils.fetch(AssetTypeBatchUpdateForAssetClassAction.class).with("assetClass");
}
