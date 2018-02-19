package selenium.boot.spring.support.jackson;


import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;



/**
 * A subtype resolver which discovers subtypes via
 * {@code META-INF/services/selenium.boot.jackson.Discoverable}.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver
 * @since 1.0
 */
public class DiscoverableSubtypeResolver extends StdSubtypeResolver
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger( DiscoverableSubtypeResolver.class.getName() );

    private static final String SERVICES = "META-INF/services/";

    private final ImmutableList<Class<?>> discoveredSubtypes;

    public DiscoverableSubtypeResolver()
    {
        this( Discoverable.class );
    }

    public DiscoverableSubtypeResolver( Class<?> rootClass )
    {
        final ImmutableList.Builder<Class<?>> subtypes = ImmutableList.builder();
        for( Class<?> clazz : discoverServices( rootClass ) )
        {
            for( Class<?> subtype : discoverServices( clazz ) )
            {
                subtypes.add( subtype );
                registerSubtypes( subtype );
            }
        }
        this.discoveredSubtypes = subtypes.build();
    }

    //endregion

    protected List<Class<?>> discoverServices( Class<?> clazz )
    {
        final List<Class<?>> serviceClasses = Lists.newArrayList();
        try
        {
            final Enumeration<URL> resources = getClassLoader().getResources( SERVICES + clazz.getName() );
            while( resources.hasMoreElements() )
            {
                final URL url = resources.nextElement();
                try( InputStream input = url.openStream();
                     InputStreamReader streamReader = new InputStreamReader( input, StandardCharsets.UTF_8 );
                     BufferedReader reader = new BufferedReader( streamReader ) )
                {
                    String line;
                    while( ( line = reader.readLine() ) != null )
                    {
                        final Class<?> loadedClass = loadClass( line );
                        if( loadedClass != null )
                        {
                            serviceClasses.add( loadedClass );
                        }
                    }
                }
            }
        }
        catch( IOException e )
        {
            log.warn( "Unable to load META-INF/services/{}", clazz.getName(), e );
        }
        return serviceClasses;
    }

    protected ClassLoader getClassLoader()
    {
        return this.getClass().getClassLoader();
    }

    @Nullable
    private Class<?> loadClass( String line )
    {
        try
        {
            return getClassLoader().loadClass( line.trim() );
        }
        catch( ClassNotFoundException e )
        {
            log.info( "Unable to load {}", line );
            return null;
        }
    }

    public ImmutableList<Class<?>> getDiscoveredSubtypes()
    {
        return discoveredSubtypes;
    }
}
