package psl.workflakes.littlejil;

/**
 * A general purpose Exception class for use in the plugins
 * @author matias
 */
public class PluginException extends Exception {

    public PluginException() {
        super();
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

}
