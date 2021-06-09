# jNativeHook
 Hook Java functions and methods from C++


# Theory
I have no idea how the frick I'm going to actually pull this off yet, HOWEVER, looking at recaf's disassembly of code, I have *some* idea of how I *might* be able to make the hooks

Here's my current plan:
1. Create native export on a callback dll to handle all callbacks
2. Create a java class with a static native func that is said export
3. Modify a target method's bytecode to call the static method with some identifier
4. Native callback distributes the call to the respective callback
5. Execution returns to the rest of the java method

If this doesn't make sense, it really shouldn't because I don't even think this is a good way to do this. There must be a better way, but this is what I've come up with so far.