package openag.gitnotes.protocols;

/**
 * The git commands that are supported by the remote protocol
 */
public enum ProtocolCommand {

  upload_pack("git-upload-pack", "upload-pack"),
  receive_pack("git-receive-pack", "receive-pack");

  private final String service;
  private final String command;

  ProtocolCommand(final String service, final String command) {
    this.service = service;
    this.command = command;
  }

  public String getService() {
    return service;
  }

  public String getCommand() {
    return command;
  }

  public static ProtocolCommand parse(String s) {
    for (ProtocolCommand command : values()) {
      if (command.service.equalsIgnoreCase(s) || command.command.equalsIgnoreCase(s)) {
        return command;
      }
    }
    throw new IllegalArgumentException("Command '" + s + "' is not supported by the git remote protocol handler");
  }

}
