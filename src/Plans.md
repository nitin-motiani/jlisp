Author : Nitin Motiani

This file describes the scope of the problem and the design decisions. There will be changes to this file as 
the ideas evolve and design becomes more concrete or when there are changes. The file is mainly there 
for me to help keep track of my thoughts in a simple manner. 


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
Seems like we are going to allow the syntax with (lambda args) at least. Not the more complicated
pattern matching though

Not going to implement those in first version for sure. 

- Will most likely keep the language case sensitive. 

- Not going to handle reserved words for time being, I think. I am confused here so this point may change. 

- Not going for symbol type for time being, I think
- Quotes are definitely out for time being. But what I have figured is that for reserved things like IF, quote 
creates some weird symbol. IF is not an expression in proper language, but quote does some weird stuff. 

- Not allowing () for time being at least
- A function probably won't have a list of things to do. Not yet at least. Will have to figure out how things
 like this work : 
(define (deriv f)
   
       (define (f-prime x)
         (/ (- (f (+ x dx)) (f x))
            dx))

       f-prime)
       


Main classes :- 

abstract class Expression with a method to evaluate
inheriting classes Atomic expressions and List Expressions

The Atomic and List Expressions inheritance hierarchy will be revisited. Removed now

Inheriting from Atomic Expressions :- 

1. Variable :- Evaluate method should return value of the variable. We'll need to store mappings for this
2. Constant Literal :- evaluate should just return literal. We might just store numbers and strings here. Will finalize that later. 
Getting rid of strings. Going to have numbers and booleans for time being. Including Strings means, a lot more
pain in parsing. 

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

7. Should evaluate method take scope? Or should we have a getCurrentScope thingy going? Currently I am going to pass the scope
The scopemanager thing feels more like a global variable shit. If I think of cleaner ways, I can always change the logic.

8. Decided that proc invocation expression would handle the scope creation at least. Whether we need scopemanager or not, I 
am not 100% certain. 

9. Another big issue, the function thing in proc can't just be a symbol or a variable expression. It can be any expression. 
Now handling that while parsing is going to be a challenge. 

10. What are we going to do about NIL/NULL thing? 

11. My current plan for Functions is to have an abstract function expression. And inheriting from that 

12. In future, can I look into generics for type inference stuff? Maybe useful for numbers, strings, etc. 

13. First step towards working code is to have fixed number of args for functions. Not a list. Then we'll see. 

14. There is a huge confusion with function stuff. One idea I am playing with is to not have functions as expression type, 
but rather have a whole different type with a method call. Which handles scope creation and shit like that. The problem
with that approach is the whole pains of not fitting with expression hierarchy everywhere including the scope
maps and having problems with things like closures. Having everything be some type of expressions does help. 
Though if I have to downcast so often, maybe it's not best. Other is to have Function as an expression, but provide
a whole different method called call or something which does the arg thing and then calls the evaluate with scope. 
It's not completely clean unfortunately, but could work. 

Another is to have function expressions but wrap up shit in a Invoker to handle bindings etc, and then evaluate function
but that seems useless and ProceedureExpression can do that. 

In anyway, I need to pass args to function. Not sure yet whether function should create its scope or whether Proc 
expression should do it? 

Another idea is to have some sort of Callable interface and have function implement it so semantics are clear, whether 
the expression is callable or not is the funda. I have gone with this appraoch for time being. May have to be changed. 

15. I am starting to wonder how to handle List expression. It's not constant exactly (or is it?) Should I have a getValue
thing there separately. Should I have a whole different data interface too like callable to have getValue thingy going

16. So after even more thought a radical new design is coming to me. Maybe not everything needs to be an expression. 
A list for example can be something other than an expression, maybe. I am not really sure if all recursion will work
well by doing this. But thinking of having a more generic data or some type which just has a get value or something. 
And then have actual expression interface or something for the shit which gets evaluated. And have separate interface
callable for functions. Of course, list is really painful even there. Since it can have expressions inside it. 
I am really pained about this. And inclusion of quote at some stage will make things even more difficult. 

17. Overall I want to have idea of type and callable, data, and evaluable interfaces.But it's not concrete
 currently, and implementing quote can change it later. So not committing to it. 
 
18. All native operations will use anonymous inner class to just do the whole calling thing. While 
other user defined function expressions will create scope and do things

19. So going to keep Expression stuff for time being. Maybe can come up with a super type to remove function 
expressions from evaluable stuff. But for now, it's okay. Keeping that. To handle list and proc confusion, there will 
be just a list type for that. And evaluate will check that first thing there is an expression which can be evaluated
to a callable. That way, we are okay, I think. 

20. More enlightenment coming. We can have list of expressions (not one list, have to be very careful here). 
So I am changing User function shit from one expression to list of expressions (not a listexpression) so that 
we can have multiple statements running in a function

21. Wonder what's better. String or variable expressions for names in Definition, Assignment, Lambda and Function 
expressions. 

22. I am really pained about list doubling as procedure to be honest :( 

23. At some point, I'll have to make things make sense in terms of list and proc and other forms. Current design seems 
ugly. If I could think of a good way to handle symbols and shit, maybe I can introduce next level of hiearchy, and 
be done with it. 

24. Booleans and integers don't automatically get cast to each other, so that's nice

25. Moving to use BigDecimals for Numbers. Else can't do operations on abstract Number class

26. Seems to me our parser will tank on multiple expressions in a row type thing. Example 
a (+ 1 2). Will handle this later. Handled now by failing on parsing. 

27. We are not allowing block level stuff at least for now. That would complicate things a lot more.  

28. Not going to allow redefining of syntactic keywords. 

Exception Types Required :- 
1. Variable not found. Scheme REPL shows me unbound variable. So may show some cool message like that
2. Condition in if should be boolean type
3. Can't be evaluated. Something like just a fucking list
4. Argument numbers mismatch
5. Not an expression that can be run. (1 2 3) for example. 
6. Invalid variable name
7. Paran mismatch

Test Expressions :- 

1. (lambda (x) x)
2. (lambda (x) (x))
3. (1 2 3)
4. 2
5. a
6. (list 1 2 (if (< a 3) 8 10))
7. (list 1 2 `(if (< a 3) 8 10))
8. (define fn (lambda () (+ 2 3)
   (fn)
   (define a (list fn))
   ((car a))
   (define a (list fn))
   (a)
   
9. (define fn2 (lambda (x y) (+ x y)
   (fn 2 3)
   (define a (list fn 2 3))
   ((car a))
   ((car a) (cdr a))
   ((car a) (car (cdr a)) (cdr (cdr a)))
   ((car a) (car (cdr a)) (car (cdr (cdr a))))
   
   
10. (< 2 3 4)
11. (< 2 8 1)
12. (map (lambda (x) (* 2 x)) (list 2))
13. (define * (lambda (x y) (+ x y)))
14. (+ (equal? 1 0) 2)
15. (+)
16. (+ 1)
17. (-)
18. (- 1)
19. (- 1 2 3 4)
20. (*)
21. (* 2)
22. (/)
23. (/ 2)
24. (/ 1 2 3 4)
25. (/ 1 0)
26. (=)
27. (= 1 1 1)
28. (= 1 2 2 2)
29. )
30. (abs 2 3)
31. (list (+ 1 2) 3)
32. (map + (list 1 2 3))
33. (map + (list 1 2 3) (list 4 5))
34. (map (lambda (x) x) (list 1 2 3) (list 2 3))
35. (map +)
36. (list 1 2 (if (= a 3) (+ 1 29) (/ 2 0)))
37. (define f (lambda (x) x))
38. (map (lambda (x) (* 2 x)) (list 1 2 3))
39. (map (lambda (x) (* 2 x) (list 2)))
40. (map (lambda (x) (* 2 x) (list 2)) (list 1 2 3))
41. (define f2 (lambda (x) (x)) - need better error message
42. (define f2 (lambda (x) (x)))
    (define f3 (lambda () (+ 2 3)))
    (f2 f3)
    
43. (define closure (lambda (n) (lambda () (set! n (+ n 1)) n)))
    (define test (closure 2))
    (test)
    (test)
    (test)
    
44. factorial
45. (define f (lambda args (+ args)))

 
V2 :- 
1. Seems like Norvig's quote is doing what mit-scheme's quasiquote is doing. 
I'll probably go with Norvig interpretation. Seems like some weird setting caused that. It's cool. 

2. Surprising thing. Seems like scheme allows redefining even key syntax things like if, quote etc. 
My v1 doesn't allow that. Maybe I can allow it. I don't like it though so maybe won't. 

3. So current non concrete plan is to have symbol type. And map from that in the scope mappings. 
May have to have some mapping from string to symbol object as it's supposed to be interned. But we'll see. 
We can have one pool of symbol objects for such shit. And then we can have symbol objects created for special forms
too. And then hierarchy can be divided into just types. And then we can have evaluable and callable. And can 
just have a print pretty type shit. Seems so far that string, booleans, and strings are self evaluating. So won't 
hurt to have a self evaluating type interface if I go that route. If I do have such hierarchies. Symbol should be a 
type and not an expresion (evaluable). And function should be callable only. Also the special forms may end up 
keeping their separate class	es by the argument that for things like define, set! etc first argument is not an 
expression to be evaluated, but a string(in v1)/symbol(in v2). 

The problem with the approach of having special form classes be completely separate is that I won't be able to 
get rid of duplication in parser. So it's possible that going back to old way of having AtomicExpresion and 
ListExpression is better. 

4. May do multiple equality operators. In scheme, = is just for numbers. And eq? is ref equality, while equal? is
value equality.

5. Need to figure out a way to make NIL work. 

6. So symbol interning will require me to have a separate effort to keep a global storage for those (not that it's wrong, 
in some way it's maintaining a perm space). But in first cut, I can avoid that, and just have a Symbol implement
hashCode and equals in a way that it only depends on the string within. 

7. The most major thing is that the basic type Any/Data/Object is not evaluable. As procedures are not evaluables. 
That's fucking important. That brings us to the question about whether we should consider symbol evaluable. 
If we don't, then we need a whole different type of evaluable for just variable expressions. So far making symbol 
evaluable certainly seems a lot simpler. Anywhere I need to evaluate things as args to a procedure, evaluating 
symbol to return an actual data thing makes things a lot simpler. 

8. The thing which makes stuff murky is that if we move to the new model, then everywhere things which are 
basically list of expressions and shit like that will become list of data. And confusion comes in the sense 
of what if something needs to be evaluated. But it seems to me, we can't just put a procedure directly in the 
mid of things in a list. You could only put a reference to that, which is evaluable. 
And will evaluate to something more meaningful. So procedure without evaluate could work. 

But now that I think even more about it :P Maybe it's totally fine. My current code will mostly remain unchanged. 
Because in all list things, there has to be an expression, which will always be evalauated. Whether it's define, 
or set!, or lambda or list. They will have expression to be evaluated. You can't put a callable object there. At best
you can have a symbol/variable or some other expression whose evaluation gives you a callable. But is that enough?
 
Because my AST evaluation works on recursion of evaluating expressions and returning expressions. Here it could return
non expression things. How do I handle that? :( 
I can do data thing and some casting :(
Or I can have getEvaluable or some such shit for all data types, which probably beats the purpose. 
and sounds shit to me. 

9. Assumption is that the items in the list will always be evaluable if we are evaluating the list. 
This will need to be changed if I can think of a counter example.

10. The ListExpression class is no longer final. A further refactor can make it abstract and remove the 
evaluation logic into a class ProcExpression

11. Probably can remove Evaluable and Callable. They seem unnecessary complications at this point. 

Out of scope :- 

1. Improper lists. 
2. Symbol interning, at least not for time being. 

Examples :- 
1. (quote 100) -> 2   Probably ignore. Some weird setting caused this. 
This caused it : (define (quote a) 2). God knows how this shit works. Basically quote got redefined as a 
function. 
2. (quote (quote (+ 1 2)))
3. (define sym (quote a))
4. (equal? sym (quote a))
   (eq? sym (quote a))

5. (define lel "a")
   (equal? lel "a")
   (eq? lel "a")
   
6. (cons 'x1 '())
   (cons 'x1 (cons 'x2 '()))
   
7. (symbol-hash sym)
8. (define test (quote (lambda (x) x)))
   (list? test)
   (symbol? (car test))
   (car (cdr test))
   (list? (car (cdr test)))
   (symbol? (car (car (cdr (test)))))
   
9. (define f (lambda (x) (* x 2)))
   ((quote f) 2)   
10. (if (= a 2) 8 10 12)

11. (car (quote (quote abracadabra)))
12. (quote (quote 1))
13. (define lel (quote (lambda args (car args))))
    (symbol? (car (cdr lel)))
    (symbol? (car (car (cdr (cdr lel)))))
    
14. (list 1 2 +)
15. (set a 3)
16. (lambda (if (= a 2) 8 10) 8)
17. (define f (lambda (x y) (+ (* 2 x) y)))
    (define g (lambda (x) (f x 2)))

