package openag.gitnotes.protocols.ssh;

import org.springframework.util.Assert;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Network socket that supports Git protocol over SSH
 */
public class SshProtocolEndpoint {

  private final KerberizedSshServer server;

  SshProtocolEndpoint(final int port,
                      final String keytabAbsolutePath,
                      final String principalName,
                      final ExecutorService executorService) {

    Assert.hasText(keytabAbsolutePath, "Keytab file location must be provided");
    Assert.hasText(principalName, "Principal name must be provided");

    server = new KerberizedSshServer(
        port,
        keytabAbsolutePath,
        principalName,
        new GitCommandFactory(executorService));
  }

  /**
   * Starts up the server; must be called before use
   */
  public void start() throws IOException {
    server.start();
  }

  /**
   * Shuts down the SSH server; mandatory teardown operation
   */
  public void stop() throws IOException {
    server.stop();
  }
}
