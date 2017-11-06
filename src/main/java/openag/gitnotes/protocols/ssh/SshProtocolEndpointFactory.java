package openag.gitnotes.protocols.ssh;

import org.springframework.beans.factory.FactoryBean;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring factory for {@link SshProtocolEndpoint} instances
 */
public class SshProtocolEndpointFactory implements FactoryBean<SshProtocolEndpoint> {

  /**
   * The SSH socket port
   */
  private int port = 2522;

  /**
   * The location of keytab file (absolute path)
   */
  private String keytabAbsolutePath;

  /**
   * The service account principal name
   */
  private String principalName;

  /**
   * The executor service that will be used to run SSH commands in a separate thread
   */
  private ExecutorService executorService;

  private volatile SshProtocolEndpoint endpoint;

  @Override
  public SshProtocolEndpoint getObject() {
    if (endpoint == null) {
      if (executorService == null) {
        executorService = defaultExecutorService();
      }

      endpoint = new SshProtocolEndpoint(
          port,
          keytabAbsolutePath,
          principalName,
          executorService);
    }
    return this.endpoint;
  }

  /**
   * Default {@link ExecutorService} for SSHD daemon if no other provided
   */
  private static ExecutorService defaultExecutorService() {
    return Executors.newCachedThreadPool(new DaemonThreadFactory());
  }

  private static class DaemonThreadFactory implements ThreadFactory {
    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
    private final AtomicInteger poolNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(final Runnable r) {
      final Thread thread = defaultThreadFactory.newThread(r);
      thread.setDaemon(true);
      thread.setName("sshd-" + poolNumber.getAndIncrement() + "-thread");
      return thread;
    }
  }

  @Override
  public Class<?> getObjectType() {
    return SshProtocolEndpoint.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  public void setPort(final int port) {
    this.port = port;
  }

  public void setKeytabAbsolutePath(final String keytabAbsolutePath) {
    this.keytabAbsolutePath = keytabAbsolutePath;
  }

  public void setPrincipalName(final String principalName) {
    this.principalName = principalName;
  }

  public void setExecutorService(final ExecutorService executorService) {
    this.executorService = executorService;
  }

  @PreDestroy
  public void shutdown() {
    this.executorService.shutdown();
  }
}
