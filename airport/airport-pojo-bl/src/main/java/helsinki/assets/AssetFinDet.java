package helsinki.assets;

import java.util.Date;

import helsinki.assets.definers.AssetFinDetInitCostDefiner;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.Dependent;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.AfterChange;
import ua.com.fielden.platform.entity.validation.annotation.GeProperty;
import ua.com.fielden.platform.entity.validation.annotation.LeProperty;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.Pair;

/**
 * One-2-One entity object.
 *
 * @author Developers
 *
 */
@KeyType(Asset.class)
@KeyTitle("Asset")
@CompanionObject(AssetFinDetCo.class)
@MapEntityTo
public class AssetFinDet extends AbstractPersistentEntity<Asset> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetFinDet.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();
    
    @IsProperty
    @MapTo
    @Dependent("disposalDate")
    @Title(value = "Commission Date", desc = "The date when this asset was commissioned.")
    private Date commissionDate;

    @IsProperty
    @MapTo
    @Dependent("commissionDate")
    @Title(value = "Disposal Date", desc = "The date when this asset was disposed.")
    private Date disposalDate;
    
    @IsProperty
    @MapTo
    @Title(value = "Initial Cost", desc = "The initial cost of this asset.")
    @AfterChange(AssetFinDetInitCostDefiner.class)
    private Money initCost;

    @Observable
    public AssetFinDet setInitCost(final Money initCost) {
        this.initCost = initCost;
        return this;
    }
    
    @Override
    @Observable
        public AssetFinDet setKey(Asset key) {
            // TODO Auto-generated method stub
            super.setKey(key);
            return this;
        }

    public Money getInitCost() {
        return initCost;
    }

    


    @Observable
    @LeProperty("disposalDate")
    public AssetFinDet setCommissionDate(final Date commissionDate) {
        this.commissionDate = commissionDate;
        return this;
    }

    public Date getCommissionDate() {
        return commissionDate;
    }

    @Observable
    @GeProperty("commissionDate")
    public AssetFinDet setDisposalDate(final Date disposalDate) {
        this.disposalDate = disposalDate;
        return this;
    }

    public Date getDisposalDate() {
        return disposalDate;
    }

}
