package selenium.boot.spring.env;


import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Set;



/**
 * Exception thrown when required environment variables are not found.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.springframework.core.env.MissingRequiredPropertiesException
 * @see com.google.common.collect.Sets
 * @since 1.0
 */
public class MissingEnvironmentVariableException extends IllegalStateException
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Set<String> missingRequiredVariable = Sets.newLinkedHashSet();

    public MissingEnvironmentVariableException()
    {
        super();
    }

    //endregion

    public void addMissingRequiredEnvironmentVariable( @Nonnull String key )
    {
        this.missingRequiredVariable.add( key );
    }

    @Override
    public String getMessage()
    {
        return "The following environment variables are required but could not be resolved: " + getMissingRequiredEnvironmentVariables();
    }

    /**
     * Return the set of properties marked as required but not present upon validation.
     */
    public Set<String> getMissingRequiredEnvironmentVariables()
    {
        return this.missingRequiredVariable;
    }
}
