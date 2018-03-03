package selenium.boot.autoconfigure.threading;


import com.google.common.util.concurrent.CycleDetectingLockFactory;
import com.google.common.util.concurrent.CycleDetectingLockFactory.Policies;
import com.google.common.util.concurrent.CycleDetectingLockFactory.Policy;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see com.google.common.util.concurrent.CycleDetectingLockFactory.Policy
 * @since 1.0
 */
@ConfigurationProperties( prefix = "selenium.boot.thread" )
public class ThreadingProperties
{
    //region initialization and constructors section

    /**
     * Pre-defined {@link com.google.common.util.concurrent.CycleDetectingLockFactory.Policy} implementations.
     */
    private CycleDetectingLockFactory.Policy cyclingPolicy = Policies.WARN;


    public ThreadingProperties()
    {
        super();
    }

    //endregion


    public Policy getCyclingPolicy()
    {
        return cyclingPolicy;
    }

    public void setCyclingPolicy( Policy cyclingPolicy )
    {
        this.cyclingPolicy = cyclingPolicy;
    }
}
