package selenium.boot.core.matchers;


import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
class FileComparator
{
    //region Static definitions, members, initialization and constructors

    //---------------------------------------------------------------------
    // Static definitions, members, initialization and constructors
    //---------------------------------------------------------------------

    private final File original;

    private final File revised;

    FileComparator( File original, File revised )
    {
        this.original = original;
        this.revised = revised;
    }

    //endregion

    public List<Chunk> getChangesFromOriginal() throws IOException
    {
        return getChunksByType( Delta.TYPE.CHANGE );
    }

    private List<Chunk> getChunksByType( Delta.TYPE type ) throws IOException
    {
        final List<Chunk> listOfChanges = new ArrayList<>();
        final List<Delta> deltas = getDeltas();
        for( Delta delta : deltas )
        {
            if( delta.getType() == type )
            {
                listOfChanges.add( delta.getRevised() );
            }
        }
        return listOfChanges;
    }

    private List<Delta> getDeltas() throws IOException
    {

        final List<String> originalFileLines = fileToLines( original );
        final List<String> revisedFileLines = fileToLines( revised );

        final Patch patch = DiffUtils.diff( originalFileLines, revisedFileLines );

        return patch.getDeltas();
    }

    private List<String> fileToLines( File file ) throws IOException
    {
        final List<String> lines = new ArrayList<>();
        String line;
        final BufferedReader in = new BufferedReader( new FileReader( file ) );
        while( ( line = in.readLine() ) != null )
        {
            lines.add( line );
        }

        return lines;
    }

    public List<Chunk> getInsertsFromOriginal() throws IOException
    {
        return getChunksByType( Delta.TYPE.INSERT );
    }

    public List<Chunk> getDeletesFromOriginal() throws IOException
    {
        return getChunksByType( Delta.TYPE.DELETE );
    }

}
