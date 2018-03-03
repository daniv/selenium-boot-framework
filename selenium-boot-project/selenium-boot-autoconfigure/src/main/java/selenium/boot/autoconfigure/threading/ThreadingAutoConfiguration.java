package selenium.boot.autoconfigure.threading;


import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import selenium.boot.utils.Threading;

import javax.annotation.PostConstruct;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
@Configuration
@AutoConfigureOrder( value = AutoConfigureOrder.DEFAULT_ORDER + 4 )
@EnableConfigurationProperties( value = { ThreadingProperties.class } )
public class ThreadingAutoConfiguration
{
    //region initialization and constructors section

    private ThreadingProperties properties;

    public ThreadingAutoConfiguration( ThreadingProperties properties )
    {
        super();
        this.properties = properties;
    }

    //endregion
    
    @PostConstruct
    public void setPolicy()
    {
        Threading.setPolicy( properties.getCyclingPolicy() );
    }
}
