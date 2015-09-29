Sean's Coding Style
===

- Think about what you are doing
- Use [Critical Thinking](https://github.com/SeanShubin/seans-coding-style/blob/master/beginning-software-engineering.md)
- When problem solving, think of a problem as a
    - "discrepancy between things as desired and things as perceived"
    - from [Are Your Lights On?](http://www.geraldmweinberg.com/Site/AYLO.html)

Simplicity (more important listed earlier)

- Meet Customer Need
- Easy To Maintain
- Clearly Express Intent
- No Duplicate Code
- Concise As Possible

Early Feedback (better listed earlier)

- Instant
- Compile
- Unit Test
- Integration Test
- Smoke Test
- Deployment Manual Test
- Customer Feedback

Avoid automatic setup/teardown

- This makes it harder to look at the code and tell what it is doing
- For simple cases, explicitly invoke the setup and teardown functions
- For more complex cases, use a template method
    - The invocation of a template method makes it explicitly clear that there is some setup and/or teardown going on

Configuraiton

- environment specific constants belong in a configuration file
- constants independent of environment belong in code

No checked exceptions

- If the exception occurs at runtime, it should extend RuntimeException
- Create utility functions to catch checked exceptions and re-throw unchecked exceptions
- This hides exception handling details from the rest of the logic

Avoid custom exceptions

- The only reason to have a custom exception is if it is expected and you actually need to handle it differently
- If the exception is expected, handle it by the earliest code up the call stack that knows what to do with it, do not let it propagate to the top level
- If the exception is unexpected, let it propagate to the top level exception handler

Store multiline text as separate lines

- Don't insert newlines until it matters
- This pushes knowledge of platform specific newline separators away from your logic

No Magic

- You should be able to tell what code is doing by looking at it
- "Self documenting code" rather than "convention over configuration"

Things that make code easy to maintain

- breakpoints
    - enables interactive debugging
- stack trace
    - enables understanding of what led to the problem
- unit tests
    - enforces simple design
    - verifies behavior
- static typing
    - enforces contracts
- compiled
    - quick feedback for static typing errors
    - pre-computes behavior before launching

Things that sabotage the above list

- specifying application behavior in a config file
    - can't set breakpoints
    - harder to unit test
    - not statically typed
    - not compiled, tests have to re-load the config file
    - config files are for constants like timeouts, ports, and uri's, not behavior
- wrapping your classes in proxies
    - stack trace becomes hard to follow
- deferring wiring to runtime
    - tests suites are slowed by having to wire the application at runtime, when this information was known at compile time
    - wiring errors not caught at compile time
    - no static typing
- manipulating behavior with annotations
    - can't set breakpoints
    - harder to unit test
    - depending on the level of magic involved makes the stack trace hard to follow
    - annotations are ok if they are talking to the compiler rather than altering application behaviour, such as the scala @tailrec annotation

Coupling & Cohesion

- The practice of valuing low coupling and high cohesion has been known since Larry Constantine articulated it in the late 1960s as part of Structured Design
    - No amount of smart people or popular frameworks has since changed this, so ignore pressures to do otherwise
    - In particular, claims of "cross cutting concerns" are a nonsensical excuse to violate these coupling and cohesion principles
    - So no routing/security/logging annotated all across the application, in compilation units whose roles have nothing to do with that particular concern
    - For routing, put routing compilation units in charge of routing
    - For security, build security into your public api, as it is an api concern
    - For logging, record important events in a sensible manner as a first class concern rather than an afterthought, just like any other feature of your application
- Each compilation unit (in Java, a class), should have only one role, and the roles should not overlap
- In particular, don't mix integration concerns with logic
- Some sample roles

- Contract
    - Keeps your dependency structure loosely coupled
    - Expresses capabilities without regard to implementation
    - No logic
    - No state
    - No instance
    - Capabilities should be self documenting with sensible names and type signatures
- Entry Point
    - Application launch concern is in exactly one place
    - No observable state
    - There can be only one
        - This makes it easy to unit test your routing.
        - For comparison, consider these questions regarding your unit tests for multiple entry points
            - Do they have dependencies?
            - Are they as simple as typical unit tests?
            - Do they even exist?
    - Feedback provided by external smoke test
    - Depends on everything
    - Should only have one line, that does the following
        - Create the appropriate wiring
        - Invoke the launcher function
        - Inject the command line parameters to wiring or launcher function if needed
- Wiring
    - Chooses implementations
    - No observable state
    - Creates instances
    - Handles dependency injection
    - Handles internal configuration (that is, not environment specific)
    - No logic
        - if your language does not have first class support for laziness, lazy instantiation logic is ok
    - Feedback provided by compiler and/or integrated development environment
        - if your language is not statically typed, you will have to either create unit tests, or rely on your entry point's smoke test
    - Depends on everything it creates
    - May need to create other wiring, wiring can be needed for
        - collecting configuration
        - setting up lifecycles
        - creating the main application object
    - Wiring creation can sometimes be behind a contract (factory pattern)
        - Services may need to initiate creation of a wiring, common if a short term lifecycle is involved
        - Services should not know about the wiring directly, because they will inherit a huge amount of dependencies
        - Therefore, use the factory pattern and put it behind a contract if you need to create wiring after the application launches
- Integration (Integration Layer)
    - Talks to things outside of your application, such as filesystem, network, database, system clock
    - No observable state
    - Behind a contract
    - No branching or chaining logic, passes straight through to api calls or services
    - Usually used by a service
    - Feedback provided by integration tests
    - Servlets and data access objects will be here
- Data Transfer Object
    - Handles marshalling to and from domain objects
    - No logic other than
        - marshalling to and from domain objects
        - validation logic related to marshalling
    - Mutable state is sometimes ok
        - For example, if your language does not make it easy to partially copy an object, and your object is built up incrementally
    - Feedback provided by unit tests when logic is involved
    - No dependencies on services or integration
    - Useful for preparing data for persistence, parsing http requests, and similar tasks
    - May depend on pure function library, value objects, or domain objects
- Service (Application Layer)
    - Coordinates the application
    - No observable state
    - Behind a contract
    - Feedback provided by unit tests
    - May depend on other contracts, Domain Objects, Value Objects, Pure Function Library, Data Transfer Object
- Pure Function Library (Domain Layer)
    - Utilities
    - No observable state
    - No side effecting
    - Feedback provided by unit tests
    - No dependencies outside of parameter list for each function
    - May not depend on contracts, even in parameter list
        - if you need this, you are coordinating
        - move coordination parts to a service
    - Value Objects and other Pure functions are ok in parameter list
- Domain Object / Value Object (Domain Layer)
    - Models the application
    - No observable mutable state
    - Observable immutable state is ok
    - Feedback provided by unit tests when logic is involved
    - May generate Value Objects
    - May depend on Value Object, Pure Function Library
    - May not depend on Data Transfer Object
        - Dependency should flow in the other direction
    - May depend on contracts
        - these should be injected via the parameter list rather than stored as state
        - it is a judgement call whether to move the contract dependency to a service rather than a domain object

Common usage patterns for these roles

- The application is launched from the command line.
- The Entry Point forwards along the command line arguments to the wiring, then invokes the main launcher from the wiring.
- An http request arrives, the top level dispatcher service examines the request and delegates to the appropriate http integration.
- An http integration asks the appropriate data transfer object to construct a domain object from the http request, then forwards the domain object to a service.
- A service coordinates operations the domain object
- Domain objects apply the appropriate business rules, delegating to other services and domain object as necessary
- A service collects the resulting domain objects, and sends them to a database integration
- A database integration persists the changes, delegating to data transfer objects as necessary
