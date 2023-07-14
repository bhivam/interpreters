package lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        /*
         * This is a a runtime error because functions may refer a variable
         * without using it yet. So to say that its illegal to refer to an
         * undefined variable would be annoying. Instead we opt for checking
         * in runtime, thus returning a runtime error.
         * 
         * This allows us some ease when it comes to mutually recursive
         * procedures. Other languages solve the issue by gathering declarations
         * before the rest of the parsing.
         */
        throw new RuntimeError(name, "Undefined variable '" +
                name.lexeme + "'");
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

}
