package openag.gitnotes;

import openag.gitnotes.protocols.git.NativeProtocolEndpoint;
import openag.gitnotes.protocols.git.NativeProtocolEndpointFactory;
import openag.gitnotes.protocols.ssh.SshProtocolEndpoint;
import openag.gitnotes.protocols.ssh.SshProtocolEndpointFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * todo:
 */
@Configuration
public class Config {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public SshProtocolEndpoint sshProtocolEndpoint() {
    final SshProtocolEndpointFactory factory = new SshProtocolEndpointFactory();
    // todo: factory.set...
    return factory.getObject();
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public NativeProtocolEndpoint nativeProtocolEndpoint() {
    final NativeProtocolEndpointFactory factory = new NativeProtocolEndpointFactory();
    // todo: factory.set...
    return factory.getObject();
  }

}
