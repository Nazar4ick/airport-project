package helsinki.assets;

import static org.junit.Assert.*;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.fetch;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.orderBy;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import java.math.BigDecimal;

import org.junit.Test;

import ua.com.fielden.platform.dao.QueryExecutionModel;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;
import ua.com.fielden.platform.security.user.User;
import ua.com.fielden.platform.test.ioc.UniversalConstantsForTesting;
import ua.com.fielden.platform.utils.IUniversalConstants;

import helsinki.personnel.Person;
import helsinki.personnel.PersonCo;
import helsinki.test_config.AbstractDaoTestCase;


/**
 * Basic testing of {@link AssetClass}.
 * 
 * @author Helsinki Team
 *
 */
public class AssetClassTest extends AbstractDaoTestCase {

    @Test
    public void a_simple_asset_class_can_be_created_and_saved() {
    	final var assetClass = co(AssetClass.class).new_();
    	assetClass.setName("Building");
    	assetClass.setDesc("Property, buildings, carparks");
    	final var savedAssetClass = co(AssetClass.class).save(assetClass);
    	assertNotNull(savedAssetClass);
    	assertTrue(savedAssetClass.isActive());
    	assertEquals("Building", savedAssetClass.getName());
    }
    
    @Test
    public void can_access_prop_active_with_default_fetch_model() {
        co(AssetClass.class).save(co(AssetClass.class).new_().setName("Building").setDesc("Property, buildings, carparks"));
        final var assetClass = co(AssetClass.class).findByKeyAndFetch(AssetClassCo.FETCH_PROVIDER.fetchModel(), "Building");
        assertTrue(assetClass.isActive());
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
