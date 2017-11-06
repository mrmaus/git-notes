package openag.gitnotes.protocols.ssh;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.gss.GSSAuthenticator;
import org.apache.sshd.server.auth.gss.UserAuthGSSFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructor for SSH server based on apache SSHD project with Kerberos authentication support
 * <p>
 * Big thanks to Alex B for <a href="https://github.com/alblue/Examples/blob/master/JavaSSH/com.bandlem.examples.javassh.kerberizedSshServer/src/main/java/com/bandlem/examples/javassh/KerberizedSshServer.java">Initial
 * Implementation</a>
 */
class KerberizedSshServer {
  private static final Logger log = LoggerFactory.getLogger(KerberizedSshServer.class);

  private final SshServer server;

  KerberizedSshServer(int port,
                      String keytab,
                      String principalName,
                      CommandFactory commandFactory) {
    final SshServer server = SshServer.setUpDefaultServer();
    server.setPort(port);

    if (principalName == null) {
      principalName = "host/" + localhost().getCanonicalHostName();
      log.info("No principal specified; using " + principalName);
    }

    // The GSS Authenticator is used to authenticate against Kerberized credentials
    final GSSAuthenticator authenticator = new GSSAuthenticator();
    authenticator.setKeytabFile(keytab);
    authenticator.setServicePrincipalName(principalName);
    server.setGSSAuthenticator(authenticator);

    final List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>(1);
    userAuthFactories.add(new UserAuthGSSFactory());
    server.setUserAuthFactories(userAuthFactories);

    // Persisting host key - otherwise it will be generated on each restart and some clients may complain
    final String hostKey = System.getProperty("java.io.tmpdir") + "/KerberizedSshServer.hostKey";
    server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(hostKey)));

    server.setCommandFactory(commandFactory);

    this.server = server;
  }

  private static InetAddress localhost() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

  void start() throws IOException {
    this.server.start();
  }

  void stop() throws IOException {
    this.server.stop();
  }
}
