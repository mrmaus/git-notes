package openag.gitnotes.protocols.git;

import openag.gitnotes.ProcessExecutor;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory for {@link NativeProtocolEndpoint} instance
 */
public class NativeProtocolEndpointFactory implements FactoryBean<NativeProtocolEndpoint> {

  private static final int DEFAULT_GIT_PORT = 9418;

  /**
   * Git endpoint port, defaults to {@link #DEFAULT_GIT_PORT} (default for git:// url)
   */
  private int port = DEFAULT_GIT_PORT;

  /**
   * Git remote process executor instance
   */
  private ProcessExecutor processExecutor;

  /**
   * Max number of concurrently executing git handlers, which is basically max number of concurrent active git://
   * protocol connections
   */
  private int numberOfProtocolHandlers = 10;

  /**
   * The endpoint singleton instance
   */
  private NativeProtocolEndpoint instance;

  @SuppressWarnings("unused")
  public void setPort(final int port) {
    this.port = port;
  }

  @SuppressWarnings("unused")
  public void setProcessExecutor(final ProcessExecutor processExecutor) {
    this.processExecutor = processExecutor;
  }

  @SuppressWarnings("unused")
  public void setNumberOfProtocolHandlers(final int numberOfProtocolHandlers) {
    this.numberOfProtocolHandlers = numberOfProtocolHandlers;
  }

  @Override
  public NativeProtocolEndpoint getObject() {
    if (this.instance == null) {
      this.instance = new NativeProtocolEndpoint(
          this.port,
          this.processExecutor,
          this.numberOfProtocolHandlers
      );
    }
    return this.instance;
  }

  @Override
  public Class<?> getObjectType() {
    return NativeProtocolEndpoint.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
