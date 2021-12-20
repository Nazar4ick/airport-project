package helsinki.security.tokens.persistent;

import static java.lang.String.format;

import helsinki.assets.AssetType;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;
import ua.com.fielden.platform.security.tokens.Template;

/**
 * A security token for entity {@link AssetType} to guard Delete.
 *
 * @author Developers
 *
 */
public class AssetType_CanDelete_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.DELETE.forTitle(), AssetType.ENTITY_TITLE);
    public final static String DESC = format(Template.DELETE.forDesc(), AssetType.ENTITY_TITLE);
}