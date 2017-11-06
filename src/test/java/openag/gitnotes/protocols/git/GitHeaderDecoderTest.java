package openag.gitnotes.protocols.git;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class GitHeaderDecoderTest {

  private final GitHeaderDecoder decoder = new GitHeaderDecoder();

  @Test
  public void test() throws Exception {
    final ByteBuf buf = Unpooled.buffer();
    buf.writeBytes("0024git-upload-pack /test.git\0host=\0".getBytes());

    final ArrayList<Object> out = new ArrayList<>(1);
    decoder.decode(null, buf, out);

    assertEquals(1, out.size());

    final GitHeader header = (GitHeader) out.get(0);

    assertEquals("git-upload-pack", header.getCommand());
    assertEquals("/test.git", header.getPath());
  }

}