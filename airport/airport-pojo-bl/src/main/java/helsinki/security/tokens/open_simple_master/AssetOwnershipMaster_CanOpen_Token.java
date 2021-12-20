package helsinki.security.tokens.open_simple_master;

import static java.lang.String.format;

import helsinki.assets.AssetOwnership;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;
import ua.com.fielden.platform.security.tokens.Template;

/**
 * A security token for entity {@link AssetOwnership} to guard Open.
 *
 * @author Developers
 *
 */
public class AssetOwnershipMaster_CanOpen_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.MASTER_OPEN.forTitle(), AssetOwnership.ENTITY_TITLE + " Master");
    public final static String DESC = format(Template.MASTER_OPEN.forDesc(), AssetOwnership.ENTITY_TITLE);
}