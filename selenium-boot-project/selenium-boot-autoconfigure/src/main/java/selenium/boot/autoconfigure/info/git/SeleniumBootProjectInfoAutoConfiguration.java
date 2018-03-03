package selenium.boot.autoconfigure.info.git;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
@Configuration
@EnableConfigurationProperties( { SeleniumBootProjectInfoProperties.class } )
@AutoConfigureOrder( value = AutoConfigureOrder.DEFAULT_ORDER + 3 )
public class SeleniumBootProjectInfoAutoConfiguration
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger( SeleniumBootProjectInfoAutoConfiguration.class.getName() );

    private final SeleniumBootProjectInfoProperties properties;

    public SeleniumBootProjectInfoAutoConfiguration( SeleniumBootProjectInfoProperties properties )
    {
        this.properties = properties;
    }

    //endregion


   // @Conditional( GitResourceAvailableCondition.class )
    @ConditionalOnResource( resources = "${selenium.boot.info.git.location:classpath:git.properties}" )
    @ConditionalOnMissingBean
    @Bean
    public GitProperties gitProperties() throws Exception
    {
        GitProperties gp = new GitProperties( loadFrom( this.properties.getGit().getLocation(), "git" ) );
        return gp;
    }

    protected Properties loadFrom( Resource location, String prefix ) throws IOException
    {
        String p = prefix.endsWith( "." ) ? prefix : prefix + ".";
        Properties source = PropertiesLoaderUtils.loadProperties( location );
        Properties target = new Properties();
        for( String key : source.stringPropertyNames() )
        {
            if( key.startsWith( p ) )
            {
                target.put( key.substring( p.length() ), source.get( key ) );
            }
        }
        return target;
    }

    @ConditionalOnResource( resources = "${selenium.boot.info.build.location:classpath:META-INF/build-info.properties}" )
    @ConditionalOnMissingBean
    @Bean
    public BuildProperties buildProperties() throws Exception
    {
        log.debug( "Creating @Bean buildProperties." );
        BuildProperties bp =  new BuildProperties( loadFrom( this.properties.getBuild().getLocation(), "build" ) );
        return bp;
    }
}
