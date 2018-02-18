package selenium.boot.autoconfigure;



/**
 * Callback interface that can be implemented by beans wishing to further customize the
 * {@link com.fasterxml.jackson.databind.ObjectMapper} via {@link JacksonObjectMapperBuilder} retaining its default
 * auto-configuration.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@FunctionalInterface
public interface JacksonObjectMapperBuilderCustomizer
{
    /**
     * Customize the JacksonObjectMapperBuilder.
     *
     * @param jacksonObjectMapperBuilder the JacksonObjectMapperBuilder to customize
     */
    void customize( JacksonObjectMapperBuilder jacksonObjectMapperBuilder );
}
