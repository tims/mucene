package fm.last;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.log4j.Logger;

/**
 * Example of how to write a daemon.
 */
public class DaemonExample implements Daemon {

  private static Logger log = Logger.getLogger(DaemonExample.class);

  /**
   * The runnable.
   */
  private DoStuff doStuff = null;

  @Override
  public void destroy() {
    log.debug("Destroying");
  }

  @Override
  public void init(DaemonContext context) throws Exception {
    log.debug("Initing");
    this.doStuff = new DoStuff();
  }

  @Override
  public void start() throws Exception {
    log.debug("Starting");
    Thread t = new Thread(doStuff);
    t.start();
  }

  @Override
  public void stop() throws Exception {
    log.debug("Stopping");
    this.doStuff.setShutdown(true);
  }

  /**
   * Example runnable class that just logs some test output.
   */
  static class DoStuff implements Runnable {
    private volatile boolean shutdown = false;
    
    public void setShutdown(boolean shutdown) {
      this.shutdown = shutdown;
    }
    
    @Override
    public void run() {
      while (!shutdown) {
        log.info("Running...");

        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          log.warn("Interrupted!", e);
        }
      }
    }
  }
}
