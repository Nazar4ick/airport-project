package helsinki.assets;

import static org.junit.Assert.*;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetch;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import java.math.BigDecimal;

import org.junit.Test;

import ua.com.fielden.platform.dao.QueryExecutionModel;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.test.ioc.UniversalConstantsForTesting;
import ua.com.fielden.platform.utils.IUniversalConstants;
import helsinki.common.validators.NoSpacesValidator;
import helsinki.personnel.Person;
import helsinki.personnel.PersonCo;
import helsinki.test_config.AbstractDaoTestCase;


/**
 * Basic testing of {@link AssetType}.
 * 
 * @author Helsinki Team
 *
 */
public class AssetTypeTest extends AbstractDaoTestCase {

    @Test
    public void a_simple_asset_type_can_be_created_and_saved() {
    	final var assetType = co(AssetType.class).new_();
    	assetType.setName("Tower");
    	assetType.setDesc("Control tower");
    	co(AssetClass.class).save(co(AssetClass.class).new_().setName("Building").setDesc("Property, buildings, carparks"));
    	final var assetClass = co(AssetClass.class).findByKeyAndFetch(AssetClassCo.FETCH_PROVIDER.fetchModel(), "Building");
        assetType.setAssetClass(assetClass);
    	final var savedAssetType = co(AssetType.class).save(assetType);
    	assertNotNull(savedAssetType);
    	assertTrue(savedAssetType.isActive());
    	assertEquals("Tower", savedAssetType.getName());
    	assertEquals("Building", savedAssetType.getAssetClass().getName());
    }
    
    @Test
    public void can_access_prop_active_with_default_fetch_model() {
        co(AssetType.class).save(co(AssetType.class).new_().setName("Tower").setDesc("Control tower"));
        final var assetType = co(AssetType.class).findByKeyAndFetch(AssetTypeCo.FETCH_PROVIDER.fetchModel(), "Tower");
        assertTrue(assetType.isActive());
    }
    
    @Test
    public void name_cannot_be_longer_than_50_characters() {
        final var longName = "Tower".repeat(50);
        final var assetType = co(AssetType.class).new_().setName(longName).setDesc("Control Tower");
        final MetaProperty<String> mpName = assetType.getProperty("name");
        assertNull(mpName.getValue());
        assertNull(assetType.getName());
        assertFalse(mpName.isValid());
        final Result validationResult = mpName.getFirstFailure();
        assertNotNull(validationResult);
        assertFalse(validationResult.isSuccessful());
        assertEquals("Value should not be longer than 50 characters.", validationResult.getMessage());
    }
    
    @Test
    public void name_cannot_contain_spaces() {
        final var assetType = co(AssetType.class).new_().setName("Tower").setDesc("Control Tower");
        assetType.setName("Name with spaces");
        final MetaProperty<String> mpName = assetType.getProperty("name");
        assertFalse(mpName.isValid());
        final Result validationResult = mpName.getFirstFailure();
        assertEquals(NoSpacesValidator.ERR_CONTAINS_SPACES, validationResult.getMessage());
        assertEquals("Tower", assetType.getName());
        assetType.setName("Tower1");
        assertTrue(mpName.isValid());
        assertEquals("Tower1", assetType.getName());
    }
    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    @Override
    public boolean useSavedDataPopulationScript() {
        return false;
    }

    @Override
    protected void populateDomain() {
        // Need to invoke super to create a test user that is responsible for data population 
    	super.populateDomain();

    	final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
    	constants.setNow(dateTime("2019-10-01 11:30:00"));

    	// If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }
    }

}
