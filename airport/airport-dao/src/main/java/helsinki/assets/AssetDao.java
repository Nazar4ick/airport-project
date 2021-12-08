package helsinki.assets;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import helsinki.security.tokens.persistent.Asset_CanDelete_Token;
import helsinki.security.tokens.persistent.Asset_CanSave_Token;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import ua.com.fielden.platform.entity.annotation.EntityType;
import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.keygen.IKeyNumber;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.security.Authorise;

/**
 * DAO implementation for companion object {@link AssetCo}.
 *
 * @author Developers
 *
 */
@EntityType(Asset.class)
public class AssetDao extends CommonEntityDao<Asset> implements AssetCo {
    
    

    @Inject
    public AssetDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
        public Asset new_() {
            // TODO Auto-generated method stub
            return super.new_().setNumber(DEFAULT_KEY_VALUE);
        }

    @Override
    @SessionRequired
    @Authorise(Asset_CanSave_Token.class)
    public Asset save(final Asset asset) {
        final boolean wasPersisted = asset.isPersisted();
        
        if(!wasPersisted) {
            final IKeyNumber keyNumberCo = co(KeyNumber.class);
            final String nextNumber = StringUtils.leftPad(keyNumberCo.nextNumber(ASSET_KEY_NAME).toString(), 9, "0");
            asset.setNumber(nextNumber);
        }
        
        try {
            return super.save(asset);
        } catch (final Exception ex) {
            if (!wasPersisted) {
                asset.beginInitialising();
                asset.setNumber(DEFAULT_KEY_VALUE);
                asset.endInitialising();
            }
            throw ex;
        }
        
    }

    @Override
    @SessionRequired
    @Authorise(Asset_CanDelete_Token.class)
    public int batchDelete(final Collection<Long> entitiesIds) {
        return defaultBatchDelete(entitiesIds);
    }

    @Override
    @SessionRequired
    @Authorise(Asset_CanDelete_Token.class)
    public int batchDelete(final List<Asset> entities) {
        return defaultBatchDelete(entities);
    }

    @Override
    protected IFetchProvider<Asset> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}