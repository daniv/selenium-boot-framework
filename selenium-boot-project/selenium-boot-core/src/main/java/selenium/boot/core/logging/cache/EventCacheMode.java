package selenium.boot.core.logging.cache;


import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author <a href="mailto:solmarkn@gmail.com">Dani Vainstein</a>
 * @version %I%, %G%
 * @since 2.0
 */
public enum EventCacheMode
{
    OFF
            {
                @Override
                public ILoggingEventCache createCache()
                {
                    return new ILoggingEventCache()
                    {

                        @Override
                        public List<ILoggingEvent> get()
                        {
                            return Collections.emptyList();
                        }

                        @Override
                        public void put( ILoggingEvent event )
                        {
                            //When caching is off, events are discarded as they are received
                        }
                    };
                }
            },
    ON
            {
                @Override
                public ILoggingEventCache createCache()
                {
                    return new ILoggingEventCache()
                    {

                        private List<ILoggingEvent> events = new ArrayList<ILoggingEvent>();

                        @Override
                        public List<ILoggingEvent> get()
                        {
                            List<ILoggingEvent> list = Collections.unmodifiableList( events );
                            events = null;
                            return list;
                        }

                        @Override
                        public void put( ILoggingEvent event )
                        {
                            events.add( event );
                        }
                    };
                }
            },
    SOFT
            {
                @Override
                public ILoggingEventCache createCache()
                {
                    return new ILoggingEventCache()
                    {

                        private List<SoftReference<ILoggingEvent>> references = new ArrayList<SoftReference<ILoggingEvent>>();

                        @Override
                        public List<ILoggingEvent> get()
                        {
                            List<ILoggingEvent> events = new ArrayList<ILoggingEvent>( references.size() );
                            for( SoftReference<ILoggingEvent> reference : references )
                            {
                                ILoggingEvent event = reference.get();
                                if( event != null )
                                {
                                    events.add( event );
                                }
                            }
                            references = null;
                            return Collections.unmodifiableList( events );
                        }

                        @Override
                        public void put( ILoggingEvent event )
                        {
                            references.add( new SoftReference<ILoggingEvent>( event ) );
                        }
                    };
                }
            };

    public abstract ILoggingEventCache createCache();
}
