package selenium.boot.spring.support.jackson;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class AnnotationSensitivePropertyNamingStrategy extends PropertyNamingStrategy
{
    //region initialization and constructors section

    private final SnakeCaseStrategy snakeCase;

    public AnnotationSensitivePropertyNamingStrategy()
    {
        super();
        this.snakeCase = new SnakeCaseStrategy();
    }


    //endregion



    @Override
    public String nameForConstructorParameter( MapperConfig<?> config,
                                               AnnotatedParameter ctorParam,
                                               String defaultName )
    {
        if( ctorParam.getDeclaringClass().isAnnotationPresent( JsonSnakeCase.class ) )
        {
            return snakeCase.nameForConstructorParameter( config, ctorParam, defaultName );
        }
        return super.nameForConstructorParameter( config, ctorParam, defaultName );
    }

    @Override
    public String nameForField( MapperConfig<?> config,
                                AnnotatedField field,
                                String defaultName )
    {
        if( field.getDeclaringClass().isAnnotationPresent( JsonSnakeCase.class ) )
        {
            return snakeCase.nameForField( config, field, defaultName );
        }

        return super.nameForField( config, field, defaultName );
    }

    @Override
    public String nameForGetterMethod( MapperConfig<?> config,
                                       AnnotatedMethod method,
                                       String defaultName )
    {
        if( method.getDeclaringClass().isAnnotationPresent( JsonSnakeCase.class ) )
        {
            return snakeCase.nameForGetterMethod( config, method, defaultName );
        }
        return super.nameForGetterMethod( config, method, defaultName );
    }

    @Override
    public String nameForSetterMethod( MapperConfig<?> config,
                                       AnnotatedMethod method,
                                       String defaultName )
    {
        if( method.getDeclaringClass().isAnnotationPresent( JsonSnakeCase.class ) )
        {
            return snakeCase.nameForSetterMethod( config, method, defaultName );
        }
        return super.nameForSetterMethod( config, method, defaultName );
    }
}
