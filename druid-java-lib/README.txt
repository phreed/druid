This library is a wrapper for the specific platform.
These classes could have been implemented by the code
generator, generating the same code repeatedly, but
that would be an inappropriate application of the 
generator.
Consider the case where a method has not been implemented.
This library provides a stub rather than omitting the
call from the generated code.
