package helsinki.assets.definers;

import java.util.Date;

import com.google.inject.Inject;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractAfterChangeEventHandler;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.IUniversalConstants;

public class AssetFinDetInitCostDefiner extends AbstractAfterChangeEventHandler<Money> {
    
    private final IUniversalConstants uc;
    
    @Inject
    protected AssetFinDetInitCostDefiner(final IUniversalConstants uc) {
        this.uc = uc;
    }
    
    @Override
    public void handle(final MetaProperty<Money> mpInitCost, final Money value) {
        if (!mpInitCost.getEntity().isInitialising()) {
            if (mpInitCost.getEntity().getProperty("commissionDate").getValue() == null) {
                mpInitCost.getEntity().getProperty("commissionDate").setValue(uc.now().toDate());
            }
        }
        mpInitCost.getEntity().getProperty("commissionDate").setRequired(value != null);
    }
}
