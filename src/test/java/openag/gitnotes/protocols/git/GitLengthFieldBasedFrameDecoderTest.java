package openag.gitnotes.protocols.git;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import openag.gitnotes.protocols.git.GitLengthFieldBasedFrameDecoder;
import org.junit.Test;

import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;


public class GitLengthFieldBasedFrameDecoderTest {

  private final GitLengthFieldBasedFrameDecoder decoder = new GitLengthFieldBasedFrameDecoder();

  @Test
  public void test() throws Exception {
    final ByteBuf buf = Unpooled.buffer();
    buf.writeBytes("0024git-upl".getBytes());

    final long length = decoder.getUnadjustedFrameLength(buf, 0, 4, ByteOrder.BIG_ENDIAN);
    assertEquals(36, length);
  }

}