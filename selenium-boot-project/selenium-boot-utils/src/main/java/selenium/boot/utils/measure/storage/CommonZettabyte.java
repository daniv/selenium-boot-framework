package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Zettabyte as commonly found in the wild (1 Zettabyte = 1 180 591 620 717 411 303 424 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class CommonZettabyte extends StorageUnit<CommonZettabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonZettabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */
    
    public static CommonZettabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonZettabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */
    public static CommonZettabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Zettabyte contains.
     *
     * @return A new Zettabyte unit with the given value.
     */
    public static CommonZettabyte valueOf( final long numberOfBytes )
    {
        return new CommonZettabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function< BigInteger,  StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_ZEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "ZB";
    }

    @Override
    public CommonZettabyte add( final long bytesToAdd )
    {
        return new CommonZettabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonZettabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonZettabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonZettabyte divide( final long divisor )
    {
        return new CommonZettabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonZettabyte multiply( final long factor )
    {
        return new CommonZettabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonZettabyte subtract( final long bytesToSubtract )
    {
        return new CommonZettabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonZettabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonZettabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
