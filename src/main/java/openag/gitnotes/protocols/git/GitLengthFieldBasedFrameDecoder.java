package openag.gitnotes.protocols.git;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * {@link LengthFieldBasedFrameDecoder} version for native git protocol; calculates line length using first 4 bytes of
 * the line (in hex)
 */
class GitLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

  GitLengthFieldBasedFrameDecoder() {
    super(Integer.MAX_VALUE, 0, 4, -4, 0);
  }

  @Override
  protected final long getUnadjustedFrameLength(final ByteBuf buf,
                                                final int offset,
                                                final int length,
                                                final ByteOrder order) {
    final String s = buf.toString(offset, length, Charset.defaultCharset());
    return Integer.parseInt(s, 16);
  }
}
