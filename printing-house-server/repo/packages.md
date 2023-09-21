# Module repo

Contains interfaces for accessing and persisting data.

Implementations are injected via spring dependency injection.

# Package pl.macia.printinghouse.server.bmodel

Interfaces for reading data from database.

You can create objects of these interfaces via [factory functions][1].

Keep in mind that you can't relly on type checking on these models. If
you really want to determine what type the object is, you should use
repository lookup.


[1]:https://kotlinlang.org/docs/coding-conventions.html#function-names