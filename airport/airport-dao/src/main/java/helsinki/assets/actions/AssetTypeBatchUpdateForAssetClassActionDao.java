package helsinki.assets.actions;

import static org.junit.Assert.assertEquals;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import java.util.stream.Stream;

import com.google.inject.Inject;

import ua.com.fielden.platform.security.Authorise;
import ua.com.fielden.platform.dao.annotations.SessionRequired;
import helsinki.assets.AssetClass;
import helsinki.assets.AssetClassCo;
import helsinki.assets.AssetType;
import helsinki.assets.AssetTypeCo;
import helsinki.security.tokens.functional.AssetTypeBatchUpdateForAssetClassAction_CanExecute_Token;
import ua.com.fielden.platform.dao.CommonEntityDao;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.entity.annotation.EntityType;

/**
 * DAO implementation for companion object {@link AssetTypeBatchUpdateForAssetClassActionCo}.
 *
 * @author Developers
 *
 */
@EntityType(AssetTypeBatchUpdateForAssetClassAction.class)
public class AssetTypeBatchUpdateForAssetClassActionDao extends CommonEntityDao<AssetTypeBatchUpdateForAssetClassAction> implements AssetTypeBatchUpdateForAssetClassActionCo {

    @Inject
    public AssetTypeBatchUpdateForAssetClassActionDao(final IFilter filter) {
        super(filter);
    }

    @Override
    @SessionRequired
    @Authorise(AssetTypeBatchUpdateForAssetClassAction_CanExecute_Token.class)
    public AssetTypeBatchUpdateForAssetClassAction save(final AssetTypeBatchUpdateForAssetClassAction action) {
        action.isValid().ifFailure(Result::throwRuntime);
        
        final var query = select(AssetType.class).where().prop("id").in().values(action.getSelectedEntityIds().toArray()).model();
        final var qem = from(query).with(AssetTypeCo.FETCH_PROVIDER.fetchModel()).model();

        final AssetTypeCo coAssetType = co$(AssetType.class);
        try (final Stream<AssetType> st = coAssetType.stream(qem)) {
            st.forEach(at -> coAssetType.save(at.setAssetClass(action.getAssetClass())));
        }
        
        return super.save(action);
    }

}