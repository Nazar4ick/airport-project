package helsinki.common.validators;

import java.lang.annotation.Annotation;
import java.util.Set;

import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.meta.impl.AbstractBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

public class NoSpacesValidator extends AbstractBeforeChangeEventHandler<String> {
    
    public static final String ERR_CONTAINS_SPACES = "Value should not contain any spaces. Please remove them.";

    @Override
    public Result handle(final MetaProperty<String> prop, final String newValue, Set<Annotation> annotations) {
        if(newValue != null && newValue.contains(" ")) {
            
            return Result.failure(ERR_CONTAINS_SPACES);
        }
        return Result.successful(newValue);
    }

}
