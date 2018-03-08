package selenium.boot.core.matchers;


import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.LevenshteinResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public class Testa
{
    //region initialization and constructors section

    private static final Logger log = LoggerFactory.getLogger( Testa.class );

    public Testa()
    {
        super();
    }

    //endregion

    public static void main( String[] args ) throws Exception
    {
        // https://www.adictosaltrabajo.com/tutoriales/comparar-ficheros-java-diff-utils/
//        File original = new File( "C:\\SeleniumBoot\\pom1.xml" );
//        File revised = new File( "C:\\SeleniumBoot\\pom2.xml" );
//        final FileComparator comparator = new FileComparator(original, revised );
//        List<Chunk> ch1 =  comparator.getChangesFromOriginal();
//        List<Chunk> ch2 =  comparator.getDeletesFromOriginal();
//        List<Chunk> ch3 =  comparator.getInsertsFromOriginal();


        LevenshteinDetailedDistance UNLIMITED_DISTANCE = new LevenshteinDetailedDistance();
        LevenshteinResults results = UNLIMITED_DISTANCE.apply( "Martin Daniel Vainstein Alvear", "Martinito");
        System.out.println( "getDeleteCount = [" + results.getDeleteCount() + "]" );
        System.out.println( "getDistance = [" + results.getDistance() + "]" );
        System.out.println( "getInsertCount = [" + results.getInsertCount() + "]" );
        System.out.println( "getSubstituteCount = [" + results.getSubstituteCount() + "]" );


         LevenshteinDistance UNLIMITED_DISTANCE_L = new LevenshteinDistance();
        int y = UNLIMITED_DISTANCE_L.apply( "Martin Daniel Vainstein Alvear", "Martinito");
        System.out.println( "y = [" + y + "]" );

    }
}
