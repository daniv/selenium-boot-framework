package selenium.boot.autoconfigure.info.git;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
@ConfigurationProperties( prefix = "selenium.boot.info" )
public class SeleniumBootProjectInfoProperties
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final SeleniumBootProjectInfoProperties.Build build = new SeleniumBootProjectInfoProperties.Build();

    private final SeleniumBootProjectInfoProperties.Git git = new SeleniumBootProjectInfoProperties.Git();

    public SeleniumBootProjectInfoProperties()
    {
        super();
    }

    //endregion

    public SeleniumBootProjectInfoProperties.Build getBuild()
    {
        return this.build;
    }

    public SeleniumBootProjectInfoProperties.Git getGit()
    {
        return this.git;
    }

    public static class Git
    {
        private Resource location = new ClassPathResource( "META-INF/git.properties" );

        public Resource getLocation()
        {
            return this.location;
        }

        public void setLocation( Resource location )
        {
            this.location = location;
        }
    }

    public static class Build
    {
        private Resource location = new ClassPathResource( "META-INF/build-info.properties" );

        public Resource getLocation()
        {
            return this.location;
        }

        public void setLocation( Resource location )
        {
            this.location = location;
        }
    }
}
