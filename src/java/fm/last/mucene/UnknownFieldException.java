package fm.last.mucene;

public class UnknownFieldException extends Exception {
  private static final long serialVersionUID = 1L;

  public UnknownFieldException() {
    super();
  }

  public UnknownFieldException(Throwable e) {
    super(e);
  }

  public UnknownFieldException(String message) {
    super(message);
  }

  public UnknownFieldException(String message, Throwable e) {
    super(message, e);
  }
}
