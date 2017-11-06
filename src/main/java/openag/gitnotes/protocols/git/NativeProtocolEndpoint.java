package openag.gitnotes.protocols.git;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import openag.gitnotes.ProcessExecutor;

/**
 * Network endpoint that implements native git protocol
 */
public class NativeProtocolEndpoint {

  private final int port;
  private final ProcessExecutor processExecutor;
  private final int numberOfProtocolHandlers;

  private EventLoopGroup connectionGroup;
  private EventExecutorGroup protocolGroup;

  NativeProtocolEndpoint(final int port,
                         final ProcessExecutor processExecutor,
                         final int numberOfProtocolHandlers) {
    this.port = port;
    this.processExecutor = processExecutor;
    this.numberOfProtocolHandlers = numberOfProtocolHandlers;
  }

  /**
   * Creates new socket and starts listening for incoming connections. This method must be called in order to initialize
   * git connector properly
   */
  @SuppressWarnings("unused")
  public void start() {
    connectionGroup = new NioEventLoopGroup();
    protocolGroup = new DefaultEventExecutorGroup(this.numberOfProtocolHandlers);

    new ServerBootstrap()
        .group(connectionGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new DefaultChannelInitializer())
        .bind(port);
  }

  /**
   * Shuts down socket and releases resources; must be called as a part of application shutdown process
   */
  @SuppressWarnings("unused")
  public void stop() {
    connectionGroup.shutdownGracefully();
    protocolGroup.shutdownGracefully();
  }

  /**
   * Netty {@link ChannelInitializer}, builds the initial state of pipeline.
   */
  class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
      ch.pipeline()
        .addLast(new GitLengthFieldBasedFrameDecoder())
        .addLast(new GitHeaderDecoder())
        .addLast(new PipelineReconfigurer());
    }
  }

  /**
   * Netty inbound handler that accepts incoming request header {@link GitHeader} and re-building the channel pipeline
   * to handle binary part of the protocol
   */
  class PipelineReconfigurer extends SimpleChannelInboundHandler<GitHeader> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final GitHeader header) throws Exception {
      final ChannelPipeline pipeline = ctx.pipeline();

      // clean the pipeline completely
      pipeline.remove(GitLengthFieldBasedFrameDecoder.class);
      pipeline.remove(GitHeaderDecoder.class);
      pipeline.remove(PipelineReconfigurer.class);

      // add new binary handler
      pipeline.addLast(protocolGroup, new GitBinaryProtocolHandler(header, processExecutor));
    }
  }
}
