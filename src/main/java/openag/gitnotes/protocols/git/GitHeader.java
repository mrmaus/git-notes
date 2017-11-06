package openag.gitnotes.protocols.git;

/**
 * Git native protocol header encapsulation. Every git client-server interaction starts with this header line
 */
class GitHeader {
  private final String command;
  private final String path;

  GitHeader(final String command, final String path) {
    this.command = command;
    this.path = path;
  }

  public String getCommand() {
    return command;
  }

  public String getPath() {
    return path;
  }
}
