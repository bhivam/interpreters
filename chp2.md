# End to End Compilation Process
## Frontend 
### Lexer
This is where the raw text is tokenized. Useless text is discarded and only meaningful bundles of characters remain.

### Parser
Here we build a syntax tree out of the tokens. We see how the tokens relate to eachother and structure them to represent this relationship. This is where we can check for syntax errors. This means looking for problems related to the relationships between signals, and reporting these problems back to the user. 

### Static Analysis
Generally, languages will do binding and resolution here. For each identifier, you will attach it to a meaning region of the program. Let's take an example.

```c
void foo()
{
    int number;
}
```

In this example, number would be *bound* to the region of code associated with foo. If we are implementing a **staticly** typed language, then we can do all the type checking here.

During this analysis step, all the informtion can be stored in a symboltable, in some attributes of the node in the syntax tree, or we can transfrom the tree into an entirely new data structure which has all the information. 

## The "Middle End"
At each stage of the compiler pipeline the data representing the user's code is reorganized to make the job of the next stage easier. The front end of the compiler is specific to the source language of the program. THe back end looks at the final architecture. The middle stage acts as an interface between the frontend and backend representations. For example, LLVM is a very popular backend. Any frontend can be conformed to the LLVM backend if the intermediate representations transforms the
data correctly. 

As an example of this, Dart compiles to many native platform and also transpiles to Javscript. They are able to do this because they have many middle ends corresponding to each backend. 

You can also have one middle end which requires some specific initial intermediate representation and final intermediate representation. Then many frontends and backends cann be constructed to support various source languages and platforms. 

After the frontend breaks down the source code into something which has more semantic meaning, we can start swapping out some parts of the program for snippets that are highly optimized. A simple optimization is to evaluate constant expressions and switching the expression with the value itself. This is called constatn folding. 

## Backend
Now we can look at generating code the machine can actually understand. The first choice is whether we generate code for a real CPU or a virtual one. If we generate code for a real CPU, then our backend is locked into the particular architecture of the chip. Generating code for the virtual machine means that we are making some idealized low-level interpretation of our language's operations. 

Bytecode becomes yet another intermediate representation where you might build a mini-compiler for each chip so that your pipeline is general to any chip except for this last step of going from byte code to machine code. The other option is to write a virtual machine for your byte code. If we implement the VM in a language which is supported for all chips, then our virtual machine can be run on all chips. Obviously this will be slower during runtime, but it saves us the work of having
to adapt to new chip architectures. 

## Runtime
Typically, there will be services running with your program. These include garbage collection or 'instance-of' testing. In the case of code directly compiled to machine code, the runtime is embedded into the program during compilation. If we are running bytecode on a virtual machine, the runtime is a part of the virtual machine.

# Shortcuts
### Single-pass Compilers
These are simple compilers that will interweave parsing, analysis, and code generation so that machine code is produced directly in the parser without having to allocate memory for more intermediate representations and data structures like syntax trees. This means that as soon as an expression is encountered we need to know enough to compile it. 

### Tree-walk Interpreters
This technique will execute code right after parsing it into an syntax tree (with some static analysis applied). The program will traverse the syntax tree and evaluate each node as it goes.

### Transpilers
This will just convert the source code of the language you are implementing into the source code of another language. Then the compilation pipline of the destinatino source code can be used. Transpilers will typically have the same first few steps, and depending on how semantically different the languages are, some intermediate representation will be made before converting to a grammatically correct string of the source code in the other language. 

### Just-in-time Compilation
A just in time compiler will compile the bytecode to machine code and then keep searching for optimizations in performance critical areas of the application and recompile in those areas to improve performance during execution, "just in time."

## Difference between a compiler and an interpreter
These are not mutually exclusive categories. Compiling is when you go from one source language to another. An interpreter will run the program from the source code itself. Unlike a compiler it will not require the user to first get compiled machine code and then run it manually.

    So obviously we can have interpreters which includ compilers in them. So to say they are not compilers is not completely correct. 
