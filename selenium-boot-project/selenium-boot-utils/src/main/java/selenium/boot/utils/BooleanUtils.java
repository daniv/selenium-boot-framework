package selenium.boot.utils;


import org.springframework.lang.Nullable;

import javax.annotation.concurrent.ThreadSafe;



/**
 * Operations on boolean primitives and Boolean objects.
 *
 * This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behaviour in more detail.
 * source: <b>org.apache.commons.lang3;</b>
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
@ThreadSafe
public final class BooleanUtils
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private BooleanUtils()
    {
        super();
    }

    //endregion


    /**
     * Negates the specified boolean.
     * <p>
     * If {@code null} is passed in, {@code null} will be returned.
     * <p>
     * NOTE: This returns null and will throw a NullPointerException if unboxed to a boolean.
     * <p>
     * <pre>{@code
     *   BooleanUtils.negate(Boolean.TRUE)  = Boolean.FALSE;
     *   BooleanUtils.negate(Boolean.FALSE) = Boolean.TRUE;
     *   BooleanUtils.negate(null)          = null;
     * }</pre>
     *
     * @param bool the Boolean to negate, may be null
     *
     * @return the negated Boolean, or {@code null} if {@code null} input
     */
    @Nullable
    public static Boolean negate( @Nullable final Boolean bool )
    {
        if( bool == null )
        {
            return null;
        }
        return bool ? Boolean.FALSE : Boolean.TRUE;
    }

}
