package selenium.boot.utils;


import org.springframework.lang.Nullable;

import java.util.function.Supplier;



/**
 *  Utility class (extends Springs Assert class) for doing assertions on parameters and object state.
 *  provides additional asserts
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@SuppressWarnings( "BooleanParameter" )
public final class Assert extends org.springframework.util.Assert
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private Assert()
    {
        // utility class
    }


    //endregion


    /**
     * Asserts that the value of {@code state} is true. If not, an IllegalStateException is thrown.
     *
     * @param state           the state validation expression
     * @param messageSupplier Supplier of the exception message if state evaluates to false
     */
    public static void state( boolean state, Supplier<String> messageSupplier )
    {
        if( !state )
        {
            throw new IllegalStateException( messageSupplier.get() );
        }
    }

    /**
     * Asserts that the given {@code expression} is false. If not, an IllegalArgumentException is thrown.
     *
     * @param expression      the state validation expression
     * @param messageSupplier Supplier of the exception message if the expression evaluates to true
     */
    public static void isFalse( boolean expression, Supplier<String> messageSupplier )
    {
        if( expression )
        {
            throw new IllegalArgumentException( messageSupplier.get() );
        }
    }

    /**
     * Assert that the given {@code value} is not {@code null}. If not, an IllegalArgumentException is
     * thrown.
     *
     * @param value           the value not to be {@code null}
     * @param messageSupplier Supplier of the exception message if the assertion fails
     */
    public static void notNull( @Nullable Object value, Supplier<String> messageSupplier )
    {
        isTrue( value != null, messageSupplier );
    }

    /**
     * Asserts that the given {@code expression} is true. If not, an IllegalArgumentException is thrown.
     *
     * @param expression      the state validation expression
     * @param messageSupplier Supplier of the exception message if the expression evaluates to false
     */
    public static void isTrue( boolean expression, Supplier<String> messageSupplier )
    {
        if( !expression )
        {
            throw new IllegalArgumentException( messageSupplier.get() );
        }
    }

    /**
     * Assert that the given {@code value} is not {@code null}. If not, an IllegalArgumentException is
     * thrown.
     *
     * @param value           the value not to be {@code null}
     * @param messageSupplier Supplier of the exception message if the assertion fails
     */
    public static <T> T nonNull( @Nullable T value, Supplier<String> messageSupplier )
    {
        isTrue( value != null, messageSupplier );
        return value;
    }
}
