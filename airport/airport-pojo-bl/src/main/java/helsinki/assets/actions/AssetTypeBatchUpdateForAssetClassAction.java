package helsinki.assets.actions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import helsinki.assets.AssetClass;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.AbstractFunctionalEntityWithCentreContext;
import ua.com.fielden.platform.entity.NoKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Required;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author Developers
 *
 */
@KeyType(NoKey.class)
@CompanionObject(AssetTypeBatchUpdateForAssetClassActionCo.class)
public class AssetTypeBatchUpdateForAssetClassAction extends AbstractFunctionalEntityWithCentreContext<NoKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetTypeBatchUpdateForAssetClassAction.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
    
    protected AssetTypeBatchUpdateForAssetClassAction() {
        setKey(NoKey.NO_KEY);
    }
    
    @IsProperty
    @Required
    @Title(value = "Asset Class", desc = "New asset class.")
    private AssetClass assetClass;
    
    @IsProperty(Long.class)
    @Title(value = "Selected asset types", desc = "Extended_description")
    private Set<Long> selectedEntityIds = new HashSet<>();

    @Observable
    public AssetTypeBatchUpdateForAssetClassAction setSelectedEntityIds(final Set<Long> selectedEntityIds) {
        this.selectedEntityIds.clear();
        this.selectedEntityIds.addAll(selectedEntityIds);
        return this;
    }

    public Set<Long> getSelectedEntityIds() {
        return Collections.unmodifiableSet(selectedEntityIds);
    }

    @Observable
    public AssetTypeBatchUpdateForAssetClassAction setAssetClass(final AssetClass assetClass) {
        this.assetClass = assetClass;
        return this;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    

}
