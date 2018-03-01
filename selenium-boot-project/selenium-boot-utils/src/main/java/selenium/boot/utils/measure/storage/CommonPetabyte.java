package selenium.boot.utils.measure.storage;


import java.math.BigInteger;
import java.util.function.Function;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public final class CommonPetabyte extends StorageUnit<CommonPetabyte>
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    CommonPetabyte( final BigInteger bytes )
    {
        super( bytes );
    }

    //endregion  

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new Petabyte unit with the given value.
     */

    public static CommonPetabyte valueOf( final BigInteger numberOfBytes )
    {
        return new CommonPetabyte( numberOfBytes );
    }

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new Petabyte unit with the given value.
     */

    public static CommonPetabyte valueOf( final Long numberOfBytes )
    {
        return valueOf( numberOfBytes.longValue() );
    }

    /**
     * @param numberOfBytes The amount of bytes the Petabyte contains.
     *
     * @return A new CommonPetabyteUnit unit with the given value.
     */

    public static CommonPetabyte valueOf( final long numberOfBytes )
    {
        return new CommonPetabyte( BigInteger.valueOf( numberOfBytes ) );
    }

    @Override
    protected Function<BigInteger, StorageUnit<?>> converter()
    {
        return StorageUnits:: commonValueOf;
    }

    @Override
    protected BigInteger getNumberOfBytesPerUnit()
    {
        return StorageUnit.BYTES_IN_A_PEBIBYTE;
    }

    @Override
    protected String getSymbol()
    {
        return "PB";
    }

    @Override
    public CommonPetabyte add( final long bytesToAdd )
    {
        return new CommonPetabyte( bytes.add( BigInteger.valueOf( bytesToAdd ) ) );
    }

    @Override
    public CommonPetabyte add( final StorageUnit<?> storageAmount )
    {
        return new CommonPetabyte( bytes.add( storageAmount.bytes ) );
    }

    @Override
    public CommonPetabyte divide( final long divisor )
    {
        return new CommonPetabyte( bytes.divide( BigInteger.valueOf( divisor ) ) );
    }

    @Override
    public CommonPetabyte multiply( final long factor )
    {
        return new CommonPetabyte( bytes.multiply( BigInteger.valueOf( factor ) ) );
    }

    @Override
    public CommonPetabyte subtract( final long bytesToSubtract )
    {
        return new CommonPetabyte( bytes.subtract( BigInteger.valueOf( bytesToSubtract ) ) );
    }

    @Override
    public CommonPetabyte subtract( final StorageUnit<?> storageAmount )
    {
        return new CommonPetabyte( bytes.subtract( storageAmount.bytes ) );
    }

}
