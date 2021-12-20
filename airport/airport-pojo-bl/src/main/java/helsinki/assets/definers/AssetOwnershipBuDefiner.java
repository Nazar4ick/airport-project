package helsinki.assets.definers;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import helsinki.assets.AssetOwnership;
import helsinki.assets.AssetOwnershipCo;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractAfterChangeEventHandler;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class AssetOwnershipBuDefiner extends AbstractAfterChangeEventHandler<String> {
    
    private final IUniversalConstants uc;
    
    @Inject
    protected AssetOwnershipBuDefiner(final IUniversalConstants uc) {
        this.uc = uc;
    }
    
    @Override
    public void handle(final MetaProperty<String> mpBu, final String value) {
        final AssetOwnership entity = mpBu.getEntity();
        if (!entity.isInitialising()) {
            if (!StringUtils.isEmpty(value)) {
                mpBu.setRequired(true, AssetOwnershipCo.ERR_REQUIRED);
                entity.getProperty("organisation").setRequired(false, AssetOwnershipCo.ERR_REQUIRED);
                entity.getProperty("role").setRequired(false, AssetOwnershipCo.ERR_REQUIRED);
                entity.setOrganisation(null);
                entity.setRole(null);
            }
        } else {
            mpBu.setRequired(!StringUtils.isEmpty(value), AssetOwnershipCo.ERR_REQUIRED);
        }
    }
}
