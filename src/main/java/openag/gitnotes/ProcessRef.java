package openag.gitnotes;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Reference to a running git process
 */
public interface ProcessRef {

  boolean isProcessAlive();

  boolean isReadable();

  InputStream in();

  OutputStream out();
}
