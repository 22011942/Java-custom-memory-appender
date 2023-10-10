import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemAppender extends AppenderSkeleton {
    private static MemAppender instance = new MemAppender();
    private List<LoggingEvent> logs;
    private Layout layout;
    private int maxSize;
    private long discardedLogCount;

    private MemAppender() {
        logs = new ArrayList<>();
        maxSize = 100;
    }

    public static MemAppender getInstance() {
        return instance;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    public List<LoggingEvent> getCurrentLogs() {
        return Collections.unmodifiableList(logs);
    }

    public List<String> getEventStrings() {
        List<String> eventStrings = new ArrayList<>();
        for (LoggingEvent event : logs) {
            eventStrings.add(layout.format(event));
        }
        return Collections.unmodifiableList(eventStrings);
    }

    public void printLogs() {
        for (LoggingEvent event : logs) {
            System.out.println(layout.format(event));
        }
        logs.clear();
    }

    public long getDiscardedLogCount() {
        return discardedLogCount;
    }



    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (logs.size() >= maxSize) {
            logs.remove(0);
            discardedLogCount++;
        }
        logs.add(loggingEvent);
    }
    @Override
    public void close() {

    }
    @Override
    public boolean requiresLayout() {
        return false;
    }
}
