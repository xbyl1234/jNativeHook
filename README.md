# jNativeHook
 Hook Java functions and methods from C++

# Theories
## T11/06/2021
(In school edition 😀)
I've spotted another [youtube video](https://youtu.be/4Futai_P5gw) that shows off exactly what I want. Similarly to the ones I've spotted yesterday, it seems that its the exact same method of hooking. This is what I *think* it is:
1. Watch for method stubs (they have an initialization function I'm pretty sure)
2. Get the address of the generated native code for the stub (There should be a function that does this, if not there is likely a field)
3. Native hook on the stub's generated code

I think this is what I need to accomplish...

## T10/06/2021
Judging by other examples of this being done, (and none of which seeming to be public/open source :/) I have some better idea of how I might be able to do this.

New plan:
1. Native hook the jvm.dll on the method invokation function(s)
2. Check method metadata to see if its what I want
3. if so, run my code, otherwise continue

This seems to be the standard approach in the Minecraft injected client & closet cheating community. My only concern is how well or poorly it will scale with many hooks at once. Also take a look at [Today's update](#U10062021)

## 09/06/2021 Theory
I have no idea how the frick I'm going to actually pull this off yet, HOWEVER, looking at recaf's disassembly of code, I have *some* idea of how I *might* be able to make the hooks

Here's my current plan:
1. Create native export on a callback dll to handle all callbacks
2. Create a java class with a static native func that is said export
3. Modify a target method's bytecode to call the static method with some identifier
4. Native callback distributes the call to the respective callback
5. Execution returns to the rest of the java method

If this doesn't make sense, it really shouldn't because I don't even think this is a good way to do this. There must be a better way, but this is what I've come up with so far.

# Updates
## U10/06/2021

Been looking at the JRE in Ghidra and found some neat things. First here are some sigs I made, and what I think I know based on the OpenJDK source code. Been using an OpenJDK build for all of this so far. Starting with Java 8 and then moving to Java 11 after. Once I have something made for those versions, I'll target the latest Java. Anyway, enough talk, here's what I found:

### Reflection - invoke function
``invoke (void invoke(longlong * klass, longlong * * reflected_method, longlong * receiver, char override, longlong * ptypes, uint rtype, longlong * args, undefined8 is_method_invoke, longlong * TRAPS))``

``?  89 ?  ?  ?  ?  89 ?  ?  ?  ?  89 ?  ?  ?  55 56 57 41 ?  41 ?  41 ?  41 ?  48 ?  ?  ?  ?  ?  ?  ?  48 81``

[Source on OpenJDK](https://github.com/openjdk/jdk/blob/master/src/hotspot/share/runtime/reflection.cpp#L953)

### JavaCalls - call function
``void JavaCalls::call(JavaValue* result, const methodHandle& method, JavaCallArguments* args, TRAPS)``

``40 ?  48 83 ?  ?  ?  89 ?  ?  ?  4D 8B ?  48``

[Source on OpenJDK](https://github.com/openjdk/jdk/blob/master/src/hotspot/share/runtime/javaCalls.cpp#L338)

### JavaCalls - call_helper function
``void JavaCalls::call_helper(JavaValue* result, const methodHandle& method, JavaCallArguments* args, TRAPS)``

``48 8B ?  ?  89 ?  ?  ?  89 ?  ?  55 56 41 ?  48 ?  ?  ?  ?  48 81 ?  ?  ?  ?  ?  48 ?  ?  ?  89``

[Source on OpenJDK](https://github.com/openjdk/jdk/blob/master/src/hotspot/share/runtime/javaCalls.cpp#L346)

### JavaCalls - call_helper function
``JavaCallWrapper::JavaCallWrapper(const methodHandle& callee_method, Handle receiver, JavaValue* result, TRAPS)``

``?  89 ?  ?  ?  ?  89 ?  ?  ?  ?  89 ?  ?  ?  ?  89 ?  ?  ?  57 41 ?  41 ?  48 83 ?  ?  48``

[Source on OpenJDK](https://github.com/openjdk/jdk/blob/master/src/hotspot/share/runtime/javaCalls.cpp#L55)

## Whats the significance?
It seems to me that the JavaCallWrapper is what actually is the beginning of code execution. Really, I have no clue, but it is what it seems. Looking at the JavaCalls file, you'll see there are lots of different types of calls and the jvm all treats them slightly different, However they all seem to hit the JavaCallWrapper at some point, leading me to believe this might be where I want to hook natively. Doing this, I might be able to just read the params from memory. A closet cheater has pulled off essentially exactly what I'm trying to do, but I don't think they ever published their sources, which is why I'm making this effort. They uploaded [A youtube video](https://youtu.be/UHhoBla4IZE) showing off the hooking. The description gives some insight on how it was done as well. This approach of hooking and then grabbing the params from the stack seems to be the best option. They also have [another video](https://youtu.be/YNK0mpc6728) which seems to display some callback code, and it looks like they are checking if it is the function they want by comparing the name. I find this interesting because wouldn't this mean every func call their code is executed? Why not just hook the methods individually? Is that not a viable option? Anycase, thanks to [tudou](https://github.com/tudou) for showing me these videos.
