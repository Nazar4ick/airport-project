package helsinki.security.tokens.persistent;

import static java.lang.String.format;

import helsinki.assets.Asset;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;
import ua.com.fielden.platform.security.tokens.Template;

/**
 * A security token for entity {@link Asset} to guard Delete.
 *
 * @author Developers
 *
 */
public class Asset_CanDelete_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.DELETE.forTitle(), Asset.ENTITY_TITLE);
    public final static String DESC = format(Template.DELETE.forDesc(), Asset.ENTITY_TITLE);
}