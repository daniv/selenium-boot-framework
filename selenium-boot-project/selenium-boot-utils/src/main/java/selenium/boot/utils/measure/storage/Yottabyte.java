package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * Yottabyte as specified in ISO IEC 80000-13:2008 (1 Yottabyte = 1 000 000 000 000 000 000 000 000 Byte).
 *
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 1.0
 */
public final class Yottabyte extends StorageUnit<Yottabyte>
{
    //region initialization and constructors section

    Yottabyte( final BigInteger bytes )
    {
        super( bytes );
    }


    //endregion 

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */

    public static Yottabyte valueOf( final BigInteger numberOfBytes )
    {
        return new Yottabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */

    public static Yottabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Yottabyte contains.
     *
     * @return A new Yottabyte unit with the given value.
     */

    public static Yottabyte valueOf( final long numberOfBytes )
    {
        return new Yottabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: decimalValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_YOTTABYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "YB";
    }

    @Override
    public Yottabyte add( final long bytesToAdd )
    {
        return new Yottabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public Yottabyte add( final StorageUnit<?> storageAmount )
    {
        return new Yottabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public Yottabyte divide( final long divisor )
    {
        return new Yottabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public Yottabyte multiply( final long factor )
    {
        return new Yottabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public Yottabyte subtract( final long bytesToSubtract )
    {
        return new Yottabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public Yottabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new Yottabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
