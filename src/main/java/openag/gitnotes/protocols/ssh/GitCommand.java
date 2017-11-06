package openag.gitnotes.protocols.ssh;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

/**
 * TODO: implement
 */
class GitCommand implements Command, Runnable, SessionAware {

  private final ExecutorService executor;

  private InputStream in;
  private OutputStream out;
  private OutputStream err;

  private ExitCallback exitCallback;
  private ServerSession session;

  /**
   * todo:
   *
   * @param executor the {@link ExecutorService} instance that will run the command in a separate thread
   */
  GitCommand(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public void run() {
    final String username = session.getUsername();
    System.out.println(username);

    //todo: implement
  }

  @Override
  public void setInputStream(final InputStream in) {
    this.in = in;
  }

  @Override
  public void setOutputStream(final OutputStream out) {
    this.out = out;
  }

  @Override
  public void setErrorStream(final OutputStream err) {
    this.err = err;
  }

  @Override
  public void setExitCallback(final ExitCallback callback) {
    this.exitCallback = callback;
  }

  @Override
  public void start(final Environment env) throws IOException {
    executor.submit(this);
  }

  @Override
  public void destroy() throws Exception {
    //todo: destroy the git process
  }

  @Override
  public void setSession(final ServerSession session) {
    this.session = session;
  }
}
