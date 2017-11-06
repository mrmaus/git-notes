package openag.gitnotes.protocols.http;

import openag.gitnotes.protocols.ProtocolCommand;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handler for HTTP protocol endpoint requests
 */
public class HttpProtocolHandler {

  private static final byte[] PADDING = "0000".getBytes();

  /**
   * Handler method for GET request on /info/refs path
   * <p>
   * Sample: GET $GIT_URL/info/refs?service=git-upload-pack HTTP/1.0
   */
  public void refs(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final ProtocolCommand command = ProtocolCommand.parse(request.getParameter("service"));

    response.setHeader("Content-Type",
        "application/x-" + command.getService() + "-advertisement");

    response.setHeader("Cache-Control", "no-cache");

    final ServletOutputStream out = response.getOutputStream();

    final String header = "# service=git-" + command.getService() + "\n";

    out.write(String.format("%04X", header.length() + 4).getBytes()); //header 4-byte hex length field
    out.write(header.getBytes());
    out.write(PADDING);

    // todo: --stateless-rpc  --advertise-refs
  }

  /**
   * POST $GIT_URL/git-upload-pack HTTP/1.0
   */
  public void upload(HttpServletRequest request,
                     HttpServletResponse response) {

    response.setHeader("Content-Operation", "application/x-git-upload-pack-result");

    //todo: git-upload-pack --stateless-rpc
  }

  /**
   * POST $GIT_URL/git-receive-pack HTTP/1.0
   */
  public void receive(final HttpServletRequest request,
                      final HttpServletResponse response) {

    response.setHeader("Content-Operation", "application/x-git-receive-pack-result");

    // todo: git-receive-pack --stateless-rpc
  }
}
