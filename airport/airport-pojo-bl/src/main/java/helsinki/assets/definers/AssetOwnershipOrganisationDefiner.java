package helsinki.assets.definers;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import helsinki.assets.AssetOwnership;
import helsinki.assets.AssetOwnershipCo;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractAfterChangeEventHandler;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class AssetOwnershipOrganisationDefiner extends AbstractAfterChangeEventHandler<String> {
    
    private final IUniversalConstants uc;
    
    @Inject
    protected AssetOwnershipOrganisationDefiner(final IUniversalConstants uc) {
        this.uc = uc;
    }
    
    @Override
    public void handle(final MetaProperty<String> mpOrganisation, final String value) {
        final AssetOwnership entity = mpOrganisation.getEntity();
        if (!entity.isInitialising()) {
            if (!StringUtils.isEmpty(value)) {
                mpOrganisation.setRequired(true, AssetOwnershipCo.ERR_REQUIRED);
                entity.getProperty("role").setRequired(false, AssetOwnershipCo.ERR_REQUIRED);
                entity.getProperty("bu").setRequired(false, AssetOwnershipCo.ERR_REQUIRED);
                entity.setRole(null);
                entity.setBu(null);
            }
        } else {
            mpOrganisation.setRequired(!StringUtils.isEmpty(value), AssetOwnershipCo.ERR_REQUIRED);
        }
    }
}
