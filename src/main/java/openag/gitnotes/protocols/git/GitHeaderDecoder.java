package openag.gitnotes.protocols.git;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Decodes native git protocol header bytes to {@link GitHeader} instance. This decoder must be configured together with
 * {@link GitLengthFieldBasedFrameDecoder}
 */
class GitHeaderDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
    in.skipBytes(4); //4-byte line length value in hex

    // git command (git-receive-pack or git-upload-pack)
    final CharSequence command = in.readCharSequence(in.bytesBefore((byte) ' '), Charset.defaultCharset());

    in.skipBytes(1); //space separator

    // git requested repository path (as it was specified with git command)
    final CharSequence path = in.readCharSequence(in.bytesBefore((byte) '\0'), Charset.defaultCharset());

    in.skipBytes(1); // zero byte

    in.skipBytes(in.bytesBefore((byte) '\0')); //skip the remainder (always ends with zero byte)

    in.skipBytes(1); //last zero byte

    final GitHeader header = new GitHeader(command.toString(), path.toString());
    out.add(header);
  }

}
