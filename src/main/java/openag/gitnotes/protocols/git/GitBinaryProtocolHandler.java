package openag.gitnotes.protocols.git;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import openag.gitnotes.ProcessExecutor;
import openag.gitnotes.ProcessRef;

import java.nio.ByteBuffer;

/**
 * Handles the binary part of the protocol
 */
class GitBinaryProtocolHandler extends SimpleChannelInboundHandler<ByteBuffer> {

  private final GitHeader header;
  private final ProcessExecutor processExecutor;

  GitBinaryProtocolHandler(final GitHeader header,
                           final ProcessExecutor processExecutor) {
    this.header = header;
    this.processExecutor = processExecutor;
  }

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final ByteBuffer msg) throws Exception {
    final ProcessRef ref = execute(header);

    //todo: implement

    while (ref.isProcessAlive() || ref.isReadable()) {
      final ByteBuf buf = ctx.alloc().buffer();
//      buf.writeBytes(ref.in(), length);

      ctx.write(buf);
    }
  }

  private ProcessRef execute(final GitHeader header) {
    switch (header.getCommand()) {
      case "git-upload-pack":
        return processExecutor.fetch(header.getPath());
      case "git-receive-pack":
        return processExecutor.push(header.getPath());
      default:
        throw new IllegalArgumentException("Git command '" + header.getCommand() + "' is not supported");
    }
  }

}
