Author : Nitin Motiani

This file describes the scope of the problem and the design decisions. There will be changes to this file as 
the ideas evolve and design becomes more concrete or when there are changes. The file is mainly there 
for me to help keep track of my thoughts rather in a simple manner. 


Scope :- A subset of scheme

Things out of scope :-
- A lot of advanced features, that I don't even know
- Currently not implementing quote
- No multiple levels in REPL for error handling
- No let
- Currently only following function definition using lambda syntax, i.e. 
  not going to handle this (define (f x) (* x 2))
   
- Lambda syntax is only going to be of the form where the args are passed via a list. 
So only syntax allowed right now is of type (lambda (r) (...))

No (lambda args ...) or more complicated (lambda (f.x) (...))

Not going to implement those in first version for sure. 

- Will most likely keep the language case sensitive. 

- Not going to handle reserved words for time being, I think. I am confused here so this point may change. 




Main classes :- 

abstract class Expression with a method to evaluate
inheriting classes Atomic expressions and List Expressions

The Atomic and List Expressions inheritance hierarchy will be revisited. 

Inheriting from Atomic Expressions :- 

1. Variable :- Evaluate method should return value of the variable. We'll need to store mappings for this
2. Constant Literal :- evaluate should just return literal. We might just store numbers and strings here. Will finalize that later. 

Inheriting from List Expressions :- 
1. Definitions :- Going to have a symbol and expression. When we run it, the expression will be evaluated and stored with 
mapping from the symbol. Symbol shouldn't already exist at runtime.

2. Conditionals :- Store condition, if, else expressions and evaluate accordingly. Evaluate logic will use java if which
is simple enough

3. Proc :- Is a bit challenging. You need to store list of expressions as args. That much is given, and a symbol of the proc. 
That is basic. It's the evaluate where we have to decide what to do. Whether it's a language provided function (which
we are going to implement using native java functions) or a user defined one. Some of the details here will be finalized
later as I think more on lambda. 

4. Set :- This is fairly simple. Symbol and expression. Symbol should exist at runtime.

5. Lambda :- This along with native functions is pain. We need to store list of arg symbols. And also the scope
at the function creation time. This parent scope is decided at function definition time not invocation time. We'll store 
the actual expression to evaluate. The other option is to store the function body as string and only parse and evaluate 
at evaluation time. The disadvantage of second approach is multiple times parsing. So current plan is to go with first. 
Though we are not going to have things like let which make everything more complicated, so functions will be simple
without local variables, I think.  


Scope objects :- So we need to have scope objects. Mapping from variables to values, and also a reference to parent. 
At the start of REPL loop, we'll create the global scope and pass it around. The evaluate methods will require scope. 
At function invocation time, we'll create scopes accordingly. Since scopes have pointers to parents, if a function object
is returned with scope to its parent alive, we can have a proper closure implementation too without extra work. 

- What needs to be figured out is the value in the mappings of the scope. Should they be some sort of <value, type> tuple
or something else entirely. Should the type be a java Class object, or just a string. How will we differentiate between
native and user defined functions. 


Parsing :- Not completely thought out. But shouldn't be too difficult. Will follow the broad strategy of Norvig's implementation. 
Will tokenize the input, and then parse. And do recursively. If error detected in syntax, fail fast and cascade the exception up
the stack, and catch and show at REPL level. 


Important points :- 
1. Quote is a bit confusing and I am not clear on their full purpose. So the initial version won't include them. 
To create a list, the list function should be used, rather than special form quote. 

2. Need to figure out java lambdas to decide how to store java native functions.

3. A very important thing to handle maybe to handle proper lists while parsing or evaluating. Something like (1 2) is 
not allowed in usual circumstances. Since we are not doing quote for now, it's okay to ignore. But have to remember 
that the args in lambda would be handled using list like that, and we need to be careful there. Good thing is that 
in case of lambda, this will strictly be list of symbols. 

4. How will I make type inference work? Not sure at the point. I think I'll be able to cast things in the native 
java functions, and if they are wrong type, throw. Check point below for more details. 

5. What should evaluate method of expression return? After thinking through for longer, it seems to make sense that 
the evaluate methods of expression should return another expression. And we can nest things around expressions in a cleaner 
way, I think. And the Constant Literals can have a function to get the actual value, which can be used when we are 
actually doing the computation using native java functions. 

6. Should the function expressions etc have a return type field too? This could go a long way towards figuring out 
correct type before evaluation. But I can't see a way to make this work easily for dynamic typed stuff. Can make 
it work for native functions sure, but not sure how to do it for user defined functions

7. Should evaluate method take scope? Or should we have a getCurrentScope thingy going? 


Exception Types Required :- 
1. Variable not found
2. Condition in if should be boolean type





