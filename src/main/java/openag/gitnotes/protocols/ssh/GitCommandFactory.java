package openag.gitnotes.protocols.ssh;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;

import java.util.concurrent.ExecutorService;

/**
 * todo: implement
 */
class GitCommandFactory implements CommandFactory {

  private final ExecutorService executorService;

  GitCommandFactory(final ExecutorService executorService) {
    this.executorService = executorService;
  }

  @Override
  public Command createCommand(final String command) {
    System.out.println("ssh>>> " + command);

    return new GitCommand(executorService);
  }
}
