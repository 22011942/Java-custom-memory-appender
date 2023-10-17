import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;

public class VelocityLayout extends Layout {
    private String pattern;
    private Template template;

    public VelocityLayout() {
        this("[$p] $c $d: $m");
    }

    public VelocityLayout(String pattern) {
        setPattern(pattern);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        try {
            Velocity.init();
            this.template = RuntimeSingleton.getTemplate(pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String format(LoggingEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLoggerName());
        context.put("d", event.getTimeStamp());
        context.put("m", event.getMessage());
        context.put("p", event.getLevel().toString());
        context.put("t", Thread.currentThread().getName());
        context.put("n", System.getProperty("line.separator"));

        StringWriter writer = new StringWriter();
        try {
            template.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {
        // No options to activate
    }
}
