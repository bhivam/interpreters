### Dynamic Typing
Lox is dynamically typed. A variable can hold a value of any type and can hold values of various types over its life time. *Potential goal is to make Lox statically typed*

### Automatic Memory Management
There are two main techniques for managing memory:
- Reference Counting
- Garbage Collection
Lox uses garbage collection.

### Data Types
#### Booleans
Lox has a dedicated type for representing booleans, True or False.
#### Numbers
We only have double-percision floating point, expressed as integer and decimal literals. *Potengial goal to be able to use octal, decimal, binary, hexadecimal representations. Also to add in a type sepcifically for integers*
#### Strings
We have strings as well. Lots of questions for how we implement this.
#### Nil
This is a type that represented no value ("null" in other languages).

### Expressions
#### Arithmetic
+, -, *, /, corresponding to addition, subtraction, multiplication, division respectively. - is also a prefix operator that can be used to negate an number.

\+ operator can also be used on strings to concatenate them.

*adding more operators*

#### Comparison and Equality
<, <=, >, >= can only be used on numbers in the obvious way.
equality can be checked within any type and also between type. There is not implicit type coversion, so things of different types are always unequal.

#### Logical Operators

the not operator is a prefix "!" and negates a boolean. "and" and "or" are infic operators that act in the obvious way. 

"and" will return the left operand if it's false and the right operand otherwise. This is basically short circuiting. We will check what the left, "first" operand is. If the left operand is false we return false. If it is not false, then the truth of the and statement is whatever the right operand is, so we just compute the right operand and return it.

"or" will return the left operand if it is true and will return the right operand if the left operand is false. This is the same principle as for the and operator. 

#### Precedence and Grouping
The precedence is the same as in C and () can be used to override the normal precedence. *Potential goal to implement bitwise, modulo, ternary/condition, shift operators*

### Statements
A statement is composed of expressions. Where an expression is used to produce a value statements produce effects. For example, modifying state, getting input, giving output. 

expressions followed by semicolons are statements called expression statements.

We can pack a series of statements, where one is expected, by surrounding the group of statements with curly braces.

### Variables
Variables are initialized with var. When no value is provided, initially, variables are initialized to nil.

variables can be assigned to using their names. Scoping is very similar to what we find in C or Java.

### Control Flow
if, else will execute one of two statements based on some condition. while will execude the body repeatedly as long as the condition expression provided remains true at the end of the last iteration. For loops is the same thing as the while loop except some parts are baked in. Less boilerplate code with for loops. *implement foreach loops*

### Functions
Function call syntax is the same as C. We define functions with the key word fun. If we don't return anything in the function body, nil is returned implicitly. 
'''
fun returnSum(a, b) {
  return a + b;
}
'''

### Closure
Functions here are first-class, so they are real values that can be manipulated, stored in variables, passed to other functions. Functions can be declared within functions and they are local to the function they wre declared within. We can create some interesting cases.
'''
fun returnFunction() {
  var outside = "outside";

  fun inner() {
    print outside;
  }

  return inner;
}

var fn = returnFunction();
fn();
'''

In this case inner gets a local variable declared outside of it. this means inner holds onto references to any surrounding variables that it uses so that they stay around even after the outer function has retured. **What happens if we declare it and then we change the value of the variable. Does the call before and after the change have different behaviour** A function like inner is called a closure. Now variable scopes are not a simple stack where local variables of a function can be disregarded once the function returns. 

### Classes
Objects can be very useful for bundling various data together and then scoping functions relating to the object on that object so that naming schemes aren't annoying.

#### Classes versus Prototypes
The class implementation of objects is more common than the prototype implementation. You can create instances from the classes which have the data and state of the object and link back to the class which contains the objects inheritence chain and methods. 

Prototype based OOP will merge classes and instances. There will only be objects and objects contain state and methods. They can direcly inherit from each other or delegate to each other. Since they directly delegate to eachother, objects in a prototypical system can have very unusual patterns and people will generally just use them like classes anyways. But there is more flexibility if there is something specific you want to achieve. 

#### Classes in Lox
```
class Breakfast {
    cook() {
        print "Eggs are frying";
    }

    serve(who) {
        print "enjoy your breakfast " + who + ".";
    }
}
```
This pretty much explains how to instantiate a class and its methods. Classes are also first class. So they can be passed to functions and assigned to variables. 

#### Instantiation and Initialization
We will be able to add any properties we want onto objects. We can access this within our methods by using the this keyword. There is an init function that will take in some parameters so that we can use them to initialize some fields.

#### Inheritance
We can create subclasses that will inherit every method definition in it super class. We can also define our own init function in the subclass, overriding the other init from the super class. But we can call the super class's init with super.init.

Lox is not a true OOP. *Maybe as an exercise I can change the primitives to also be objects with functions.*

#### Standard Library
*As an exercise add in string manipulation, trigonometric functions, file I/O, networking, reading input from the user.


