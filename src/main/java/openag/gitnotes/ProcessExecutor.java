package openag.gitnotes;

/**
 * Executor of git remote process requests
 */
public interface ProcessExecutor {

  /**
   * Starts the git fetch process (git-upload-pack)
   *
   * @param path path of the repository
   * @return {@link ProcessRef} instance
   */
  ProcessRef fetch(String path);

  /**
   * Starts the git push process (git-receive-pack)
   *
   * @param path path of the repository
   * @return {@link ProcessRef} instance
   */
  ProcessRef push(String path);
}
