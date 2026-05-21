package exceptions;

public class InvalidLineException extends Exception {
    public InvalidLineException(String message) { super(message); }
}

public class InvalidWarehouseException extends Exception {
    public InvalidWarehouseException(String message) { super(message); }
}

public class InvalidLocationException extends Exception {
    public InvalidLocationException(String message) { super(message); }
}

public class InvalidTypeException extends Exception {
    public InvalidTypeException(String message) { super(message); }
}

public class NotFoundException extends Exception {
    public NotFoundException(String message) { super(message); }
}