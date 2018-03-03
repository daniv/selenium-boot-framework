package selenium.boot.core.support;


import org.springframework.core.AttributeAccessor;
import org.springframework.core.AttributeAccessorSupport;



/**
 * An {@link org.springframework.core.AttributeAccessor} that synchronizes on a mutex (not this) before
 * modifying or accessing the underlying attributes.
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @see org.springframework.core.AttributeAccessorSupport
 * @since 1.0
 */
public class SynchronizedAttributeAccessor implements AttributeAccessor
{

    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    /**
     * All methods are delegated to this support object.
     */
    private final AttributeAccessorSupport support = new AttributeAccessorSupport()
    {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = -7664290016506582290L;
    };

    @Override
    public int hashCode()
    {
        return support.hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object other )
    {
        if( this == other )
        {
            return true;
        }
        AttributeAccessorSupport that;
        if( other instanceof SynchronizedAttributeAccessor )
        {
            that = ( ( SynchronizedAttributeAccessor ) other ).support;
        }
        else if( other instanceof AttributeAccessorSupport )
        {
            that = ( AttributeAccessorSupport ) other;
        }
        else
        {
            return false;
        }
        synchronized( support )
        {
            return support.equals( that );
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder( "SynchronizedAttributeAccessor: [" );
        synchronized( support )
        {
            String[] names = attributeNames();
            for( int i = 0; i < names.length; i++ )
            {
                String name = names[ i ];
                buffer.append( names[ i ] ).append( "=" ).append( getAttribute( name ) );
                if( i < names.length - 1 )
                {
                    buffer.append( ", " );
                }
            }
            buffer.append( "]" );
            return buffer.toString();
        }
    }

    /**
     * Additional support for atomic put if absent.
     *
     * @param name  the key for the attribute name
     * @param value the value of the attribute
     *
     * @return null if the attribute was not already set, the existing value
     * otherwise.
     */
    public Object setAttributeIfAbsent( String name, Object value )
    {
        synchronized( support )
        {
            Object old = getAttribute( name );
            if( old != null )
            {
                return old;
            }
            setAttribute( name, value );
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.core.AttributeAccessor#setAttribute(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void setAttribute( String name, Object value )
    {
        synchronized( support )
        {
            support.setAttribute( name, value );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.core.AttributeAccessor#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute( String name )
    {
        synchronized( support )
        {
            return support.getAttribute( name );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.core.AttributeAccessor#removeAttribute(java.lang.String)
     */
    @Override
    public Object removeAttribute( String name )
    {
        synchronized( support )
        {
            return support.removeAttribute( name );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.core.AttributeAccessor#hasAttribute(java.lang.String)
     */
    @Override
    public boolean hasAttribute( String name )
    {
        synchronized( support )
        {
            return support.hasAttribute( name );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.core.AttributeAccessor#attributeNames()
     */
    @Override
    public String[] attributeNames()
    {
        synchronized( support )
        {
            return support.attributeNames();
        }
    }

}
