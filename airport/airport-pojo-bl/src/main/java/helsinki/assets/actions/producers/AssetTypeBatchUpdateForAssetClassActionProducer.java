package helsinki.assets.actions.producers;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.DefaultEntityProducerWithContext;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;
import ua.com.fielden.platform.error.Result;
import helsinki.assets.actions.AssetTypeBatchUpdateForAssetClassAction;
/**
 * A producer for new instances of entity {@link AssetTypeBatchUpdateForAssetClassAction}.
 *
 * @author Developers
 *
 */
public class AssetTypeBatchUpdateForAssetClassActionProducer extends DefaultEntityProducerWithContext<AssetTypeBatchUpdateForAssetClassAction> {

    @Inject
    public AssetTypeBatchUpdateForAssetClassActionProducer(final EntityFactory factory, final ICompanionObjectFinder coFinder) {
        super(factory, AssetTypeBatchUpdateForAssetClassAction.class, coFinder);
    }

    @Override
    protected AssetTypeBatchUpdateForAssetClassAction provideDefaultValues(final AssetTypeBatchUpdateForAssetClassAction action) {
        if (contextNotEmpty()) {
            if (selectedEntitiesEmpty()) {
                throw Result.failure("Please select some asset types to be updated and try again.");
            }
            action.setSelectedEntityIds(selectedEntityIds());
            action.getProperty("selectedEntityIds").validationResult().ifFailure(Result::throwRuntime);
        }
        return super.provideDefaultValues(action);
    }
}
