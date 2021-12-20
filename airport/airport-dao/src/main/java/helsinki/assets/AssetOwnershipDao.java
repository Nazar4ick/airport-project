package helsinki.assets;

import com.google.inject.Inject;

import java.util.Collection;
import java.util.List;

import ua.com.fielden.platform.entity.fetch.IFetchProvider;
import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import helsinki.security.tokens.persistent.AssetOwnership_CanSave_Token;
import helsinki.security.tokens.persistent.AssetOwnership_CanDelete_Token;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.entity.annotation.EntityType;

/**
 * DAO implementation for companion object {@link AssetOwnershipCo}.
 *
 * @author Developers
 *
 */
@EntityType(AssetOwnership.class)
public class AssetOwnershipDao extends CommonEntityDao<AssetOwnership> implements AssetOwnershipCo {

    @Inject
    public AssetOwnershipDao(final IFilter filter) {
        super(filter);
    }
    
    @Override
    public AssetOwnership new_() {
        final var ownership = super.new_();
        ownership.getProperty("role").setRequired(true, ERR_REQUIRED);
        ownership.getProperty("organisation").setRequired(true, ERR_REQUIRED);
        ownership.getProperty("bu").setRequired(true, ERR_REQUIRED);
        return ownership;
    }

    @Override
    @SessionRequired
    @Authorise(AssetOwnership_CanSave_Token.class)
    public AssetOwnership save(AssetOwnership entity) {
        return super.save(entity);
    }

    @Override
    @SessionRequired
    @Authorise(AssetOwnership_CanDelete_Token.class)
    public int batchDelete(final Collection<Long> entitiesIds) {
        return defaultBatchDelete(entitiesIds);
    }

    @Override
    @SessionRequired
    @Authorise(AssetOwnership_CanDelete_Token.class)
    public int batchDelete(final List<AssetOwnership> entities) {
        return defaultBatchDelete(entities);
    }

    @Override
    protected IFetchProvider<AssetOwnership> createFetchProvider() {
        return FETCH_PROVIDER;
    }
}