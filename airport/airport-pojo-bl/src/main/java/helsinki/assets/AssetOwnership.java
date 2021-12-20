package helsinki.assets;

import java.util.Date;

import helsinki.assets.definers.AssetOwnershipBuDefiner;
import helsinki.assets.definers.AssetOwnershipOrganisationDefiner;
import helsinki.assets.definers.AssetOwnershipRoleDefiner;
import ua.com.fielden.platform.entity.ActivatableAbstractEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DescTitle;
import ua.com.fielden.platform.entity.annotation.DisplayDescription;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.mutator.AfterChange;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * One-2-Many entity object.
 *
 * @author Developers
 *
 */
@KeyType(DynamicEntityKey.class)
@KeyTitle("AssetOwnsership")
@CompanionObject(AssetOwnershipCo.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
public class AssetOwnership extends ActivatableAbstractEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(AssetOwnership.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    @IsProperty
    @MapTo
    @CompositeKeyMember(1)
    private Asset asset;
    
    @IsProperty
    @MapTo
    @Title(value = "Ownership start date", desc = "The date when this ownership started")
    @CompositeKeyMember(2)
    private Date startDate;
    
    @IsProperty
    @MapTo
    @Title(value = "Role", desc = "Owner as Role")
    @AfterChange(AssetOwnershipRoleDefiner.class)
    private String role;
    
    @IsProperty
    @MapTo
    @Title(value = "Organisation", desc = "Owner as organisation")
    @AfterChange(AssetOwnershipOrganisationDefiner.class)
    private String organisation;
    
    @IsProperty
    @MapTo
    @Title(value = "Business Unit", desc = "Owner as Business Unit")
    @AfterChange(AssetOwnershipBuDefiner.class)
    private String bu;

    @Observable
    public AssetOwnership setBu(final String bu) {
        this.bu = bu;
        return this;
    }

    public String getBu() {
        return bu;
    }

    @Observable
    public AssetOwnership setOrganisation(final String organisation) {
        this.organisation = organisation;
        return this;
    }

    public String getOrganisation() {
        return organisation;
    }

    @Observable
    public AssetOwnership setRole(final String Role) {
        this.role = Role;
        return this;
    }

    public String getRole() {
        return role;
    }

    @Observable
    public AssetOwnership setStartDate(final Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Observable
    public AssetOwnership setAsset(final Asset value) {
        this.asset = value;
        return this;
    }

    public Asset getAsset() {
        return asset;
    }

}
