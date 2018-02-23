package selenium.boot.core.validation;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;



/**
 *  An invocation handler used to test method-level validation.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class ValidationInvocationHandler implements InvocationHandler
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final Object wrapped;

    private final Validator validator;

    private final Class<?>[] groups;

    public ValidationInvocationHandler( Object wrapped, Validator validator, Class<?>... groups )
    {
        this.wrapped = wrapped;
        this.validator = validator;
        this.groups = groups;
    }

    //endregion

    @Override
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
    {
        Set<ConstraintViolation<Object>> constraintViolations = validator.forExecutables().validateParameters(
                wrapped,
                method,
                args == null ? new Object[] { } : args,
                groups
        );

        if( !constraintViolations.isEmpty() )
        {
            throw new ConstraintViolationException( new HashSet<ConstraintViolation<?>>( constraintViolations ) );
        }

        Object result = method.invoke( wrapped, args );

        constraintViolations = validator.forExecutables().validateReturnValue( wrapped, method, result, groups );

        if( !constraintViolations.isEmpty() )
        {
            throw new ConstraintViolationException( new HashSet<ConstraintViolation<?>>( constraintViolations ) );
        }

        return result;
    }
}
