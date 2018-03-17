package selenium.boot.core.logging;


import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

import java.util.Map;



/**
 * Changes the {@link ch.qos.logback.classic.encoder.PatternLayoutEncoder} default {@code highlight} definition.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see ch.qos.logback.classic.encoder.PatternLayoutEncoder
 * @see CustomHighlightingCompositeConverter
 * @since 1.0
 */
class CustomPatternLayoutEncoder extends PatternLayoutEncoder
{
    @Override
    public void start()
    {
        PatternLayout patternLayout = new PatternLayout();
        Map<String, String> defaultMap = patternLayout.getDefaultConverterMap();

        /* checks if already contains map property highlight in default converters */
        if( defaultMap.containsKey( "highlight" ) )
        {
            defaultMap.remove( "highlight" );
            defaultMap.put( "highlight", CustomHighlightingCompositeConverter.class.getName() );
        }

        /* checks if already contains map property highlight in instance converters */
        Map<String, String> instance = patternLayout.getInstanceConverterMap();
        if( instance.containsKey( "highlight" ) )
        {
            instance.remove( "highlight" );
            instance.put( "highlight", CustomHighlightingCompositeConverter.class.getName() );
        }

        /* checks if already contains map property highlight in effective converters map */
        Map<String, String> effective = patternLayout.getEffectiveConverterMap();
        if( effective.containsKey( "highlight" ) )
        {
            effective.remove( "highlight" );
            effective.put( "highlight", CustomHighlightingCompositeConverter.class.getName() );
        }

        patternLayout.setContext( context );
        patternLayout.setPattern( getPattern() );
        patternLayout.setOutputPatternAsHeader( outputPatternAsHeader );
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }
}
